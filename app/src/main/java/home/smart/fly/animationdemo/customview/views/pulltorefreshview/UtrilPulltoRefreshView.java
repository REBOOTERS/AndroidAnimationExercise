package home.smart.fly.animationdemo.customview.views.pulltorefreshview;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by co-mall on 2017/4/19.
 */

public class UtrilPulltoRefreshView extends LinearLayout {
    private static final String TAG = "UtrilPulltoRefreshView";
    // refresh states
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    // pull state
    private static final int PULL_UP_STATE = 0;
    private static final int PULL_DOWN_STATE = 1;

    /**
     * list or grid
     */
    private AdapterView<?> mAdapterView;
    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * ScrollView
     */
    private ScrollView mScrollView;
    /**
     * WebView
     */
    private WebView mWebView;

    private int mHeaderState;
    /**
     * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
     */
    private int mPullState;

    private View headerView;

    private int headViewHeight;

    //action
    private int lastY;

    private BaseHeaderAdapter mBaseHeaderAdapter;

    public void setBaseHeaderAdapter(BaseHeaderAdapter baseHeaderAdapter) {
        mBaseHeaderAdapter = baseHeaderAdapter;
    }

    public UtrilPulltoRefreshView(Context context) {
        super(context);
        init(context);
    }

    public UtrilPulltoRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UtrilPulltoRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        if (mBaseHeaderAdapter == null) {
            mBaseHeaderAdapter = new ProgressBarAdapter(context);
        }
        initView();
    }

    private void initView() {
        headerView = mBaseHeaderAdapter.getHeaderView();
        measureView(headerView);
        headViewHeight = headerView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, headViewHeight);
        params.topMargin = -headViewHeight;
        addView(headerView, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initSubViewType();
    }

    private void initSubViewType() {
        int count = getChildCount();
        if (count < 2) {
            throw new IllegalArgumentException(
                    "this layout must contain 2 child views,and AdapterView or ScrollView must in the second position!");
        }

        View view = getChildAt(1);

        if (view instanceof AdapterView<?>) {
            mAdapterView = (AdapterView<?>) view;
        }

        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
        }

        if (view instanceof ScrollView) {
            mScrollView = (ScrollView) view;
        }

        if (view instanceof WebView) {
            mWebView = (WebView) view;
        }

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - lastY;
                if (isParentViewScroll(deltaY)) {
                    Log.e(TAG, "onInterceptTouchEvent: belong to ParentView");
                    return true; //此时,触发onTouchEvent事件
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - lastY;
                if (mPullState == PULL_DOWN_STATE) {
                    Log.e(TAG, "onTouchEvent: pull down begin-->" + deltaY);
                    initHeaderViewToRefresh(deltaY);
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int topMargin = getHeaderTopMargin();
                Log.e(TAG, "onTouchEvent: topMargin==" + topMargin);
                if (mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        headerRefreshing();
                    } else {
                        setHeaderTopMargin(-headViewHeight);
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void initHeaderViewToRefresh(int deltaY) {
        int topDistance = UpdateHeadViewMarginTop(deltaY);
        if (topDistance < 0 && topDistance > -headViewHeight) {
            mBaseHeaderAdapter.pullViewToRefresh();
            mHeaderState = PULL_TO_REFRESH;
        } else if (topDistance > 0 && mHeaderState != RELEASE_TO_REFRESH) {
            mBaseHeaderAdapter.releaseViewToRefresh();
            mHeaderState = RELEASE_TO_REFRESH;
        }

    }

    private int UpdateHeadViewMarginTop(int deltaY) {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        float topMargin = params.topMargin + deltaY * 0.3f;
        params.topMargin = (int) topMargin;
        headerView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }


    private void headerRefreshing() {
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);
        mBaseHeaderAdapter.headerRefreshing();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshComplete();
            }
        }, 2000);
    }

    private void refreshComplete() {
        setHeaderTopMargin(-headViewHeight);
        mBaseHeaderAdapter.headerRefreshComplete();
        mHeaderState = PULL_TO_REFRESH;
    }

    /**
     * 滑动由父View（当前View）处理
     *
     * @param deltaY
     * @return
     */
    private boolean isParentViewScroll(int deltaY) {
        boolean belongToParentView = false;
        if (mHeaderState == REFRESHING) {
            belongToParentView = false;
        }

        if (mAdapterView != null) {

            if (deltaY > 0) {
                View child = mAdapterView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }

                if (mAdapterView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            }
        }


        if (mRecyclerView != null) {
            if (deltaY > 0) {
                View child = mRecyclerView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }
                LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstPosition == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            }
        }

        if (mScrollView != null) {
            if (deltaY > 0) {
                View child = mScrollView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }

                int distance = mScrollView.getScrollY();
                if (distance == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            }
        }

        if (mWebView != null) {
            if (deltaY > 0) {
                View child = mWebView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }

                int distance = mWebView.getScrollY();
                if (distance == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            }
        }


        return belongToParentView;
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 获取当前header view 的topMargin
     *
     * @return
     * @description
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        return params.topMargin;
    }

    /**
     * 设置header view 的topMargin的值
     *
     * @param topMargin ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
     *                  hylin 2012-7-31上午11:24:06
     * @description
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        params.topMargin = topMargin;
        headerView.setLayoutParams(params);
        invalidate();
    }
}
