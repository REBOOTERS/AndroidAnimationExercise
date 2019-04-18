package com.engineer.imitate.ui.widget.split

/**
 *
 * @author: zhuyongging
 * @since: 2019-03-23
 */
class Ball : Cloneable {
    var aX: Float = 0.toFloat()//加速度
    var aY: Float = 0.toFloat()//加速度Y
    var vX: Float = 0.toFloat()//速度X
    var vY: Float = 0.toFloat()//速度Y
    var x: Float = 0.toFloat()//点位X
    var y: Float = 0.toFloat()//点位Y
    var color: Int = 0//颜色
    var r: Float = 0.toFloat()//半径
    var born: Long = 0//诞生时间

    public override fun clone(): Any {
        var clone: Ball? = null
        try {
            clone = super.clone() as Ball
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return clone!!
    }
}