package com.engineer.flex.callback;

/**
 * Created by gavin
 * date 2018/6/12
 * 下拉监听
 */
public interface OnPullListener {

    /**
     * 下拉
     * @param offset
     */
    void onPull(int offset);

    /**
     * 松开
     */
    void onRelease();
}
