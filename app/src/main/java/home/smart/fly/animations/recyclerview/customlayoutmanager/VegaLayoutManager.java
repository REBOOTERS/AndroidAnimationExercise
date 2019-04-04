package home.smart.fly.animations.recyclerview.customlayoutmanager;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;


public class VegaLayoutManager extends RecyclerView.LayoutManager {

    private int mDecoratedMeasuredWidth;
    private int mDecoratedMeasuredHeight;
    private int scroll = 0;
    private SparseArray<Rect> locationRects = new SparseArray<>();
    private SparseBooleanArray attachedItems = new SparseBooleanArray();
//    private SparseArray<Boolean> attachedItems = new SparseArray<>();
    private boolean needSnap = false;
    private int lastDy = 0;
    private int maxScroll = -1;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int itemCount = getItemCount();
        if (itemCount <= 0 || state.isPreLayout()) {
            return;
        }

        if (getChildCount() == 0) {
            // 通过第一个itemView，获取一些中间变量
            View itemView = recycler.getViewForPosition(0);
            addView(itemView);
            measureChildWithMargins(itemView, 0, 0);
            mDecoratedMeasuredWidth = getDecoratedMeasuredWidth(itemView);
            mDecoratedMeasuredHeight = getDecoratedMeasuredHeight(itemView);
        }

        int tempPosition = getPaddingTop();
        for (int i = 0; i < itemCount; i++) {
            Rect rect = new Rect();
            rect.left = getPaddingLeft();
            rect.top = tempPosition;
            rect.right = mDecoratedMeasuredWidth - getPaddingRight();
            rect.bottom = rect.top + mDecoratedMeasuredHeight;
            locationRects.put(i, rect);
            attachedItems.put(i, false);

            tempPosition = tempPosition + mDecoratedMeasuredHeight;
        }

        // 得到中间变量后，第一个View先回收放到缓存，后面会再次统一layout
        detachAndScrapAttachedViews(recycler);
        layoutItemsOnCreate(recycler);
        computeMaxScroll();
    }

    /**
     * 计算可滑动的最大值
     */
    private void computeMaxScroll() {
        maxScroll = locationRects.get(locationRects.size() - 1).bottom - getHeight();
        int childCount = getChildCount();
        int screenFilledHeight = 0;
        for (int i = childCount; i >= 0; i--) {
            Rect rect = locationRects.get(i);
            screenFilledHeight = screenFilledHeight + (rect.bottom - rect.top);
            if (screenFilledHeight > getHeight()) {
                int extraSnapHeight = getHeight() - (screenFilledHeight - (rect.bottom - rect.top));
                maxScroll = maxScroll + extraSnapHeight;
                break;
            }
        }
    }

    /**
     * 初始化的时候，layout子View
     */
    private void layoutItemsOnCreate(RecyclerView.Recycler recycler) {
        int itemCount = getItemCount();

        for (int i = 0; i < itemCount; i++) {
            View childView = recycler.getViewForPosition(i);
            addView(childView);
            measureChildWithMargins(childView, 0, 0);
            layoutItem(childView, locationRects.get(i));
            attachedItems.put(i, true);
            childView.setPivotY(0);
            childView.setPivotX(childView.getMeasuredWidth() / 2);

            if (locationRects.get(i).top > getHeight()) {
                break;
            }
        }
    }


    /**
     * 初始化的时候，layout子View
     */
    private void layoutItemsOnScroll(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        int childCount = getChildCount();
        if (state.isPreLayout() || childCount == 0) {
            return;
        }

        // 1. 已经在屏幕上显示的child
        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            int position = getPosition(child);
            if (!Rect.intersects(displayRect, locationRects.get(position))) {
                // 回收滑出屏幕的View
                removeAndRecycleView(child, recycler);
                attachedItems.put(position, false);
            } else {
                // Item还在显示区域内，更新滑动后Item的位置
                layoutItem(child, locationRects.get(position)); //更新Item位置
            }
        }

        // 2. 复用View添加
        for (int i = 0; i < itemCount; i++) {
            if (Rect.intersects(displayRect, locationRects.get(i)) &&
                    !attachedItems.get(i)) {
                // 重新加载可见范围内的Item
                View scrap = recycler.getViewForPosition(i);
                measureChildWithMargins(scrap, 0, 0);
                scrap.setPivotY(0);
                scrap.setPivotX(scrap.getMeasuredWidth() / 2);
                if (dy > 0) {
                    addView(scrap);
                } else {
                    addView(scrap, 0);
                }
                // 将这个Item布局出来
                layoutItem(scrap, locationRects.get(i));
                attachedItems.put(i, true);
            }
        }
    }

    private void layoutItem(View child, Rect rect) {
        int topDistance = scroll - rect.top;
        int layoutTop, layoutBottom;
        if (topDistance < mDecoratedMeasuredHeight && topDistance >= 0) {
            float rate1 = (float) topDistance / mDecoratedMeasuredHeight;
            float rate2 = 1 - rate1 * rate1 / 3;
            float rate3 = 1 - rate1 * rate1;
            child.setScaleX(rate2);
            child.setScaleY(rate2);
            child.setAlpha(rate3);

            layoutTop = 0;
            layoutBottom = mDecoratedMeasuredHeight;
        } else {
            child.setScaleX(1);
            child.setScaleY(1);
            child.setAlpha(1);

            layoutTop = rect.top - scroll;
            layoutBottom = rect.bottom - scroll;
        }
        layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || dy == 0) {
            return 0;
        }
        int travel = dy;
        if (dy + scroll < 0) {
            travel = -scroll;
        } else if (dy + scroll > maxScroll) {
            travel = maxScroll - scroll;
        }
        scroll += travel; //累计偏移量
        lastDy = dy;
        layoutItemsOnScroll(recycler, state, dy);
        return travel;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        new StartSnapHelper().attachToRecyclerView(view);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            needSnap = true;
        }
        super.onScrollStateChanged(state);
    }

    public int getSnapHeight() {
        if (!needSnap) {
            return 0;
        }
        needSnap = false;

        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            Rect itemRect = locationRects.get(i);
            if (displayRect.intersect(itemRect)) {

                if (lastDy > 0) {
                    // scroll变大，属于列表往下走，往下找下一个为snapView
                    if (i < itemCount - 1) {
                        Rect nextRect = locationRects.get(i + 1);
                        return nextRect.top - displayRect.top;
                    }
                }
                return itemRect.top - displayRect.top;
            }
        }
        return 0;
    }

    public View findSnapView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }
}
