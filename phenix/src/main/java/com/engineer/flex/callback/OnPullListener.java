package com.engineer.flex.callback;


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
