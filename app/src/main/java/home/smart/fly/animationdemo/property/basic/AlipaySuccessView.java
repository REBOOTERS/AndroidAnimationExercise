package home.smart.fly.animationdemo.property.basic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by co-mall on 2016/10/19.
 */

public class AlipaySuccessView extends View {
    private String TAG = AlipaySuccessView.class.getSimpleName();


    private Paint mLoadPaint;
    private float mStrokeWidth = 3;
    private float mCenterX, mCenterY;
    private float mRadius;
    private final RectF mRectF = new RectF();
    private int mDegree;
    private int mPaintColor = Color.RED;
    private Float mOffsetValue = 0f;
    private Float mOffsetRightValue = 0f;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private static final float PADDING = 10;
    private ValueAnimator mCircleAnim;
    private ValueAnimator mLineLeftAnimator;
    private ValueAnimator mLineRightAnimator;

    private boolean mIsCanHide;

    public AlipaySuccessView(Context context) {
        this(context, null);
    }

    public AlipaySuccessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlipaySuccessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLoadPaint = new Paint();
        mLoadPaint.setAntiAlias(true);
        mLoadPaint.setStrokeJoin(Paint.Join.ROUND);
        mLoadPaint.setStrokeWidth(mStrokeWidth);
        mLoadPaint.setColor(mPaintColor);
        mLoadPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.left = mCenterX - mRadius;
        mRectF.top = mCenterY - mRadius;
        mRectF.right = mCenterX + mRadius;
        mRectF.bottom = mCenterY + mRadius;
        canvas.drawArc(mRectF, 0, mDegree, false, mLoadPaint);
        canvas.drawLine(mCenterX - mRadius / 2, mCenterY,
                mCenterX - mRadius / 2 + mOffsetValue, mCenterY + mOffsetValue, mLoadPaint);
        canvas.drawLine(mCenterX, mCenterY + mRadius / 2,
                mCenterX + mOffsetRightValue, mCenterY + mRadius / 2 - (3f / 2f) * mOffsetRightValue, mLoadPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        lodingCircleMeasure();
    }

    private void lodingCircleMeasure() {
        int mViewWidth = getWidth();
        int mViewHeight = getHeight();
        int mViewLength = Math.min(mViewHeight, mViewWidth);
        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;
        mRadius = (mViewLength - 2 * PADDING) / 2;
    }

    public void loadCircle() {
        if (null != mAnimatorSet && mAnimatorSet.isRunning()) {
            return;
        }
        initDegreeAndOffset();
        lodingCircleMeasure();
        mCircleAnim = ValueAnimator.ofInt(0, 360);
        mLineLeftAnimator = ValueAnimator.ofFloat(0, mRadius / 2f);
        mLineRightAnimator = ValueAnimator.ofFloat(0, mRadius / 2f);
        Log.i(TAG, "mRadius" + mRadius);
        mCircleAnim.setDuration(700);
        mLineLeftAnimator.setDuration(350);
        mLineRightAnimator.setDuration(350);
        mCircleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDegree = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLineLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mOffsetValue = (Float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mLineRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetRightValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
//        mAnimatorSet.play(mCircleAnim).before(mLineLeftAnimator);
//        mAnimatorSet.play(mLineRightAnimator).after(mLineLeftAnimator);
        mAnimatorSet.playSequentially(mCircleAnim,mLineLeftAnimator,mLineRightAnimator);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                stop();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mEndListner != null) {
                            mEndListner.onCircleDone();
                        }
                    }
                }, 800);
            }
        });
        mAnimatorSet.start();
    }

    public void stop() {
        if (null != mCircleAnim) {
            mCircleAnim.end();
        }
        if (null != mLineLeftAnimator) {
            mLineLeftAnimator.end();
        }
        if (null != mLineRightAnimator) {
            mLineRightAnimator.end();
        }
        clearAnimation();
    }

    public boolean isStarted() {
        if (null != mAnimatorSet) {
            return mAnimatorSet.isStarted();
        }
        return false;
    }

    public void initDegreeAndOffset() {
        mDegree = 0;
        mOffsetValue = 0f;
        mOffsetRightValue = 0f;
    }

    public boolean IsCanHide() {
        return mIsCanHide;
    }

    public void setCanHide(boolean mCanHide) {
        this.mIsCanHide = mCanHide;
    }

    private OnDoneCircleAnimListner mEndListner;

    public void addCircleAnimatorEndListner(OnDoneCircleAnimListner endListenr) {
        if (null == mEndListner) {
            this.mEndListner = endListenr;
        }
    }

    public interface OnDoneCircleAnimListner {
        void onCircleDone();
    }

    public void removeCircleAnimatorEndListner() {
        mEndListner = null;
    }
}
