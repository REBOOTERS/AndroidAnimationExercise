package home.smart.fly.animations.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * Created by engineer on 2017/9/2.
 */

public class TransformView extends View {
    private static final String TAG = "TransformView";
    private Paint mPaint;
    private Matrix mMatrix;
    private Camera mCamera;
    private Paint helpPaint;

    //
    private Bitmap mBitmap;

    //
    private Point center;
    private int viewW, viewH;
    //
    private float degreeX, degreeY, degreeZ;
    private float scaleX = 1, scaleY = 1;
    private boolean isFixed = true;

    private boolean UpDownFlipView = true, LeftRightFlipView;


    public TransformView(Context context) {
        super(context);
        init();
    }

    public TransformView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TransformView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


        //
        mPaint = new Paint();
        mCamera = new Camera();
        mMatrix = new Matrix();

        initBitmap();
        //
        helpPaint = new Paint();
        helpPaint.setStyle(Paint.Style.STROKE);
    }

    private void initBitmap() {
        if (UpDownFlipView || LeftRightFlipView) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
            center = new Point((viewW - mBitmap.getWidth()) / 2,
                    (viewH - mBitmap.getHeight()) / 2);
            //
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float newZ = -displayMetrics.density * 6;
            mCamera.setLocation(0, 0, newZ);

        } else {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_robot);
            center = new Point((viewW - mBitmap.getWidth()) / 2,
                    (viewH - mBitmap.getHeight()) / 2);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCamera.save();
        mMatrix.reset();
        mCamera.rotateX(degreeX);
        mCamera.rotateY(degreeY);
        mCamera.rotateZ(degreeZ);

        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        if (isFixed) {
            mMatrix.preTranslate(-center.x - mBitmap.getWidth() / 2, -center.y - mBitmap.getHeight() / 2);
            mMatrix.postTranslate(center.x + mBitmap.getWidth() / 2, center.y + mBitmap.getHeight() / 2);
        }

        mMatrix.postScale(scaleX, scaleY, center.x + mBitmap.getWidth() / 2, center.y + mBitmap.getHeight() / 2);

        //上下翻转的FlipView
        if (UpDownFlipView) {
            canvas.save();
            canvas.clipRect(center.x, center.y, center.x + mBitmap.getWidth(), center.y + mBitmap.getHeight() / 2);
            canvas.drawBitmap(mBitmap, center.x, center.y, mPaint);
            canvas.restore();


            canvas.save();
            canvas.concat(mMatrix);


            canvas.clipRect(center.x, center.y + mBitmap.getHeight() / 2, center.x + mBitmap.getWidth(),
                    center.y + mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, center.x, center.y, mPaint);
            canvas.restore();


        } else if (LeftRightFlipView) {
            canvas.save();
            canvas.clipRect(center.x + mBitmap.getWidth() / 2, center.y, center.x + mBitmap.getWidth(),
                    center.y + mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, center.x, center.y, mPaint);
            canvas.restore();


            canvas.save();
            canvas.concat(mMatrix);

            canvas.clipRect(center.x, center.y, center.x + mBitmap.getWidth() / 2, center.y + mBitmap.getHeight());

            canvas.drawBitmap(mBitmap, center.x, center.y, mPaint);
            canvas.restore();

        } else {
            canvas.save();
            canvas.concat(mMatrix);
            canvas.drawBitmap(mBitmap, center.x, center.y, mPaint);
            canvas.restore();


        }

        canvas.drawRect(center.x, center.y,
                center.x + mBitmap.getWidth(), center.y + mBitmap.getHeight(), helpPaint);


    }

    public void setDegreeX(float degree) {
        this.degreeX = degree;
        invalidate();
    }

    public void setDegreeY(float degree) {
        this.degreeY = degree;
        invalidate();
    }

    public void setDegreeZ(float degreeZ) {
        this.degreeZ = degreeZ;
        invalidate();
    }

    @Override
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        invalidate();
    }

    @Override
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        invalidate();
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    public void setUpDownFlipView(boolean upDownFlipView) {
        UpDownFlipView = upDownFlipView;
        initBitmap();
        invalidate();
    }

    public void setLeftRightFlipView(boolean leftRightFlipView) {
        LeftRightFlipView = leftRightFlipView;
        initBitmap();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
        center = new Point((w - mBitmap.getWidth()) / 2,
                (h - mBitmap.getHeight()) / 2);

    }


}
