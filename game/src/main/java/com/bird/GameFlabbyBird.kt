package com.bird

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.bird.internal.*
import com.bird.internal.dp
import com.engineer.android.game.R
import java.util.*

class GameFlabbyBird @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {

    var customBg = true

    private val mHolder: SurfaceHolder = holder

    private var mCanvas: Canvas? = null

    private var t: Thread? = null
    private var isRunning = false


    private val mPaint: Paint

    /**
     * 当前View的尺寸
     */
    private var mWidth = 0
    private var mHeight = 0
    private val mGamePanelRect = RectF()

    private var mBg: Bitmap? = null

    private var mBird: Bird? = null
    private var mBirdBitmap: Bitmap? = null


    private var mFloor: Floor? = null
    private var mFloorBg: Bitmap? = null

    private var mPipeTop: Bitmap? = null
    private var mPipeBottom: Bitmap? = null
    private var mPipeRect: RectF? = null
    private val mPipeWidth: Int
    private val mPipes: MutableList<Pipe> = ArrayList()

    /**
     * 分数
     */
    private val mNums = intArrayOf(
        R.drawable.n0, R.drawable.n1,
        R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
        R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9
    )
    private lateinit var mNumBitmap: Array<Bitmap?>
    private var mGrade = 0
    private var mRemovedPipe = 0
    private var mSingleGradeWidth = 0
    private var mSingleGradeHeight = 0
    private var mSingleNumRectF: RectF? = null


    private val mSpeed = 2.dp

    /**
     * 记录游戏的状态
     */
    private var mStatus = GameStatus.WAITTING

    /**
     * 将上升的距离转化为px；这里多存储一个变量，变量在run中计算
     */
    private val mBirdUpDis = TOUCH_UP_SIZE.dp
    private var mTmpBirdDis = 0

    /**
     * 鸟自动下落的距离
     */
    private val mAutoDownSpeed = 2.dp

    /**
     * 两个管道间距离
     */
    private val PIPE_DIS_BETWEEN_TWO = 200.dp

    /**
     * 记录移动的距离，达到 PIPE_DIS_BETWEEN_TWO 则生成一个管道
     */
    private var mTmpMoveDistance = 0

    /**
     * 记录需要移除的管道
     */
    private val mNeedRemovePipe: MutableList<Pipe> = ArrayList()

    /**
     * 处理一些逻辑上的计算
     */
    private fun logic() {
        when (mStatus) {
            GameStatus.RUNNING -> {
                mGrade = 0
                // 更新我们地板绘制的x坐标，地板移动
                mFloor!!.x = mFloor!!.x - mSpeed
                logicPipe()

                // 默认下落，点击时瞬间上升
                mTmpBirdDis += mAutoDownSpeed
                mBird!!.y = mBird!!.y + mTmpBirdDis

                // 计算分数
                mGrade += mRemovedPipe
                for (pipe in mPipes) {
                    if (pipe.x + mPipeWidth < mBird!!.x) {
                        mGrade++
                    }
                }
                checkGameOver()
            }
            GameStatus.STOP ->                 // 如果鸟还在空中，先让它掉下来
                if (mBird!!.y < mFloor!!.y - mBird!!.width) {
                    mTmpBirdDis += mAutoDownSpeed
                    mBird!!.y = mBird!!.y + mTmpBirdDis
                } else {
                    mStatus = GameStatus.WAITTING
                    mGrade = 0
                    initPos()
                }
            else -> {
            }
        }
    }

    /**
     * 重置鸟的位置等数据
     */
    private fun initPos() {
        mPipes.clear()
        //立即增加一个
        mPipes.add(
            Pipe(
                width, height, mPipeTop!!,
                mPipeBottom!!
            )
        )
        mNeedRemovePipe.clear()
        // 重置鸟的位置
        // mBird.setY(mHeight * 2 / 3);
        mBird!!.resetHeight()
        // 重置下落速度
        mTmpBirdDis = 0
        mTmpMoveDistance = 0
        mRemovedPipe = 0
    }

    private fun checkGameOver() {

        // 如果触碰地板，gg
        if (mBird!!.y > mFloor!!.y - mBird!!.height) {
            mStatus = GameStatus.STOP
        }
        // 如果撞到管道
        for (wall in mPipes) {
            // 已经穿过的
            if (wall.x + mPipeWidth < mBird!!.x) {
                continue
            }
            if (wall.touchBird(mBird!!)) {
                mStatus = GameStatus.STOP
                break
            }
        }
    }

    private fun logicPipe() {
        // 管道移动
        for (pipe in mPipes) {
            if (pipe.x < -mPipeWidth) {
                mNeedRemovePipe.add(pipe)
                mRemovedPipe++
                continue
            }
            pipe.x = pipe.x - mSpeed
        }
        // 移除管道
        mPipes.removeAll(mNeedRemovePipe)
        mNeedRemovePipe.clear()

        // Log.e("TAG", "现存管道数量：" + mPipes.size());

        // 管道
        mTmpMoveDistance += mSpeed
        // 生成一个管道
        if (mTmpMoveDistance >= PIPE_DIS_BETWEEN_TWO) {
            val pipe = Pipe(
                width, height,
                mPipeTop!!, mPipeBottom!!
            )
            mPipes.add(pipe)
            mTmpMoveDistance = 0
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            when (mStatus) {
                GameStatus.WAITTING -> mStatus = GameStatus.RUNNING
                GameStatus.RUNNING -> mTmpBirdDis = mBirdUpDis
            }
        }
        return true
    }

