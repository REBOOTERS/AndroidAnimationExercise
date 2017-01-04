package home.smart.fly.animationdemo.customview.swipeanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import home.smart.fly.animationdemo.R;


public class RadarScanView extends View {
    //默认扫描图标
    private Bitmap mIconScanBitmap;
    //扫描时图标
    private Bitmap mIconScaningBitmap;
    //白色扫描图
    private Bitmap mScanBitmap;
    //黑色扫描背景
    private Bitmap mScanBackgroundBitmap;
    //扫描按钮区域
    private Rect mButtonArea = new Rect();
    //缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    //旋转矩阵
    private Matrix mRotateMatrix = new Matrix();
    //扫描图标旋转动画
    private ValueAnimator mRotateAnimator = new ValueAnimator();
    //手指点击时白色扫描图片缩小动画
    private ValueAnimator mScaleMinAnimator = new ValueAnimator();
    //手指放开时白色扫描图片放大动画
    private ValueAnimator mScaleMaxAnimator = new ValueAnimator();

    //扫描波纹灰色线动画
    private ValueAnimator mOutGrayAnimator = new ValueAnimator();
    //扫描波纹白色动画
    private ValueAnimator mInnerWhiteAnimator = new ValueAnimator();
    //扫描波纹灰色动画
    private ValueAnimator mBlackAnimator = new ValueAnimator();
    //画笔
    private Paint mOutGrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mInnerWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBlackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //扫描图标旋转角度
    private float mRotateDegree;
    //缩放比例，默认1:1
    private float mScaleRatio = 1;
    //设定自定义View半径
    private int mRadius;
    //扫描波纹灰色线半径
    private float mOutGrayRadius = 0;
    //扫描波纹白色部分半径
    private float mInnerWhiteRadius = 0;
    //扫描波纹灰色部分半径
    private float mBlackRadius = 0;
    //默认扫描文字提示
    private String mTipText = "点击雷达，开始探索";
    //测量扫描文字提示边界
    private Rect mTextBound = new Rect();
    //是否点击按钮，默认没有点击
    private boolean isButtonClick = false;


    public RadarScanView(Context context) {
        this(context, null);
    }

