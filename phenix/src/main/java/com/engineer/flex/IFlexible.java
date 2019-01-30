package com.engineer.flex;

public interface IFlexible {

    /**
     * 是否准备下拉
     *
     * @return
     */
    boolean isReady();

    /**
     * 头部ready
     *
     * @return
     */
    boolean isHeaderReady();

    /**
     * 下拉Header
     *
     * @param offsetY
     */
    void changeHeader(int offsetY);

    /**
     * 重置Header
     */
    void resetHeader();

    /**
     * 刷新
     */
    void changeRefreshView(int offsetY);

    /**
     * 松开时的刷新控件
     * 重置 或 触发刷新
     *
     * @param offsetY
     */
    void changeRefreshViewOnActionUp(int offsetY);

    /**
     * 刷新完成
     */
    void onRefreshComplete();

    /**
     * 是否正在刷新
     */
    boolean isRefreshing();
}
