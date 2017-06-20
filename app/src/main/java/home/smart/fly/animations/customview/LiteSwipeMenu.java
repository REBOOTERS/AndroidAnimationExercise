package home.smart.fly.animations.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
    private boolean isMenuOpened = false; //是否已经显示了菜单
    private onMenuSwipeListener mListener; //滑动监听

    public LiteSwipeMenu(Context context) {
        super(context);
        init(context);
    }

    public LiteSwipeMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiteSwipeMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                if (!isMenuOpened()) {
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
                float offset = x - xLast;
                if (getScrollX() - offset <= 0) {
                    offset = 0;
                } else if (getScrollX() + mScreenWidth - offset > mScreenWidth * 2 - mMenuOffset) {
                    offset = 0;
                }
                scrollBy((int) (-offset), 0); //跟随拖动

                if (mListener != null) {
                    float progress = 1 - getScrollX() * 1.0f / (mScreenWidth - mMenuOffset);
                    mListener.onProgressChange(progress, mMenuView, mContentView);
                }
                break;
            case MotionEvent.ACTION_UP:
                //横向滑动的距离小于菜单宽度的一半时，菜单依旧保持打开
                if (getScrollX() < (mScreenWidth - mMenuOffset) / 2) {
                    //滑动菜单
                    openMenu();
                } else {
                    //滑动到内容
                    closeMenu();
                }
                break;
        }
        xLast = x;
        return false;
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
            mMenuView.setBackgroundColor(Color.TRANSPARENT);
            mMenuView.layout(0, 0, mScreenWidth - mMenuOffset, mScreenHeight);
            mContentView.layout(mScreenWidth - mMenuOffset, 0, mScreenWidth - mMenuOffset + mScreenWidth, mScreenHeight);
            scrollTo(mScreenWidth - mMenuOffset, 0);
        }
    }

    public interface onMenuSwipeListener {

        void onProgressChange(float progress, View menuView, View contentView);
    }

    public void setOnSwipeProgressListener(onMenuSwipeListener listener) {
        this.mListener = listener;
    }

    public boolean isMenuOpened() {
        if (getScrollX() <= 0) {
            isMenuOpened = true;
        } else {
            isMenuOpened = false;
        }
        return isMenuOpened;
    }

    public void openMenu() {
        mScroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0);
        if (mListener != null) {
            mListener.onProgressChange(1, mMenuView, mContentView);
        }
        invalidate();
    }

    public void closeMenu() {
        mScroller.startScroll(getScrollX(), 0, mScreenWidth - mMenuOffset - getScrollX(), 0);
        mListener.onProgressChange(0, mMenuView, mContentView);
        invalidate();
    }

    private float Dp2Px(Context context, float dpi) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context.getResources().getDisplayMetrics());
    }

    public void setMenuOffset(float factor) {
        if (Math.abs(factor) > 1.0) {
            factor = 1.0f;
        }
        mMenuOffset = (int) (mScreenWidth * Math.abs(factor));
    }


    public void attachWithActivity(Activity activity) {
        LiteMenuHelper.setStatusBar(activity);
    }


}