    /**
     * 初始化图片
     */
    private fun initBitmaps() {
        mBg = loadImageByResId(R.drawable.bg1)
        mBirdBitmap = loadImageByResId(R.drawable.b1)
        mFloorBg = loadImageByResId(R.drawable.floor_bg2)
        mPipeTop = loadImageByResId(R.drawable.g2)
        mPipeBottom = loadImageByResId(R.drawable.g1)
        mNumBitmap = arrayOfNulls(mNums.size)
        for (i in mNumBitmap.indices) {
            mNumBitmap[i] = loadImageByResId(mNums[i])
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.e("TAG", "surfaceCreated")

        // 开启线程
        isRunning = true
        t = Thread(this)
        t!!.start()
    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int,
        height: Int
    ) {
        Log.e("TAG", "surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.e("TAG", "surfaceDestroyed")
        // 通知关闭线程
        isRunning = false
    }

    override fun run() {
        while (isRunning) {
            val start = System.currentTimeMillis()
            logic()
            draw()
            val end = System.currentTimeMillis()
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start))
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun draw() {
        try {
            // 获得canvas
            mCanvas = mHolder.lockCanvas()
            if (mCanvas != null) {
                drawBg()
                drawBird()
                drawPipes()
                drawFloor()
                drawGrades()
            }
        } catch (e: Exception) {
        } finally {
            if (mCanvas != null) mHolder.unlockCanvasAndPost(mCanvas)
        }
    }

    private fun drawFloor() {
        mFloor!!.draw(mCanvas!!, mPaint)
    }

    /**
     * 绘制背景
     */
    private fun drawBg() {
        if (customBg) {
            mCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        } else {
            mCanvas!!.drawBitmap(mBg!!, null, mGamePanelRect, null)
        }
    }

    private fun drawBird() {
        mBird!!.draw(mCanvas!!)
    }

    /**
     * 绘制管道
     */
    private fun drawPipes() {
        for (pipe in mPipes) {
            pipe.x = pipe.x - mSpeed
            pipe.draw(mCanvas!!, mPipeRect!!)
        }
    }

    /**
     * 初始化尺寸相关
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mGamePanelRect[0f, 0f, w.toFloat()] = h.toFloat()

        // 初始化mBird
        mBird = Bird(mWidth, mHeight, mBirdBitmap!!)
        // 初始化地板
        mFloor = Floor(mWidth, mHeight, mFloorBg)
        // 初始化管道范围
        mPipeRect = RectF(0f, 0f, mPipeWidth.toFloat(), mHeight.toFloat())
        val pipe = Pipe(w, h, mPipeTop!!, mPipeBottom!!)
        mPipes.add(pipe)

        // 初始化分数
        mSingleGradeHeight = (h * RADIO_SINGLE_NUM_HEIGHT).toInt()
        mSingleGradeWidth = (mSingleGradeHeight * 1.0f
                / mNumBitmap[0]!!.height * mNumBitmap[0]!!.width).toInt()
        mSingleNumRectF = RectF(0f, 0f, mSingleGradeWidth.toFloat(), mSingleGradeHeight.toFloat())
    }

    /**
     * 绘制分数
     */
    private fun drawGrades() {
        val grade = mGrade.toString()
        mCanvas!!.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), null)
        mCanvas!!.translate(
            (mWidth / 2 - grade.length * mSingleGradeWidth / 2).toFloat(),
            1f / 8 * mHeight
        )
        // draw single num one by one
        for (i in grade.indices) {
            val numStr = grade.substring(i, i + 1)
            val num = numStr.toInt()
            mCanvas!!.drawBitmap(mNumBitmap[num]!!, null, mSingleNumRectF!!, null)
            mCanvas!!.translate(mSingleGradeWidth.toFloat(), 0f)
        }
        mCanvas!!.restore()
    }

    /**
     * 根据resId加载图片
     *
     * @param resId
     * @return
     */
    private fun loadImageByResId(resId: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, resId)
    }

    companion object {
        /**
         * 管道的宽度 60dp
         */
        private const val PIPE_WIDTH = 60
        private const val RADIO_SINGLE_NUM_HEIGHT = 1 / 15f

        /**
         * 触摸上升的距离，因为是上升，所以为负值
         */
        private const val TOUCH_UP_SIZE = -16
    }

    init {
        mHolder.addCallback(this)
        setZOrderOnTop(true) // 设置画布 背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT)

        // 设置可获得焦点
        isFocusable = true
        isFocusableInTouchMode = true
        // 设置常亮
        this.keepScreenOn = true
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        initBitmaps()

        // 初始化速度
        mPipeWidth = PIPE_WIDTH.dp
    }

}