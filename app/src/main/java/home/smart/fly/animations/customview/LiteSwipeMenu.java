package home.smart.fly.animations.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;


public class LiteSwipeMenu extends ViewGroup {

    private float xIntercept = 0;
    private float yIntercept = 0;
    private float xLast = 0;
    private Scroller mScroller;
    private int mScreenWidth;
    private int mScreenHeight;
    private View mMenuView;
    private View mContentView;
    private int mDragWipeOffset; //侧边拖动的偏移值
    private int mMenuOffset; //菜单距右边的距离
    private boolean isMeasured = false; //是否已经测量过
    private boolean isMenuShowing = false; //是否已经显示了菜单
    private onMenuSwipeListener mListener; //滑动监听


    public LiteSwipeMenu(Context context) {
        this(context, null);
    }

    public LiteSwipeMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    //添加背景图获取屏幕宽高
    private void init(Context context) {
        mScroller = new Scroller(context);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        boolean intercept = false;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float xDelta = x - xIntercept;
                float yDelta = y - yIntercept;
                if (mDragWipeOffset == 0 && Math.abs(xDelta) > 20) { //全屏滑动
                    intercept = true;
                    break;
                }
                if (!isMenuShowing()) {
                    if (x >= Dp2Px(getContext(), mDragWipeOffset)) {
                        return false;
                    }
                }
                if (x + getScrollX() < mScreenWidth + mDragWipeOffset) {

                    if (Math.abs(xDelta) > Math.abs(yDelta) && Math.abs(xDelta) > 20) { //X滑动主导
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        xLast = x;
        xIntercept = x;
        yIntercept = y;
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float xDelta = x - xLast;
                float offset = xDelta;
                touchMove_deal(offset);
                break;
            case MotionEvent.ACTION_UP:
                touchUp_deal();
                break;
        }
        xLast = x;
        return false;
    }


    //滑动处理
    private void touchMove_deal(float offset) {
        if (getScrollX() - offset <= 0) {
            offset = 0;
        } else if (getScrollX() + mScreenWidth - offset > mScreenWidth * 2 - mMenuOffset) {
            offset = 0;
        }
        scrollBy((int) (-offset), 0); //跟随拖动

        if (mListener != null) {
            mListener.onProgressChange(offset);
        }

    }


    //抬起处理
    private void touchUp_deal() {
        if (getScrollX() < (mScreenWidth - mMenuOffset) / 2) {
            //滑动菜单
            openMenu();
        } else {
            //滑动到内容
            closeMenu();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isMeasured) {
            mMenuView = getChildAt(0);
            mContentView = getChildAt(1);
            mMenuView.getLayoutParams().width = mScreenWidth - mMenuOffset;
            mMenuView.getLayoutParams().height = mScreenHeight;
            mContentView.getLayoutParams().width = mScreenWidth;
            mContentView.getLayoutParams().height = mScreenHeight;
            measureChild(mMenuView, widthMeasureSpec, heightMeasureSpec);
            measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
            isMeasured = true;
        }
        setMeasuredDimension(mScreenWidth * 2 - mMenuOffset, mScreenHeight);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (b) {
            mContentView.setClickable(true);
            mMenuView.setClickable(true);
            mMenuView.layout(0, 0, mScreenWidth - mMenuOffset, mScreenHeight);
            mContentView.layout(mScreenWidth - mMenuOffset, 0, mScreenWidth - mMenuOffset + mScreenWidth, mScreenHeight);
            scrollTo(mScreenWidth - mMenuOffset, 0);
        }
    }

    public interface onMenuSwipeListener {

        void onProgressChange(float progress);
    }

    public void setOnSwipeProgressListener(onMenuSwipeListener listener) {
        this.mListener = listener;
    }

    public boolean isMenuShowing() {
        if (getScrollX() <= 0) {
            isMenuShowing = true;
        } else {
            isMenuShowing = false;
        }
        return isMenuShowing;
    }

    public void openMenu() {
        mScroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0);
        invalidate();
    }

    public void closeMenu() {
        mScroller.startScroll(getScrollX(), 0, mScreenWidth - mMenuOffset - getScrollX(), 0);
        invalidate();
    }

    private float Dp2Px(Context context, float dpi) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context.getResources().getDisplayMetrics());
    }

    public void setMenuOffset(float factor) {
        if (Math.abs(factor) > 1.0) {
            factor = 1.0f;
        }
        mMenuOffset = (int) (mScreenWidth*Math.abs(factor));
    }
}
