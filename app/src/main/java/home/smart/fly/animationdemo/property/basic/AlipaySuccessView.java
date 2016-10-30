package home.smart.fly.animationdemo.property.basic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by rookie on 2016/10/19.
 */

public class AlipaySuccessView extends View {

    private String TAG = AlipaySuccessView.class.getSimpleName();
    private static final float PADDING = 20;


    private Paint mCirclePanit;
    private Paint mLinePaint;

    private float mStrokeWidth = 10;
    private float mCenterX, mCenterY;
    private float mRadius = 150;
    private final RectF mRectF = new RectF();
    private int mDegree;
    private Float mLeftValue = 0f;
    private Float mRightValue = 0f;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
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
        mCirclePanit = new Paint();
        mCirclePanit.setAntiAlias(true);
        mCirclePanit.setStrokeJoin(Paint.Join.ROUND);
        mCirclePanit.setStrokeWidth(mStrokeWidth);
        mCirclePanit.setColor(Color.WHITE);
        mCirclePanit.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeWidth(mStrokeWidth);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.left = mCenterX - mRadius;
        mRectF.top = mCenterY - mRadius;
        mRectF.right = mCenterX + mRadius;
        mRectF.bottom = mCenterY + mRadius;
        canvas.drawArc(mRectF, 0, mDegree, false, mCirclePanit);
        canvas.drawLine(mCenterX - mRadius / 2, mCenterY,
                mCenterX - mRadius / 2 + mLeftValue, mCenterY + mLeftValue, mLinePaint);
        canvas.drawLine(mCenterX, mCenterY + mRadius / 2,
                mCenterX + mRightValue, mCenterY + mRadius / 2 - (3f / 2f) * mRightValue, mLinePaint);

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

    }

    public void loadCircle(int mRadius) {
        this.mRadius = mRadius - PADDING;
        if (null != mAnimatorSet && mAnimatorSet.isRunning()) {
            return;
        }
        initDegreeAndOffset();
        lodingCircleMeasure();
        Log.e("left", "R is -------->" + mRadius);
        mCircleAnim = ValueAnimator.ofInt(0, 360);
        mLineLeftAnimator = ValueAnimator.ofFloat(0, this.mRadius / 2f);
        mLineRightAnimator = ValueAnimator.ofFloat(0, this.mRadius / 2f);
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
                mLeftValue = (Float) valueAnimator.getAnimatedValue();
                Log.e("left", "-------->" + mLeftValue);
                invalidate();
            }
        });
        mLineRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRightValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimatorSet.play(mCircleAnim).before(mLineLeftAnimator);
        mAnimatorSet.play(mLineRightAnimator).after(mLineLeftAnimator);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                stop();
                if (mEndListner != null) {
                    mEndListner.onCircleDone();
                    SuccessAnim();
                }


            }
        });
        mAnimatorSet.start();
    }

    private void SuccessAnim() {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.1f, 1.0f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.1f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(scaleXAnim, scaleYAnim);
        set.start();
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
        mLeftValue = 0f;
        mRightValue = 0f;
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

    public void setPaintColor(int color) {
        mCirclePanit.setColor(color);
        mLinePaint.setColor(color);
        invalidate();
    }
}
