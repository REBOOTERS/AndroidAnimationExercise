package com.engineer.imitate.interfaces

import com.xw.repo.BubbleSeekBar

/**
 *
 * @FileName:com.engineer.imitate.interfaces.SimpleProgressChangeListener.java
 * @author: zhuyongging
 * @date: 2018-07-15 12:04
 * @version V1.0
 */

abstract class SimpleProgressChangeListener:BubbleSeekBar.OnProgressChangedListener {

    override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {

    }

    override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
    }

    override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
    }

}