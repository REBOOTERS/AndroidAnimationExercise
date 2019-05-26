package com.engineer.imitate.ui.list.layoutmanager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.engineer.imitate.BuildConfig;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhuyongging
 * @since: 2019-05-26
 * <p>
 * {[FocusLayoutManager]https://github.com/CCY0122/FocusLayoutManager}
 */


public class FocusLayoutManager extends RecyclerView.LayoutManager {
    public static final String TAG = "FocusLayoutManager";
    /**
     * 堆叠方向在左
     */
    public static final int FOCUS_LEFT = 1;
    /**
     * 堆叠方向在右
     */
    public static final int FOCUS_RIGHT = 2;
    /**
     * 堆叠方向在上
     */
    public static final int FOCUS_TOP = 3;
    /**
     * 堆叠方向在下
     */
    public static final int FOCUS_BOTTOM = 4;

    /**
     * 最大可堆叠层级
     */
    int maxLayerCount;

    /**
     * 堆叠的方向。
     * 期望滚动方向为水平时，传{@link #FOCUS_LEFT}或{@link #FOCUS_RIGHT}；
     * 期望滚动方向为垂直时，传{@link #FOCUS_TOP}或{@link #FOCUS_BOTTOM}。
     */
    @FocusOrientation
    private int focusOrientation = FOCUS_LEFT;
    /**
     * 堆叠view之间的偏移量
     */
    private float layerPadding;
    /**
     * 普通view之间的margin
     */
    private float normalViewGap;
    /**
     * 是否自动选中
     */
    private boolean isAutoSelect;
    /**
     * 变换监听接口。
     */
    private List<TrasitionListener> trasitionListeners;
    /**
     *
     */
    private OnFocusChangeListener onFocusChangeListener;
    /**
     * 水平方向累计偏移量
     */
    private long mHorizontalOffset;
    /**
     * 垂直方向累计偏移量
     */
    private long mVerticalOffset;
    /**
     * 屏幕可见的第一个View的Position
     */
    private int mFirstVisiPos;
    /**
     * 屏幕可见的最后一个View的Position
     */
    private int mLastVisiPos;
    /**
     * 一次完整的聚焦滑动所需要移动的距离。
     */
    private float onceCompleteScrollLength = -1;
    /**
     * 焦点view的position
     */
    private int focusdPosition = -1;
    /**
     * 自动选中动画
     */
    private ValueAnimator selectAnimator;
    private long autoSelectMinDuration;
    private long autoSelectMaxDuration;


    @IntDef({FOCUS_LEFT, FOCUS_RIGHT, FOCUS_TOP, FOCUS_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusOrientation {
    }


    public FocusLayoutManager() {
        this(new Builder());
    }

    private FocusLayoutManager(Builder builder) {
        this.maxLayerCount = builder.maxLayerCount;
        this.focusOrientation = builder.focusOrientation;
        this.layerPadding = builder.layerPadding;
        this.trasitionListeners = builder.trasitionListeners;
        this.normalViewGap = builder.normalViewGap;
        this.isAutoSelect = builder.isAutoSelect;
        this.onFocusChangeListener = builder.onFocusChangeListener;
        this.autoSelectMinDuration = builder.autoSelectMinDuration;
        this.autoSelectMaxDuration = builder.autoSelectMaxDuration;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }

        onceCompleteScrollLength = -1;
        //分离全部已有的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);

        fill(recycler, state, 0);
    }


    @Override
    public boolean canScrollHorizontally() {
        return focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT;
    }

    @Override
    public boolean canScrollVertically() {
        return focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler,
                                    RecyclerView.State state) {
        //手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;

        //位移0、没有子View 当然不移动
        if (dx == 0 || getChildCount() == 0) {
            return 0;
        }

        mHorizontalOffset += dx;//累加实际滑动距离


        dx = fill(recycler, state, dx);

        return dx;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                  RecyclerView.State state) {
        //位移0、没有子View 当然不移动
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        mVerticalOffset += dy;//累加实际滑动距离


        dy = fill(recycler, state, dy);

        return dy;
    }


    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        cancelAnimator();
        super.onDetachedFromWindow(view, recycler);
    }

    /**
     * @param recycler
     * @param state
     * @param delta
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int delta) {
        int resultDelta = delta;

        //为了可读性，分成四个方法
        switch (focusOrientation) {
            case FOCUS_LEFT:
                resultDelta = fillHorizontalLeft(recycler, state, delta);
                break;
            case FOCUS_RIGHT:
                resultDelta = fillHorizontalRight(recycler, state, delta);
                break;
            case FOCUS_TOP:
                resultDelta = fillVerticalTop(recycler, state, delta);
                break;
            case FOCUS_BOTTOM:
                resultDelta = fillVerticalBottom(recycler, state, delta);
                break;
            default:
                break;
        }

        recycleChildren(recycler);

//        log("childCount= [" + getChildCount() + "]" + ",[recycler.getScrapList().size():" +
// recycler.getScrapList().size());
        return resultDelta;
    }


    /**
     * 水平滚动、向左堆叠布局
     *
     * @param recycler
     * @param state
     * @param dx       偏移量。手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
     */
    private int fillHorizontalLeft(RecyclerView.Recycler recycler, RecyclerView.State state,
                                   int dx) {

        //----------------1、边界检测-----------------
        if (dx < 0) {
            //已达左边界
            if (mHorizontalOffset < 0) {
                mHorizontalOffset = dx = 0;
            }
        }

        if (dx > 0) {
            //滑动到只剩堆叠view，没有普通view了，说明已经到达右边界了
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                //因为scrollHorizontallyBy里加了一次dx，现在减回去
                mHorizontalOffset -= dx;
                dx = 0;
            }
        }

        //分离全部的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);

        //----------------2、初始化布局数据-----------------

        float startX = getPaddingLeft() - layerPadding;

        View tempView = null;
        int tempPosition = -1;
        if (onceCompleteScrollLength == -1) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos;
            tempView = recycler.getViewForPosition(tempPosition);
            measureChildWithMargins(tempView, 0, 0);
            onceCompleteScrollLength = getDecoratedMeasurementHorizontal(tempView) + normalViewGap;
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        float fraction =
                (Math.abs(mHorizontalOffset) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        float layerViewOffset = layerPadding * fraction;
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        float normalViewOffset = onceCompleteScrollLength * fraction;
        boolean isLayerViewOffsetSetted = false;
        boolean isNormalViewOffsetSetted = false;

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos = (int) Math.floor(Math.abs(mHorizontalOffset) / onceCompleteScrollLength); //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = getItemCount() - 1;

//        log("fill.fraction = " + fraction + ";offset = " + mHorizontalOffset + ";mfirstPos = "
// + mFirstVisiPos);


        int newFocusedPosition = mFirstVisiPos + maxLayerCount - 1;
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChanged(newFocusedPosition, focusdPosition);
            }
            focusdPosition = newFocusedPosition;
        }


        //----------------3、开始布局-----------------

        for (int i = mFirstVisiPos; i <= mLastVisiPos; i++) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                View item;

                if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    item = tempView;
                } else {
                    item = recycler.getViewForPosition(i);
                }
                addView(item);
                measureChildWithMargins(item, 0, 0);

                startX += layerPadding;
                if (!isLayerViewOffsetSetted) {
                    startX -= layerViewOffset;
                    isLayerViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        trasitionListener.handleLayerView(this, item, i - mFirstVisiPos,
                                maxLayerCount, i, fraction, dx);
                    }
                }

                int l, t, r, b;
                l = (int) startX;
                t = getPaddingTop();
                r = (int) (startX + getDecoratedMeasurementHorizontal(item));
                b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                layoutDecoratedWithMargins(item, l, t, r, b);


            } else {//属于普通区域

                View item = recycler.getViewForPosition(i);
                addView(item);
                measureChildWithMargins(item, 0, 0);

                startX += onceCompleteScrollLength;
                if (!isNormalViewOffsetSetted) {
                    startX += layerViewOffset;
                    startX -= normalViewOffset;
                    isNormalViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener.handleFocusingView(this, item, i, fraction, dx);
                        } else {
                            trasitionListener.handleNormalView(this, item, i, fraction, dx);
                        }
                    }
                }

                int l, t, r, b;
                l = (int) startX;
                t = getPaddingTop();
                r = (int) (startX + getDecoratedMeasurementHorizontal(item));
                b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                layoutDecoratedWithMargins(item, l, t, r, b);

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startX + onceCompleteScrollLength > getWidth() - getPaddingRight()) {
                    mLastVisiPos = i;
                    break;
                }
            }
        }

        return dx;
    }

    /**
     * 水平滚动、向右堆叠布局。详细注释请参考
     * {@link #fillHorizontalLeft(RecyclerView.Recycler, RecyclerView.State, int)}。
     *
     * @param recycler
     * @param state
     * @param dx       偏移量。手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
     */
    private int fillHorizontalRight(RecyclerView.Recycler recycler, RecyclerView.State state,
                                    int dx) {

        //----------------1、边界检测-----------------

        //从最右边开始布局，所以mHorizontalOffset一直是负数
        if (dx > 0) {
            if (mHorizontalOffset > 0) {
                mHorizontalOffset = dx = 0;
            }
        }
        if (dx < 0) {
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                mHorizontalOffset -= dx;
                dx = 0;
            }
        }

        detachAndScrapAttachedViews(recycler);

        //----------------2、初始化布局数据-----------------

        float startX = getWidth() - getPaddingRight() + layerPadding;

        View tempView = null;
        int tempPosition = -1;
        if (onceCompleteScrollLength == -1) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos;
            tempView = recycler.getViewForPosition(tempPosition);
            measureChildWithMargins(tempView, 0, 0);
            onceCompleteScrollLength = getDecoratedMeasurementHorizontal(tempView) + normalViewGap;
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        float fraction =
                (Math.abs(mHorizontalOffset) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        float layerViewOffset = layerPadding * fraction;
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        float normalViewOffset = onceCompleteScrollLength * fraction;
        boolean isLayerViewOffsetSetted = false;
        boolean isNormalViewOffsetSetted = false;

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos = (int) Math.floor(Math.abs(mHorizontalOffset) / onceCompleteScrollLength); //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = getItemCount() - 1;


        int newFocusedPosition = mFirstVisiPos + maxLayerCount - 1;
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChanged(newFocusedPosition, focusdPosition);
            }
            focusdPosition = newFocusedPosition;
        }


        //----------------3、开始布局-----------------

        for (int i = mFirstVisiPos; i <= mLastVisiPos; i++) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                View item;

                if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    item = tempView;
                } else {
                    item = recycler.getViewForPosition(i);
                }
                addView(item);
                measureChildWithMargins(item, 0, 0);

                startX -= layerPadding;
                if (!isLayerViewOffsetSetted) {
                    startX += layerViewOffset;
                    isLayerViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        trasitionListener.handleLayerView(this, item, i - mFirstVisiPos,
                                maxLayerCount, i, fraction, dx);
                    }
                }

                int l, t, r, b;
                l = (int) (startX - getDecoratedMeasurementHorizontal(item));
                t = getPaddingTop();
                r = (int) (startX);
                b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                layoutDecoratedWithMargins(item, l, t, r, b);


            } else {//属于普通区域

                View item = recycler.getViewForPosition(i);
                addView(item);
                measureChildWithMargins(item, 0, 0);
                startX -= onceCompleteScrollLength;
                if (!isNormalViewOffsetSetted) {
                    startX -= layerViewOffset;
                    startX += normalViewOffset;
                    isNormalViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener.handleFocusingView(this, item, i, fraction, dx);
                        } else {
                            trasitionListener.handleNormalView(this, item, i, fraction, dx);
                        }
                    }
                }

                int l, t, r, b;
                l = (int) (startX - getDecoratedMeasurementHorizontal(item));
                t = getPaddingTop();
                r = (int) (startX);
                b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                layoutDecoratedWithMargins(item, l, t, r, b);

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startX - onceCompleteScrollLength < getPaddingLeft()) {
                    mLastVisiPos = i;
                    break;
                }
            }
        }

        return dx;
    }

    /**
     * 垂直滚动、向上堆叠布局
     * 详细注释请参考{@link #fillHorizontalLeft(RecyclerView.Recycler, RecyclerView.State, int)}。
     *
     * @param recycler
     * @param state
     * @param dy       偏移量。手指从上向下滑动，dy < 0; 手指从下向上滑动，dy > 0;
     */
    private int fillVerticalTop(RecyclerView.Recycler recycler, RecyclerView.State state,
                                int dy) {

        //----------------1、边界检测-----------------
        if (dy < 0) {
            if (mVerticalOffset < 0) {
                mVerticalOffset = dy = 0;
            }
        }

        if (dy > 0) {
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                mVerticalOffset -= dy;
                dy = 0;
            }
        }

        detachAndScrapAttachedViews(recycler);

        //----------------2、初始化布局数据-----------------

        float startY = getPaddingTop() - layerPadding;

        View tempView = null;
        int tempPosition = -1;
        if (onceCompleteScrollLength == -1) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos;
            tempView = recycler.getViewForPosition(tempPosition);
            measureChildWithMargins(tempView, 0, 0);
            onceCompleteScrollLength = getDecoratedMeasurementVertical(tempView) + normalViewGap;
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        float fraction =
                (Math.abs(mVerticalOffset) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        float layerViewOffset = layerPadding * fraction;
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        float normalViewOffset = onceCompleteScrollLength * fraction;
        boolean isLayerViewOffsetSetted = false;
        boolean isNormalViewOffsetSetted = false;

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos = (int) Math.floor(Math.abs(mVerticalOffset) / onceCompleteScrollLength);
        //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = getItemCount() - 1;


        int newFocusedPosition = mFirstVisiPos + maxLayerCount - 1;
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChanged(newFocusedPosition, focusdPosition);
            }
            focusdPosition = newFocusedPosition;
        }


        //----------------3、开始布局-----------------

        for (int i = mFirstVisiPos; i <= mLastVisiPos; i++) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                View item;

                if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    item = tempView;
                } else {
                    item = recycler.getViewForPosition(i);
                }
                addView(item);
                measureChildWithMargins(item, 0, 0);

                startY += layerPadding;
                if (!isLayerViewOffsetSetted) {
                    startY -= layerViewOffset;
                    isLayerViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        trasitionListener.handleLayerView(this, item, i - mFirstVisiPos,
                                maxLayerCount, i, fraction, dy);
                    }
                }

                int l, t, r, b;
                l = getPaddingLeft();
                t = (int) startY;
                r = getPaddingLeft() + getDecoratedMeasurementHorizontal(item);
                b = (int) (startY + getDecoratedMeasurementVertical(item));
                layoutDecoratedWithMargins(item, l, t, r, b);


            } else {//属于普通区域

                View item = recycler.getViewForPosition(i);
                addView(item);
                measureChildWithMargins(item, 0, 0);
                startY += onceCompleteScrollLength;
                if (!isNormalViewOffsetSetted) {
                    startY += layerViewOffset;
                    startY -= normalViewOffset;
                    isNormalViewOffsetSetted = true;
                }


                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener.handleFocusingView(this, item, i, fraction, dy);
                        } else {
                            trasitionListener.handleNormalView(this, item, i, fraction, dy);
                        }
                    }
                }

                int l, t, r, b;
                l = getPaddingLeft();
                t = (int) startY;
                r = getPaddingLeft() + getDecoratedMeasurementHorizontal(item);
                b = (int) (startY + getDecoratedMeasurementVertical(item));
                layoutDecoratedWithMargins(item, l, t, r, b);

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startY + onceCompleteScrollLength > getHeight() - getPaddingBottom()) {
                    mLastVisiPos = i;
                    break;
                }
            }
        }

        return dy;
    }

    /**
     * 垂直滚动、向下堆叠布局
     * 详细注释请参考{@link #fillHorizontalLeft(RecyclerView.Recycler, RecyclerView.State, int)}。
     *
     * @param recycler
     * @param state
     * @param dy       偏移量。手指从上向下滑动，dy < 0; 手指从下向上滑动，dy > 0;
     */
    private int fillVerticalBottom(RecyclerView.Recycler recycler, RecyclerView.State state,
                                   int dy) {

        //----------------1、边界检测-----------------
        //从最下边开始布局，所以mVerticalOffset一直是负数

        if (dy > 0) {
            //已达上边界
            if (mVerticalOffset > 0) {
                mVerticalOffset = dy = 0;
            }
        }

        if (dy < 0) {
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                mVerticalOffset -= dy;
                dy = 0;
            }
        }

        detachAndScrapAttachedViews(recycler);

        //----------------2、初始化布局数据-----------------

        float startY = getHeight() - getPaddingBottom() + layerPadding;

        View tempView = null;
        int tempPosition = -1;
        if (onceCompleteScrollLength == -1) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos;
            tempView = recycler.getViewForPosition(tempPosition);
            measureChildWithMargins(tempView, 0, 0);
            onceCompleteScrollLength = getDecoratedMeasurementVertical(tempView) + normalViewGap;
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        float fraction =
                (Math.abs(mVerticalOffset) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        float layerViewOffset = layerPadding * fraction;
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        float normalViewOffset = onceCompleteScrollLength * fraction;
        boolean isLayerViewOffsetSetted = false;
        boolean isNormalViewOffsetSetted = false;

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos = (int) Math.floor(Math.abs(mVerticalOffset) / onceCompleteScrollLength);
        //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = getItemCount() - 1;


        int newFocusedPosition = mFirstVisiPos + maxLayerCount - 1;
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChanged(newFocusedPosition, focusdPosition);
            }
            focusdPosition = newFocusedPosition;
        }


        //----------------3、开始布局-----------------

        for (int i = mFirstVisiPos; i <= mLastVisiPos; i++) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                View item;

                if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    item = tempView;
                } else {
                    item = recycler.getViewForPosition(i);
                }
                addView(item);
                measureChildWithMargins(item, 0, 0);

                startY -= layerPadding;
                if (!isLayerViewOffsetSetted) {
                    startY += layerViewOffset;
                    isLayerViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        trasitionListener.handleLayerView(this, item, i - mFirstVisiPos,
                                maxLayerCount, i, fraction, dy);
                    }
                }

                int l, t, r, b;
                l = getPaddingLeft();
                t = (int) (startY - getDecoratedMeasurementVertical(item));
                r = getPaddingLeft() + getDecoratedMeasurementHorizontal(item);
                b = (int) (startY);
                layoutDecoratedWithMargins(item, l, t, r, b);


            } else {//属于普通区域

                View item = recycler.getViewForPosition(i);
                addView(item);
                measureChildWithMargins(item, 0, 0);
                startY -= onceCompleteScrollLength;
                if (!isNormalViewOffsetSetted) {
                    startY -= layerViewOffset;
                    startY += normalViewOffset;
                    isNormalViewOffsetSetted = true;
                }

                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (TrasitionListener trasitionListener : trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener.handleFocusingView(this, item, i, fraction, dy);
                        } else {
                            trasitionListener.handleNormalView(this, item, i, fraction, dy);
                        }
                    }
                }

                int l, t, r, b;
                l = getPaddingLeft();
                t = (int) (startY - getDecoratedMeasurementVertical(item));
                r = getPaddingLeft() + getDecoratedMeasurementHorizontal(item);
                b = (int) (startY);
                layoutDecoratedWithMargins(item, l, t, r, b);

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startY - onceCompleteScrollLength < getPaddingTop()) {
                    mLastVisiPos = i;
                    break;
                }
            }
        }

        return dy;
    }

    @Override
    public void onAdapterChanged(@Nullable RecyclerView.Adapter oldAdapter,
                                 @Nullable RecyclerView.Adapter newAdapter) {
        resetData();
        super.onAdapterChanged(oldAdapter, newAdapter);
    }

    @Override
    public void onMeasure(@NonNull RecyclerView.Recycler recycler,
                          @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {

        int widthMode = View.MeasureSpec.getMode(widthSpec);
        int heightMode = View.MeasureSpec.getMode(heightSpec);
        if (widthMode == View.MeasureSpec.AT_MOST && (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT)) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("FocusLayoutManager-onMeasure:当滚动方向为水平时，recyclerView" +
                        "的宽度请不要使用wrap_content");
            } else {
                Log.e(TAG, "FocusLayoutManager-onMeasure:当滚动方向为水平时，recyclerView" +
                        "的宽度请不要使用wrap_content");
            }

        }
        if (heightMode == View.MeasureSpec.AT_MOST && (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM)) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("FocusLayoutManager-onMeasure:当滚动方向为垂直时，recyclerView" +
                        "的高度请不要使用wrap_content");
            } else {
                Log.e(TAG, "FocusLayoutManager-onMeasure:当滚动方向为垂直时，recyclerView" +
                        "的高度请不要使用wrap_content");
            }
        }
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    /**
     * 回收需回收的Item。
     */
    private void recycleChildren(RecyclerView.Recycler recycler) {
        List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            RecyclerView.ViewHolder holder = scrapList.get(i);
            removeAndRecycleView(holder.itemView, recycler);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                //当手指按下时，停止当前正在播放的动画
                cancelAnimator();
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                //当列表滚动停止后，判断一下自动选中是否打开
                if (isAutoSelect) {
                    //找到离目标落点最近的item索引
                    smoothScrollToPosition(findShouldSelectPosition());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 返回应当选中的position
     *
     * @return
     */
    private int findShouldSelectPosition() {
        if (onceCompleteScrollLength == -1 || mFirstVisiPos == -1) {
            return -1;
        }
        int remainder = -1;
        if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) {
            remainder = (int) (Math.abs(mHorizontalOffset) % onceCompleteScrollLength);
        } else if (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM) {
            remainder = (int) (Math.abs(mVerticalOffset) % onceCompleteScrollLength);
        }

        if (remainder >= onceCompleteScrollLength / 2.0f) { //超过一半，应当选中下一项
            if (mFirstVisiPos + 1 <= getItemCount() - 1) {
                return mFirstVisiPos + 1;
            }
        }

        return mFirstVisiPos;
    }

    /**
     * 返回移动到position所需的偏移量
     *
     * @param position
     * @return
     */
    private float getScrollToPositionOffset(int position) {
        if (focusOrientation == FOCUS_LEFT) {
            return position * onceCompleteScrollLength - Math.abs(mHorizontalOffset);
        } else if (focusOrientation == FOCUS_RIGHT) {
            return -(position * onceCompleteScrollLength - Math.abs(mHorizontalOffset));
        } else if (focusOrientation == FOCUS_TOP) {
            return position * onceCompleteScrollLength - Math.abs(mVerticalOffset);
        } else if (focusOrientation == FOCUS_BOTTOM) {
            return -(position * onceCompleteScrollLength - Math.abs(mVerticalOffset));
        }
        return 0;
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        smoothScrollToPosition(position);
    }

    @Override
    public void scrollToPosition(int position) {
        if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) {
            mHorizontalOffset += getScrollToPositionOffset(position);
            requestLayout();
        } else if (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM) {
            mVerticalOffset += getScrollToPositionOffset(position);
            requestLayout();
        }
    }

    /**
     * 平滑滚动到某个位置
     *
     * @param position 目标Item索引
     */
    public void smoothScrollToPosition(int position) {
        if (position > -1 && position < getItemCount()) {
            startValueAnimator(position);
        }
    }

    private void startValueAnimator(int position) {
        cancelAnimator();

        final float distance = getScrollToPositionOffset(position);
//        log("onceLength = " + onceCompleteScrollLength);
//        log("curoffset = " + mHorizontalOffset + ";distance = " + distance + ";endOffset = " +
// (mHorizontalOffset + distance));
//        log("currentpos = " + ((int) (Math.abs(mHorizontalOffset) / (int)
// onceCompleteScrollLength)) + ";end pos = " + ((mHorizontalOffset + distance) %
// onceCompleteScrollLength));
//        log("current fra = " + ((Math.abs(mHorizontalOffset) % onceCompleteScrollLength) /
// (onceCompleteScrollLength * 1.0f)) + ";end fra = " + (Math.abs(mHorizontalOffset + distance) %
// onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f));

        long minDuration = autoSelectMinDuration;
        long maxDuration = autoSelectMaxDuration;
        long duration;
        float distanceFraction = (Math.abs(distance) / (onceCompleteScrollLength));
        if (distance <= onceCompleteScrollLength) {
            duration = (long) (minDuration + (maxDuration - minDuration) * distanceFraction);
        } else {
            duration = (long) (maxDuration * distanceFraction);
        }

        selectAnimator = ValueAnimator.ofFloat(0.0f, distance);
        selectAnimator.setDuration(duration);
        selectAnimator.setInterpolator(new LinearInterpolator());
        final float startedOffset =
                (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) ?
                        mHorizontalOffset : mVerticalOffset;

        selectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) {
                    if (mHorizontalOffset < 0) {
                        mHorizontalOffset =
                                (long) Math.floor(startedOffset + (float) animation.getAnimatedValue());
                    } else {
                        mHorizontalOffset =
                                (long) Math.ceil(startedOffset + (float) animation.getAnimatedValue());
                    }
                    requestLayout();
                } else if (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM) {
                    if (mVerticalOffset < 0) {
                        mVerticalOffset =
                                (long) Math.floor(startedOffset + (float) animation.getAnimatedValue());
                    } else {
                        mVerticalOffset =
                                (long) Math.ceil(startedOffset + (float) animation.getAnimatedValue());
                    }
                    requestLayout();
                }

            }
        });
        selectAnimator.start();

    }

    /**
     * 获取某个childView在水平方向所占的空间，将margin考虑进去
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间,将margin考虑进去
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 重置数据
     */
    public void resetData() {
        mFirstVisiPos = 0;
        mLastVisiPos = 0;
        onceCompleteScrollLength = -1;
        mHorizontalOffset = 0;
        mVerticalOffset = 0;
        focusdPosition = -1;
        cancelAnimator();
    }

    public void cancelAnimator() {
        if (selectAnimator != null && (selectAnimator.isStarted() || selectAnimator.isRunning())) {
            selectAnimator.cancel();
        }
    }

    public int getFocusdPosition() {
        return focusdPosition;
    }

    public void setFocusdPosition(int focusdPosition, boolean anim) {
        if (focusdPosition > -1 && focusdPosition < getItemCount() && focusdPosition >= maxLayerCount - 1) {
            if (anim) {
                smoothScrollToPosition(focusdPosition - (maxLayerCount - 1));
            } else {
                scrollToPosition(focusdPosition - (maxLayerCount - 1));
            }
        }
    }


    public boolean isAutoSelect() {
        return isAutoSelect;
    }

    public void setAutoSelect(boolean autoSelect) {
        isAutoSelect = autoSelect;
    }


    public int getMaxLayerCount() {
        return maxLayerCount;
    }

    public void setMaxLayerCount(int maxLayerCount) {
        this.maxLayerCount = maxLayerCount;
        resetData();
        requestLayout();
    }

    public int getFocusOrientation() {
        return focusOrientation;
    }

    public void setFocusOrientation(@FocusOrientation int focusOrientation) {
        this.focusOrientation = focusOrientation;
        resetData();
        requestLayout();
    }

    public float getLayerPadding() {
        return layerPadding;
    }

    public void setLayerPadding(float layerPadding) {
        if (layerPadding < 0) {
            layerPadding = 0;
        }
        this.layerPadding = layerPadding;
        resetData();
        requestLayout();
    }

    public float getNormalViewGap() {
        return normalViewGap;
    }

    public void setNormalViewGap(float normalViewGap) {
        this.normalViewGap = normalViewGap;
        resetData();
        requestLayout();
    }

    public List<TrasitionListener> getTrasitionListeners() {
        return trasitionListeners;
    }

    public void addTrasitionListener(TrasitionListener trasitionListener) {
        this.trasitionListeners.add(trasitionListener);
        requestLayout();
    }

    /**
     * @param trasitionListener if null,remove all
     * @return
     */
    public boolean removeTrasitionlistener(TrasitionListener trasitionListener) {
        if (trasitionListener != null) {
            return trasitionListeners.remove(trasitionListener);
        } else {
            trasitionListeners.clear();
            return true;
        }
    }


    public OnFocusChangeListener getOnFocusChangeListener() {
        return onFocusChangeListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }


    public static class Builder {


        int maxLayerCount;
        @FocusOrientation
        private int focusOrientation;
        private float layerPadding;
        private float normalViewGap;
        private boolean isAutoSelect;
        private List<TrasitionListener> trasitionListeners;
        private OnFocusChangeListener onFocusChangeListener;
        private long autoSelectMinDuration;
        private long autoSelectMaxDuration;
        private TrasitionListener defaultTrasitionListener;


        public Builder() {
            maxLayerCount = 3;
            focusOrientation = FOCUS_LEFT;
            layerPadding = 60;
            normalViewGap = 60;
            isAutoSelect = true;
            trasitionListeners = new ArrayList<>();
            defaultTrasitionListener = new TrasitionListenerConvert(new SimpleTrasitionListener() {
            });
            trasitionListeners.add(defaultTrasitionListener);
            onFocusChangeListener = null;
            autoSelectMinDuration = 100;
            autoSelectMaxDuration = 300;
        }

        /**
         * 最大可堆叠层级
         */
        public Builder maxLayerCount(int maxLayerCount) {
            if (maxLayerCount <= 0) {
                throw new RuntimeException("maxLayerCount不能小于0");
            }
            this.maxLayerCount = maxLayerCount;
            return this;
        }


        /**
         * 堆叠的方向。
         * 滚动方向为水平时，传{@link #FOCUS_LEFT}或{@link #FOCUS_RIGHT}；
         * 滚动方向为垂直时，传{@link #FOCUS_TOP}或{@link #FOCUS_BOTTOM}。
         *
         * @param focusOrientation
         * @return
         */
        public Builder focusOrientation(@FocusOrientation int focusOrientation) {
            this.focusOrientation = focusOrientation;
            return this;
        }

        /**
         * 堆叠view之间的偏移量
         *
         * @param layerPadding
         * @return
         */
        public Builder layerPadding(float layerPadding) {
            if (layerPadding < 0) {
                layerPadding = 0;
            }
            this.layerPadding = layerPadding;
            return this;
        }

        /**
         * 普通view之间的margin
         */
        public Builder normalViewGap(float normalViewGap) {
            this.normalViewGap = normalViewGap;
            return this;
        }

        /**
         * 是否自动选中
         */
        public Builder isAutoSelect(boolean isAutoSelect) {
            this.isAutoSelect = isAutoSelect;
            return this;
        }

        public Builder autoSelectDuration(@IntRange(from = 0) long minDuration, @IntRange(from =
                0) long maxDuration) {
            if (minDuration < 0 || maxDuration < 0 || maxDuration < minDuration) {
                throw new RuntimeException("autoSelectDuration入参不合法");
            }
            this.autoSelectMinDuration = minDuration;
            this.autoSelectMaxDuration = maxDuration;
            return this;
        }

        /**
         * 高级定制 添加滚动过程中view的变换监听接口
         *
         * @param trasitionListener
         * @return
         */
        public Builder addTrasitionListener(TrasitionListener trasitionListener) {
            if (trasitionListener != null) {
                this.trasitionListeners.add(trasitionListener);
            }
            return this;
        }

        /**
         * 简化版 滚动过程中view的变换监听接口。实际会被转换为{@link TrasitionListener}
         *
         * @param simpleTrasitionListener if null,remove current
         * @return
         */
        public Builder setSimpleTrasitionListener(SimpleTrasitionListener simpleTrasitionListener) {
            this.trasitionListeners.remove(defaultTrasitionListener);
            defaultTrasitionListener = null;
            if (simpleTrasitionListener != null) {
                defaultTrasitionListener = new TrasitionListenerConvert(simpleTrasitionListener);
                this.trasitionListeners.add(defaultTrasitionListener);
            }
            return this;
        }

        public Builder setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
            this.onFocusChangeListener = onFocusChangeListener;
            return this;
        }

        public FocusLayoutManager build() {
            return new FocusLayoutManager(this);
        }
    }


    /**
     * 滚动过程中view的变换监听接口。属于高级定制，暴露了很多关键布局数据。若定制要求不高，考虑使用{@link SimpleTrasitionListener}
     */
    public interface TrasitionListener {

        /**
         * 处理在堆叠里的view。
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param viewLayer          当前层级，0表示底层，maxLayerCount-1表示顶层
         * @param maxLayerCount      最大层级
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         *                           ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        void handleLayerView(FocusLayoutManager focusLayoutManager, View view, int viewLayer,
                             int maxLayerCount, int position, float fraction, float offset);

        /**
         * 处理正聚焦的那个View（即正处在从普通位置滚向聚焦位置时的那个view,即堆叠顶层view）
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         *                           ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        void handleFocusingView(FocusLayoutManager focusLayoutManager, View view, int position,
                                float fraction, float offset);

        /**
         * 处理不在堆叠里的普通view（正在聚焦的那个view除外）
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         *                           ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        void handleNormalView(FocusLayoutManager focusLayoutManager, View view, int position,
                              float fraction, float offset);

    }

    /**
     * 简化版  滚动过程中view的变换监听接口。
     */
    public static abstract class SimpleTrasitionListener {

        /**
         * 返回堆叠view最大透明度
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getLayerViewMaxAlpha(int maxLayerCount) {
            return getFocusingViewMaxAlpha();
        }

        /**
         * 返回堆叠view最小透明度
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getLayerViewMinAlpha(int maxLayerCount) {
            return 0;
        }


        /**
         * 返回堆叠view最大缩放比例
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        public float getLayerViewMaxScale(int maxLayerCount) {
            return getFocusingViewMaxScale();
        }

        /**
         * 返回堆叠view最小缩放比例
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        public float getLayerViewMinScale(int maxLayerCount) {
            return 0.7f;
        }


        /**
         * 返回一个百分比值，相对于"一次完整的聚焦滑动"期间，在该百分比值内view就完成缩放、透明度的渐变变化。
         * 例：若返回值为1，说明在"一次完整的聚焦滑动"期间view将线性均匀完成缩放、透明度变化；
         * 例：若返回值为0.5，说明在"一次完整的聚焦滑动"的一半路程内（具体从什么时候开始变由实际逻辑自己决定），view将完成的缩放、透明度变化
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getLayerChangeRangePercent() {
            return 0.35f;
        }

        /**
         * 返回聚焦view的最大透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getFocusingViewMaxAlpha() {
            return 1f;
        }

        /**
         * 返回聚焦view的最小透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getFocusingViewMinAlpha() {
            return getNormalViewAlpha();
        }

        /**
         * 返回聚焦view的最大缩放比例
         *
         * @return
         */
        public float getFocusingViewMaxScale() {
            return 1.2f;
        }

        /**
         * 返回聚焦view的最小缩放比例
         *
         * @return
         */
        public float getFocusingViewMinScale() {
            return getNormalViewScale();
        }

        /**
         * 返回值意义参考{@link #getLayerChangeRangePercent()}
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getFocusingViewChangeRangePercent() {
            return 0.5f;
        }

        /**
         * 返回普通view的透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getNormalViewAlpha() {
            return 1.0f;
        }

        /**
         * 返回普通view的缩放比例
         *
         * @return
         */
        public float getNormalViewScale() {
            return 1.0f;
        }

    }

    /**
     * 将SimpleTrasitionListener转换成实际的TrasitionListener的转换器
     */
    public static class TrasitionListenerConvert implements TrasitionListener {
        SimpleTrasitionListener stl;

        public TrasitionListenerConvert(SimpleTrasitionListener simpleTrasitionListener) {
            stl = simpleTrasitionListener;
        }

        @Override
        public void handleLayerView(FocusLayoutManager focusLayoutManager, View view,
                                    int viewLayer, int maxLayerCount, int position,
                                    float fraction, float offset) {
            /**
             * 期望效果：从0%开始到{@link SimpleTrasitionListener#getLayerChangeRangePercent()} 期间
             * view均匀完成渐变，之后一直保持不变
             */
            //转换为真实的渐变变化百分比
            float realFraction;
            if (fraction <= stl.getLayerChangeRangePercent()) {
                realFraction = fraction / stl.getLayerChangeRangePercent();
            } else {
                realFraction = 1.0f;
            }

            float minScale = stl.getLayerViewMinScale(maxLayerCount);
            float maxScale = stl.getLayerViewMaxScale(maxLayerCount);
            float scaleDelta = maxScale - minScale; //总缩放差
            float currentLayerMaxScale =
                    minScale + scaleDelta * (viewLayer + 1) / (maxLayerCount * 1.0f);
            float currentLayerMinScale = minScale + scaleDelta * viewLayer / (maxLayerCount * 1.0f);
            float realScale =
                    currentLayerMaxScale - (currentLayerMaxScale - currentLayerMinScale) * realFraction;

            float minAlpha = stl.getLayerViewMinAlpha(maxLayerCount);
            float maxAlpha = stl.getLayerViewMaxAlpha(maxLayerCount);
            float alphaDelta = maxAlpha - minAlpha; //总透明度差
            float currentLayerMaxAlpha =
                    minAlpha + alphaDelta * (viewLayer + 1) / (maxLayerCount * 1.0f);
            float currentLayerMinAlpha = minAlpha + alphaDelta * viewLayer / (maxLayerCount * 1.0f);
            float realAlpha =
                    currentLayerMaxAlpha - (currentLayerMaxAlpha - currentLayerMinAlpha) * realFraction;

//            log("layer =" + viewLayer + ";alpha = " + realAlpha + ";fraction = " + fraction);
            view.setScaleX(realScale);
            view.setScaleY(realScale);
            view.setAlpha(realAlpha);

        }

        @Override
        public void handleFocusingView(FocusLayoutManager focusLayoutManager, View view,
                                       int position, float fraction, float offset) {
            /**
             * 期望效果：从0%开始到{@link SimpleTrasitionListener#getFocusingViewChangeRangePercent()} 期间
             * view均匀完成渐变，之后一直保持不变
             */
            //转换为真实的渐变百分比
            float realFraction;
            if (fraction <= stl.getFocusingViewChangeRangePercent()) {
                realFraction = fraction / stl.getFocusingViewChangeRangePercent();
            } else {
                realFraction = 1.0f;
            }

            float realScale =
                    stl.getFocusingViewMinScale() + (stl.getFocusingViewMaxScale() - stl.getFocusingViewMinScale()) * realFraction;
            float realAlpha =
                    stl.getFocusingViewMinAlpha() + (stl.getFocusingViewMaxAlpha() - stl.getFocusingViewMinAlpha()) * realFraction;

            view.setScaleX(realScale);
            view.setScaleY(realScale);
            view.setAlpha(realAlpha);

        }

        @Override
        public void handleNormalView(FocusLayoutManager focusLayoutManager, View view,
                                     int position, float fraction, float offset) {
            /**
             * 期望效果：直接完成变换
             */

            view.setScaleX(stl.getNormalViewScale());
            view.setScaleY(stl.getNormalViewScale());
            view.setAlpha(stl.getNormalViewAlpha());
        }
    }


    public interface OnFocusChangeListener {

        /**
         * @param focusdPosition
         * @param lastFocusedPosition 可能为-1
         */
        void onFocusChanged(int focusdPosition, int lastFocusedPosition);
    }

    public float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        boolean isDebug = true;
        if (isDebug) {
            Log.d(tag, msg);
        }
    }


}

