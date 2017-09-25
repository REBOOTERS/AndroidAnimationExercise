package home.smart.fly.animations.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * Created by engineer on 2017/9/14.
 */

public class FantasyView extends View {
    private static final int BACK_GROUND_COLOR = Color.argb(135, 34, 128, 128);


    private Paint mPaint;
    private Bitmap mBitmap;

    private Paint mHelpPaint;

    //
    private int bitmapWidth, bitmapHeight;
    private int centerX, centerY;
    private int drawX, drawY;

    //animations

    //ClipRect
    private Rect mLeftHalfRect;
    private Rect mRightHalfRect;

    private AnimatorSet mObjectAnimator;


    public FantasyView(Context context) {
        super(context);
        init();
    }

    public FantasyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FantasyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHelpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHelpPaint.setColor(Color.RED);
        mHelpPaint.setStyle(Paint.Style.FILL);


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        //


        //
        ObjectAnimator mRightUpAnimation = ObjectAnimator.ofFloat(this, "rotationY", 0, -20);

        ObjectAnimator mLeftAnimation = ObjectAnimator.ofFloat(this, "rotationY", 0, 20);


        mObjectAnimator = new AnimatorSet();
        mObjectAnimator.setDuration(5000);
        mObjectAnimator.playTogether(mRightUpAnimation);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        canvas.drawColor(BACK_GROUND_COLOR);


//        canvas.save();
//        canvas.clipRect(centerX - bitmapWidth / 2, centerY - bitmapHeight / 2, centerX, centerY + bitmapHeight / 2);
//        canvas.drawBitmap(mBitmap, drawX, drawY, mPaint);
//        canvas.restore();


        canvas.save();
        canvas.clipRect(centerX, centerY - bitmapHeight / 2, centerX + bitmapWidth / 2, centerY + bitmapHeight / 2);
        canvas.drawBitmap(mBitmap, drawX, drawY, mPaint);
        canvas.restore();



    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mObjectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mObjectAnimator.cancel();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmapWidth = mBitmap.getWidth();
        bitmapHeight = mBitmap.getHeight();
        centerX = w / 2;
        centerY = h / 2;
        drawX = centerX - bitmapWidth / 2;
        drawY = centerY - bitmapHeight / 2;

        mLeftHalfRect = new Rect(centerX - bitmapWidth / 2, centerY - bitmapHeight / 2,
                centerX, centerY + bitmapHeight / 2);
        mRightHalfRect = new Rect(centerX, centerY - bitmapHeight / 2, centerX + bitmapWidth / 2
                , centerY + bitmapHeight / 2);
    }


}
