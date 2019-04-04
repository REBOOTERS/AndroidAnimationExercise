package com.engineer.imitate.widget.split

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import androidx.annotation.Nullable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.engineer.imitate.R


/**
 *
 * @author: zhuyongging
 * @since: 2019-03-23
 */
class BitmapSplitView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : View(context, attrs) {
    private val mCoo = PointF(800f, 400f)//坐标系


    private var mPaint: Paint? = null//主画笔
    private var mPath: Path? = null//主路径
    private var mBitmap: Bitmap? = null
    private val mColArr: Array<IntArray>? = null

    private val d = 3//复刻的像素边长
    private val mBalls = ArrayList<Ball>()//粒子集合
    private var mAnimator: ValueAnimator? = null//时间流
    private var mRunTime: Long = 0//粒子运动时刻
    private val isOK: Boolean = false//结束的flag
    private val curBitmapIndex = 0//当前图片索引
    private val mBitmaps: Array<Bitmap>? = null//图片数组

    init {
        init()//初始化
    }

    private fun init() {
        //初始化主画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.setColor(Color.BLUE)
        mPaint!!.setStrokeWidth(5f)
        //初始化主路径
        mPath = Path()


        //初始化时间流ValueAnimator
        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator!!.repeatCount = -1
        mAnimator!!.duration = 2000
        mAnimator!!.interpolator = LinearInterpolator()
        mAnimator!!.addUpdateListener { animation ->
            updateBall()//更新小球位置
            invalidate()
        }


        //加载图片数组
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star)

        initBall(mBitmap!!)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(mCoo.x, mCoo.y)
        //        drawBitmapWithStar(canvas);

        for (ball in mBalls) {
            mPaint!!.setColor(ball.color)
            //            canvas.drawRect(
            //                    ball.x - d / 2, ball.y - d / 2, ball.x + d / 2, ball.y + d / 2, mPaint);

            canvas.drawCircle(ball.x, ball.y, d / 2f, mPaint)
        }
        canvas.restore()
        //        HelpDraw.draw(canvas, mGridPicture, mCooPicture);
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mRunTime = System.currentTimeMillis()//记录点击时间
                mAnimator!!.start()
            }
            MotionEvent.ACTION_UP -> {
            }
        }//                mAnimator.pause();
        return true
    }


    /**
     * 更新小球
     */
    private fun updateBall() {
        for (i in mBalls.indices) {
            val ball = mBalls.get(i)
            if (System.currentTimeMillis() - mRunTime > 2000) {
                mBalls.removeAt(i)
            }
            ball.x += ball.vX
            ball.y += ball.vY
            ball.vY += ball.aY
            ball.vX += ball.aX
        }
    }

    /**
     * 根像素初始化粒子
     *
     * @param bitmap
     * @return
     */
    private fun initBall(bitmap: Bitmap) {
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val ball = Ball()//产生粒子---每个粒子拥有随机的一些属性信息
                ball.x = (i * d + d / 2).toFloat()
                ball.y = (j * d + d / 2).toFloat()
                ball.vX = (Math.pow(-1.0, Math.ceil(Math.random() * 1001)) * 40.0 * Math.random()).toFloat()
                ball.vY = 0f
                ball.aX = 0.98f
                ball.color = bitmap.getPixel(i, j)
                ball.born = System.currentTimeMillis()
                mBalls.add(ball)
            }
        }
    }

}