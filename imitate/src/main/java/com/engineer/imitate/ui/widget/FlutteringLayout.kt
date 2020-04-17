package com.engineer.imitate.ui.widget

import android.animation.*
import android.content.Context
import android.graphics.Interpolator
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import com.engineer.imitate.R
import java.util.*

/**
 * @author zhuyongqing @ Zhihu Inc.
 * @since 04-16-2020
 */
class FlutteringLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    /**
     * 桃心
     */
    var heartRes = intArrayOf(
        R.drawable.resource_heart7
    )

    /**
     * 插补器
     */
    var interpolators =
        arrayOf<Interpolator>()
    private var mWidth = 0
    private var mHeight = 0
    private var mRandom: Random? = null

    /**
     * 进入动画持续时间
     */
    var enterDuration = 300

    /**
     * 动画持续时间
     */
    var duration = 3000

    /**
     * 桃心的缩放比例
     */
    var scale = 1.0f
    var heartLayoutParams: LayoutParams? = null

    /**
     * 是否是相同大小（如果是则只计算一次）
     */
    var isSameSize = true
    private var mStartPointF: PointF? = null
    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        mRandom = Random()
        mStartPointF = PointF()
        heartLayoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        heartLayoutParams!!.addRule(ALIGN_PARENT_BOTTOM, TRUE)
        heartLayoutParams!!.addRule(CENTER_HORIZONTAL, TRUE)

//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlutteringLayout);
//        mEnterDuration = a.getInt(R.styleable.FlutteringLayout_enter_duration, mEnterDuration);
//        mDuration = a.getInt(R.styleable.FlutteringLayout_duration, mDuration);
//        mScale = a.getFloat(R.styleable.FlutteringLayout_scale, mScale);
//        mIsSameSize = a.getBoolean(R.styleable.FlutteringLayout_same_size, mIsSameSize);

//        a.recycle();
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //测量后的宽高
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    /**
     * 添加桃心
     */
    fun addHeart() {
        val iv = getHeartView(randomHeartResource())
        addView(iv)
        updateStartPointF(iv)
        val animator = getAnimator(iv)
        animator.addListener(EndAnimatorListener(iv))
        animator.start()
    }

    /**
     * 获取一个桃心
     *
     * @param resId
     * @return
     */
    private fun getHeartView(resId: Int): ImageView {
        val iv = ImageView(context)
        iv.layoutParams = heartLayoutParams
        iv.setImageResource(resId)
        return iv
    }

    /**
     * 设置布局配置
     *
     * @param params
     */
    fun setLayoutParams(params: LayoutParams?) {
        heartLayoutParams = params
    }

    /**
     * 飘心入场动画
     *
     * @param target
     * @return
     */
    private fun getEnterAnimator(target: View): AnimatorSet {
        val scaleX = ObjectAnimator.ofFloat(
            target,
            View.SCALE_X,
            0.1f,
            scale
        )
        val scaleY = ObjectAnimator.ofFloat(
            target,
            View.SCALE_Y,
            0.1f,
            scale
        )
        val alpha =
            ObjectAnimator.ofFloat(target, View.ALPHA, 0.1f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = enterDuration.toLong()
        return animatorSet
    }

    /**
     * 贝塞尔曲线动画
     *
     * @param target
     * @return
     */
    private fun getBezierCurveAnimator(target: View): ValueAnimator {

        //贝塞尔曲线中间的两个点
        val pointf1 = randomPointF(3.0f)
        val pointf2 = randomPointF(1.5f)

        // 传入  起点 和 终点
        val valueAnimator =
            ValueAnimator.ofObject(
                TypeEvaluator<PointF> { fraction, startValue, endValue -> //三次方贝塞尔曲线 逻辑 通过四个点确定一条三次方贝塞尔曲线
                    val timeLeft = 1.0f - fraction
                    val pointF = PointF()
                    pointF.x = (startValue.x * Math.pow(
                        timeLeft.toDouble(),
                        3.0
                    ) + 3 * pointf1.x * fraction * Math.pow(
                        timeLeft.toDouble(),
                        2.0
                    ) + 3 * pointf2.x * Math.pow(
                        fraction.toDouble(),
                        2.0
                    ) * timeLeft + endValue.x * Math.pow(
                        fraction.toDouble(),
                        3.0
                    )).toFloat()
                    pointF.y = (startValue.y * Math.pow(
                        timeLeft.toDouble(),
                        3.0
                    ) + 3 * pointf1.y * fraction * Math.pow(
                        timeLeft.toDouble(),
                        2.0
                    ) + 3 * pointf2.y * Math.pow(
                        fraction.toDouble(),
                        2.0
                    ) * timeLeft + endValue.y * Math.pow(
                        fraction.toDouble(),
                        3.0
                    )).toFloat()
                    pointF
                }, //起点和终点
                mStartPointF, PointF(mRandom!!.nextInt(mWidth).toFloat(), 0F)
            )

//        valueAnimator.setInterpolator(randomInterpolator());
        valueAnimator.addUpdateListener { animation ->
            val pointF = animation.animatedValue as PointF
            //更新target的坐标
            target.x = pointF.x
            target.y = pointF.y
            //透明度 从不透明到完全透明
            target.alpha = 1.0f - animation.animatedFraction * animation.animatedFraction
        }
        valueAnimator.duration = duration.toLong()
        return valueAnimator
    }

    private fun getAnimator(target: View): Animator {
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(getEnterAnimator(target), getBezierCurveAnimator(target))
        return animatorSet
    }

    /**
     * 测量
     *
     * @param target
     */
    private fun makeMeasureSpec(target: View) {
        val spec = MeasureSpec.makeMeasureSpec(
            0,
            MeasureSpec.UNSPECIFIED
        )
        target.measure(spec, spec)
    }

    /**
     * 起点
     *
     * @param target
     * @return
     */
    private fun updateStartPointF(target: View) {
        if (mStartPointF!!.x == 0f || mStartPointF!!.y == 0f || !isSameSize) {
            makeMeasureSpec(target)
            val width = target.measuredWidth
            val height = target.measuredHeight
            mStartPointF!!.x = (mWidth + paddingLeft - paddingRight - width) / 2.toFloat()
            mStartPointF!!.y = mHeight + paddingTop - paddingBottom - height.toFloat()
        }
    }

    /**
     * 随机贝塞尔曲线中间的点
     *
     * @param scale
     * @return
     */
    private fun randomPointF(scale: Float): PointF {
        val pointF = PointF()
        pointF.x = mRandom!!.nextInt(mWidth).toFloat()
        pointF.y = mRandom!!.nextInt(mHeight) / scale
        return pointF
    }

    /**
     * 随机一个插补器
     *
     * @return
     */
    private fun randomInterpolator(): Interpolator {
        return interpolators[mRandom!!.nextInt(interpolators.size)]
    }

    /**
     * 随机一个桃心
     *
     * @return
     */
    private fun randomHeartResource(): Int {
        return heartRes[mRandom!!.nextInt(heartRes.size)]
    }

    private inner class EndAnimatorListener(private val target: View) :
        AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            //动画结束 移除target
            removeView(target)
        }

    }

    init {
        init(context, attrs)
    }
}