    public RadarScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //加载图片
        mIconScanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_button_icon_scan);
        mIconScaningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_button_icon_scaning);
        mScanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_button_scan);
        mScanBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_button_scan_background);
        //初始化画笔
        initViewPaint();
    }

    private void initViewPaint() {
        mOutGrayPaint.setStrokeWidth(4f);
        mOutGrayPaint.setColor(Color.rgb(228, 228, 228));
        mOutGrayPaint.setDither(true);
        mOutGrayPaint.setStyle(Paint.Style.STROKE);

        mInnerWhitePaint.setStrokeWidth(1f);
        mInnerWhitePaint.setColor(Color.rgb(241, 241, 241));
        mInnerWhitePaint.setDither(true);
        mInnerWhitePaint.setStyle(Paint.Style.FILL);

        mBlackPaint.setStrokeWidth(1f);
        mBlackPaint.setColor(Color.rgb(228, 228, 228));
        mBlackPaint.setDither(true);
        mBlackPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setTextSize(37f);
        mTextPaint.setColor(Color.rgb(185, 185, 185));
        mTextPaint.setDither(true);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void startAnim() {
        if (mScaleMinAnimator != null) {

            mScaleMinAnimator.start();
            mScaleMaxAnimator.start();
            mRotateAnimator.start();
        }
    }

    public void stopAnim() {
        if (mScaleMinAnimator != null) {

            mScaleMinAnimator.cancel();
        }
    }


    private void initScaleMinAnimator() {
        mScaleMinAnimator.setFloatValues(mRadius, mRadius * 0.87f);
        mScaleMinAnimator.setDuration(500);
        mScaleMinAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScaleRatio = ((Float) animation.getAnimatedValue()) / mRadius;
                invalidateView();
            }
        });
    }

    private void initScaleMaxAnimator() {
        mScaleMaxAnimator.setFloatValues(mRadius * 0.87f, mRadius);
        mScaleMaxAnimator.setDuration(500);
        mScaleMaxAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScaleRatio = ((Float) animation.getAnimatedValue()) / mRadius;
                invalidateView();
            }
        });
    }

    private void initRoateAnimator() {
        mRotateAnimator.setFloatValues(0, 360);
        mRotateAnimator.setDuration(1000);
        mRotateAnimator.setRepeatCount(-1);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateDegree = (Float) animation.getAnimatedValue();
                invalidateView();
            }
        });
        mRotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mTipText = "正在探索周边的...";
                //旋转动画启动后启动扫描波纹动画
                mOutGrayAnimator.start();
                mInnerWhiteAnimator.start();
                mBlackAnimator.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //取消扫描波纹动画
                mOutGrayAnimator.cancel();
                mInnerWhiteAnimator.cancel();
                mBlackAnimator.cancel();
                //重置界面要素
                mOutGrayRadius = 0;
                mInnerWhiteRadius = 0;
                mBlackRadius = 0;
                mTipText = "未能探索到周边的...，请稍后再试";
                invalidateView();
            }
        });
    }

    private void initOutGrayAnimator() {
        mOutGrayAnimator.setFloatValues(mBlackRadius, getMeasuredWidth() / 2);
        mOutGrayAnimator.setDuration(1000);
        mOutGrayAnimator.setRepeatCount(-1);
        mOutGrayAnimator.setInterpolator(new LinearInterpolator());
        mOutGrayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOutGrayRadius = (Float) animation.getAnimatedValue();
            }
        });
    }

    private void initInnerWhiteAnimator() {
        mInnerWhiteAnimator.setFloatValues(0, getMeasuredWidth() / 3);
        mInnerWhiteAnimator.setDuration(1000);
        mInnerWhiteAnimator.setRepeatCount(-1);
        mInnerWhiteAnimator.setInterpolator(new AccelerateInterpolator());
        mInnerWhiteAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mInnerWhiteRadius = (Float) animation.getAnimatedValue();
            }
        });
    }

    private void initBlackAnimator() {
        mBlackAnimator.setFloatValues(0, getMeasuredWidth() / 3);
        mBlackAnimator.setDuration(1000);
        mBlackAnimator.setRepeatCount(-1);
        mBlackAnimator.setInterpolator(new DecelerateInterpolator());
        mBlackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBlackRadius = (Float) animation.getAnimatedValue();
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //扫描按钮区域，取自扫描灰色背景图片区域即可
        mButtonArea.set(getMeasuredWidth() / 2 - mScanBackgroundBitmap.getWidth() / 2, getMeasuredHeight() / 2 - mScanBackgroundBitmap.getHeight() / 2, getMeasuredWidth() / 2 +
                mScanBackgroundBitmap
                        .getWidth() / 2, getMeasuredHeight() / 2 + mScanBackgroundBitmap.getHeight() / 2);
        //View半径,取自View宽高最小者
        mRadius = mScanBitmap.getWidth() / 2 > mScanBitmap.getHeight() / 2 ? mScanBitmap.getHeight() / 2 : mScanBitmap.getWidth() / 2;
        //初始化动画
        initScaleMinAnimator();
        initScaleMaxAnimator();
        initRoateAnimator();
        initBlackAnimator();
        initInnerWhiteAnimator();
        initOutGrayAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制波纹
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mBlackRadius, mBlackPaint);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mInnerWhiteRadius, mInnerWhitePaint);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mOutGrayRadius, mOutGrayPaint);

        //绘制背景
        Bitmap mScanBgBitmap = getScanBackgroundBitmap();
        if (mScanBgBitmap != null) {
            canvas.drawBitmap(mScanBgBitmap, getMeasuredWidth() / 2 - mScanBgBitmap.getWidth() / 2, getMeasuredHeight() / 2 - mScanBgBitmap.getHeight() / 2, new Paint(Paint
                    .ANTI_ALIAS_FLAG));
        }

        //绘制按钮背景
        Bitmap mButtonBgBitmap = getButtonBackgroundBitmap();
        canvas.drawBitmap(mButtonBgBitmap, getMeasuredWidth() / 2 - mButtonBgBitmap.getWidth() / 2, getMeasuredHeight() / 2 - mButtonBgBitmap.getHeight() / 2, new Paint(Paint.ANTI_ALIAS_FLAG));

        //绘制扫描图片
        Bitmap mScanBitmap = getScanBitmap();
        canvas.drawBitmap(mScanBitmap, getMeasuredWidth() / 2 - mScanBitmap.getWidth() / 2, getMeasuredHeight() / 2 - mScanBitmap.getHeight() / 2, new Paint(Paint.ANTI_ALIAS_FLAG));
        //绘制文本提示
        mTextPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        //此处50为文本与按钮之间间隔，可以自己设定
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mScanBackgroundBitmap.getHeight() / 2 + mTextBound.height() + 50, mTextPaint);

    }

    //绘制白色按钮背景，根据缩放矩阵与缩放比例，复制图片达到手指点击与手指放开时按钮的缩小与放大效果
    private Bitmap getButtonBackgroundBitmap() {
        mScaleMatrix.reset();
        mScaleMatrix.postScale(mScaleRatio, mScaleRatio);
        return Bitmap.createBitmap(mScanBitmap, 0, 0, mScanBitmap.getWidth(), mScanBitmap.getHeight(), mScaleMatrix, true);
    }

    //判断是否正在执行旋转动画，如果正在执行动画，则取消灰色白净
    private Bitmap getScanBackgroundBitmap() {
        if (mRotateAnimator.isRunning()) {
            return null;
        }
        return mScanBackgroundBitmap;
    }

    //判断是否正在执行动画，如果正在执行，根据旋转矩阵，与旋转的角度复制扫描图标，则实现图标不断旋转，如果未执行，则返回未扫描图片
    private Bitmap getScanBitmap() {
        if (mRotateAnimator.isRunning()) {
            mRotateMatrix.reset();
            mRotateMatrix.postRotate(mRotateDegree, mIconScaningBitmap.getWidth() / 2, mIconScaningBitmap.getHeight() / 2);
            return Bitmap.createBitmap(mIconScaningBitmap, 0, 0, mIconScaningBitmap.getWidth(), mIconScaningBitmap.getHeight(), mRotateMatrix, true);
        }
        return mIconScanBitmap;
    }

    //刷新View
    public void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
