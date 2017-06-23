package home.smart.fly.animations.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;


/**
 * 一个轻量级的SwipeMenu控件，将滑动时滑动比例及滑动参数通过接口传递至
 * 视图层，进行动画处理
 */
public class LiteSwipeMenu extends ViewGroup {

    private float xLastIntercept = 0;
    private float yLastIntercept = 0;
    private float xLast = 0;
    private Scroller mScroller;
    private int mScreenWidth;
    private int mScreenHeight;
    private View mMenuView; //菜单视图
    private View mContentView; //内容视图
    private int mMenuOffset; //菜单距右边的距离
    private boolean isMeasured = false; //是否已经测量过
    private int mTouchSlop; //系统可识别最小滑动距离

    private onMenuSwipeListener mListener;


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

    private void init(Context context) {
        mScroller = new Scroller(context);
        mScreenWidth = LiteMenuHelper.getScreenWidth(context);
        mScreenHeight = LiteMenuHelper.getScreenHeight(context);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        //是否拦截事件
        boolean intercept = false;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float xDelta = x - xLastIntercept;
                float yDelta = y - yLastIntercept;
                //视图总滑动距离小于屏幕宽度
                if (x + getScrollX() < mScreenWidth) {
                    //1.消除纵向滑动冲突；2.忽略小于系统级别的滑动
                    if (Math.abs(xDelta) > Math.abs(yDelta) && Math.abs(xDelta) > mTouchSlop) {
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
        xLastIntercept = x;
        yLastIntercept = y;
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
                //整个视图按照滑动变化，步进式的滑动
                scrollBy((int) (-offset), 0);
                // 获取菜单视图在横向滑动中的比例
                float progress = 1 - getScrollX() * 1.0f / (mScreenWidth - mMenuOffset);
                if (mListener != null) {
                    mListener.onProgressChange(progress, getScrollX());
                }
                break;
            case MotionEvent.ACTION_UP:
                //横向滑动的距离小于屏幕宽度的一半时，菜单依旧保持打开
                if (getScrollX() < (mScreenWidth - mMenuOffset) / 2) {
                    openMenu();
                } else {
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
            //将视图向左移动，保持菜单隐藏
            scrollTo(mScreenWidth - mMenuOffset, 0);
        }
    }


    public void openMenu() {

        mScroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 500);
        if (mListener != null) {
            mListener.onProgressChange(1, 0);
        }
        invalidate();
    }

    public void closeMenu() {
        mScroller.startScroll(getScrollX(), 0, mScreenWidth - mMenuOffset - getScrollX(), 0);
        if (mListener != null) {
            mListener.onProgressChange(0, mScreenWidth - mMenuOffset);
        }
        invalidate();
    }


    /**
     * 设置菜单右边距，这里按屏幕宽度 百分比取值
     * @param factor 百分比系数（0-1.0 之前)
     */
    public void setMenuOffset(float factor) {
        if (Math.abs(factor) > 1.0) {
            factor = 1.0f;
        }
        mMenuOffset = (int) (mScreenWidth * Math.abs(factor));
    }


    /**
     * 获取视图的Activity实例，同时设置状态栏为透明色,保持菜单栏和视图内容可以沉寝式的显示。
     * @param activity
     */
    public void attachWithActivity(Activity activity) {
        LiteMenuHelper.addStatusBar(activity);
    }

    public interface onMenuSwipeListener {

        void onProgressChange(float progress, int scrollX);
    }

    public void setOnSwipeProgressListener(onMenuSwipeListener listener) {
        this.mListener = listener;
    }

}
