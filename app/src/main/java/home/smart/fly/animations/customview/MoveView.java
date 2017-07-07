package home.smart.fly.animations.customview;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import home.smart.fly.animations.utils.StatusBarUtil;


public class MoveView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private PointF downP;

    /**
     * view开始和结束时后的位置
     */
    private PointF currentPoint, startPoint;
    private float maxScaleX, minScaleX;
    private boolean isCanDrag;
    private GestureDetector detector;
    private OnLongClickListener longClickListener;
    private OnClickListener clickListener;
    private MoveViewListener listener;
    private boolean isSacel;
    private float doubliScale;
    private float mHeight, mWidth;
    private Bitmap bitmap;
    private long time;
    private Paint mPaint;

    /**
     * 原始View的位置
     */
    Rect viewRect, startRect;

    /**
     * 设置MoveView监听,设置此监听时候,${setOnClickListener}和${setOnLongClickListener}方法失效
     *
     * @param listener
     */
    public void setListener(MoveViewListener listener) {
        this.listener = listener;
    }

    public MoveView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        downP = new PointF(0, 0);
        minScaleX = 1;
        doubliScale = 2f;
        currentPoint = new PointF(0, 0);
        startPoint = new PointF(0, 0);
        setClickable(true);
        detector = new GestureDetector(context, this);
        time = 300;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (viewRect != null && bitmap != null) {
            double v = viewRect.width() * 1.0 / startRect.width();
            mPaint.setColor(Color.BLACK);
            mPaint.setAlpha((int) ((int) (255 * v) / maxScaleX));
            canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
            canvas.save();
            canvas.translate(currentPoint.x, currentPoint.y);
            Bitmap bitmap = creatBitmap(this.bitmap, viewRect);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.restore();
        }

    }

    /**
     * 缩放bitmap
     *
     * @param bitmap
     * @param viewRect
     * @return
     */
    private Bitmap creatBitmap(Bitmap bitmap, Rect viewRect) {
        float width = viewRect.width();
        float height = width * (bitmap.getHeight() * 1.0f / bitmap.getWidth());
        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
    }

    private void startAnimotion() {
        //起始数据
        float startTranslatY = startPoint.y;
        float stattTranslateX = startPoint.x;
        int right = startRect.right;
        int bottom = startRect.bottom;
        PointF pointF = new PointF();
        pointF.x = right;
        pointF.y = bottom;
        ViewPoint point = new ViewPoint(stattTranslateX, startTranslatY, pointF);
        //结束数据
        float width = mWidth;
        float height = mHeight;
        double v = bitmap.getWidth() * 1.0 / bitmap.getHeight();
        float endTranX = 0;
        float endTranY = 0;

        if (mWidth / mHeight > v) {
            width = (float) (mHeight * v);
            endTranX = (mWidth - width) / 2;
        } else {
            height = (float) (width / v);
            endTranY = (mHeight - height) / 2;
        }
        PointF pointF1 = new PointF(width, height);
        ViewPoint endPoint = new ViewPoint(endTranX, endTranY, pointF1);
        getAnimotion(point, endPoint, false);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(new Runnable() {
            @Override
            public void run() {
                mWidth = getWidth();
                mHeight = getHeight();
                //由于部分时候,图片并不是完全填充View,所以需要处理一下
                float width = startRect.width();
                float height = startRect.height();
                double v = bitmap.getWidth() * 1.0 / bitmap.getHeight();
                float endTranX = 0;
                float endTranY = 0;

                if (width / height > v) {
                    endTranX = (float) ((width - height * v) / 2);
                    width = (float) (height * v);
                } else {
                    endTranY = (float) ((height - width / v) / 2);
                    height = (float) (width / v);
                }
                viewRect.right = (int) width;
                viewRect.bottom = (int) height;
                startRect.right = (int) width;
                startRect.bottom = (int) height;
                currentPoint.x += endTranX;
                currentPoint.y += endTranY;
                startPoint.x += endTranX;
                startPoint.y += endTranY;
                float maxScaleX = mWidth * 1.0f / startRect.width();
                float maxScaleY = mHeight * 1.0f / startRect.height();
                MoveView.this.maxScaleX = Math.min(maxScaleX, maxScaleY);
                minScaleX = 1;
                startAnimotion();
            }
        });
    }

    /**
     * 自定义估值算法
     * Created by Administrator on 2016/2/2.
     */
    public class MyTypeEvaluator implements TypeEvaluator<ViewPoint> {
        @Override
        public ViewPoint evaluate(float fraction, ViewPoint startValue, ViewPoint endValue) {
            ViewPoint point = new ViewPoint();
            float value = fraction * fraction;
            value = (float) Math.pow(value, 0.5);
            point.translateX = startValue.translateX + (endValue.translateX - startValue.translateX) * value;
            point.translateY = startValue.translateY + (endValue.translateY - startValue.translateY) * value;
            point.sizePoint.x = startValue.sizePoint.x + (endValue.sizePoint.x - startValue.sizePoint.x) * value;
            point.sizePoint.y = startValue.sizePoint.y + (endValue.sizePoint.y - startValue.sizePoint.y) * value;
            return point;
        }
    }

    /**
     * 保存小球坐标的类
     * Created by Administrator on 2016/2/2.
     */
    public class ViewPoint {
        public float translateX;
        public float translateY;
        public PointF sizePoint;

        public ViewPoint() {
            sizePoint = new PointF();
        }

        public ViewPoint(float translateX, float translateY, PointF sizePoint) {
            this.translateX = translateX;
            this.translateY = translateY;
            this.sizePoint = sizePoint;
        }
    }


    /**
     * 设置传入进来的View
     *
     * @param originView
     */
    public void setOriginView(View originView, Bitmap bitmap) {
        viewRect = new Rect();
        startRect = new Rect();
        //获取控件的宽高
        int width = originView.getWidth();
        int height = originView.getHeight();
        viewRect.right = width;
        viewRect.bottom = height;
        startRect.right = width;
        startRect.bottom = height;
        int[] loaction = new int[2];
        originView.getLocationInWindow(loaction);
        currentPoint.x = loaction[0];
        currentPoint.y = loaction[1] - StatusBarUtil.getStatusBarHeight(getContext());
        startPoint.x = loaction[0];
        startPoint.y = loaction[1] - StatusBarUtil.getStatusBarHeight(getContext());
        this.bitmap = bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clear();
        if (isCanDrag && event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE) {
            if (startRect.width() * 1.0 / viewRect.width() > 0.6) {
                moveToPoint();
            } else {
                recoverToPosition();
            }

            isCanDrag = false;
            downP.x = 0;
            downP.y = 0;

        }
        detector.onTouchEvent(event);
        return true;
    }


    private void recoverToPosition() {
        int right = viewRect.right;
        int bottom = viewRect.bottom;
        PointF pointF = new PointF();
        pointF.x = right;
        pointF.y = bottom;
        ViewPoint point = new ViewPoint(currentPoint.x, currentPoint.y, pointF);
        //结束数据
        float width = mWidth;
        float height = mHeight;
        double v = bitmap.getWidth() * 1.0 / bitmap.getHeight();
        float endTranX = 0;
        float endTranY = 0;

        if (mWidth / mHeight > v) {
            width = (float) (mHeight * v);
            endTranX = (mWidth - width) / 2;
        } else {
            height = (float) (width / v);
            endTranY = (mHeight - height) / 2;
        }
        PointF pointF1 = new PointF(width, height);
        ViewPoint endPoint = new ViewPoint(endTranX, endTranY, pointF1);
        getAnimotion(point, endPoint, false);

    }

    /**
     * 结束
     */
    private void moveToPoint() {
        int right = viewRect.right;
        int bottom = viewRect.bottom;
        PointF pointF = new PointF();
        pointF.x = right;
        pointF.y = bottom;
        ViewPoint point = new ViewPoint(currentPoint.x, currentPoint.y, pointF);
        //结束数据
        double v = bitmap.getWidth() * 1.0 / bitmap.getHeight();
        float width = (startRect.width());
        float height = (startRect.height());
        if (mWidth / mHeight > v) {
            width = (float) (height * v);
        } else {
            height = (float) (width / v);
        }
        PointF pointF1 = new PointF(width, height);
        float startTranslatY = startPoint.y;
        float stattTranslateX = startPoint.x;
        ViewPoint endPoint = new ViewPoint(stattTranslateX, startTranslatY, pointF1);
        getAnimotion(point, endPoint, true);
    }

    ValueAnimator animator;

    private void getAnimotion(ViewPoint point, ViewPoint endPoint, final boolean miss) {
        clear();
        animator = ValueAnimator.ofObject(new MyTypeEvaluator(), point, endPoint);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewPoint point = (ViewPoint) animation.getAnimatedValue();
                viewRect.right = (int) point.sizePoint.x;
                viewRect.left = 0;
                viewRect.top = 0;
                viewRect.bottom = (int) point.sizePoint.y;
                currentPoint.y = point.translateY;
                currentPoint.x = point.translateX;
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null && miss) {
                    listener.animotionEnd(MoveView.this);
                }
            }
        });
        animator.setDuration(time);
        animator.start();
    }

    private void clear() {
        if (animator != null) {
            animator.cancel();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.clickListener = l;
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        this.longClickListener = l;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!isSacel) {
            distanceY = e2.getRawY() - e1.getRawY();
            distanceX = e2.getRawX() - e1.getRawX();

            if (distanceY > Math.abs(distanceX) && Math.abs(distanceY) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                isCanDrag = true;
            }
            if (isCanDrag) {
                float abs = Math.abs((mHeight - distanceY) * maxScaleX / mHeight);
                abs = abs > maxScaleX ? maxScaleX : (abs < minScaleX ? minScaleX : abs);
                //结束数据
                float width = (startRect.width() * abs);
                float height = (startRect.height() * abs);
                double v = bitmap.getWidth() * 1.0 / bitmap.getHeight();
                float endTranX = 0;
                float endTranY = 0;

                if (mWidth / mHeight > v) {
                    width = (float) (height * v);
                    endTranX = (mWidth - width) / 2;
                } else {
                    height = (float) (width / v);
                    endTranY = (mHeight - height) / 2;
                }
                currentPoint.x = distanceX + endTranX;
                currentPoint.y = distanceY + endTranY;
                viewRect.right = (int) width;
                viewRect.left = 0;
                viewRect.top = 0;
                viewRect.bottom = (int) height;

                postInvalidate();
            }
        } else {
            float y = -distanceY * doubliScale + getTranslationY();
            float x = -distanceX * doubliScale + getTranslationX();
            setTranslationY(y);
            setTranslationX(x);
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (!isCanDrag) {
            if (listener != null) {
                listener.onLongClick(this);
            } else if (clickListener != null) {
                longClickListener.onLongClick(this);
            }
        }

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (!isCanDrag) {
            if (listener != null) {
                listener.onClick(this);
            } else if (clickListener != null) {
                clickListener.onClick(this);
            }
            if (isSacel) {
                animate().scaleY(1).scaleX(1).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        moveToPoint();
                    }
                });
            } else {
                moveToPoint();
            }
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (!isSacel) {
            setPivotX(e.getX());
            setPivotY(e.getX());
            animate().scaleX(doubliScale).scaleY(doubliScale).setInterpolator(new AccelerateInterpolator());
            isSacel = true;
        } else {
            animate().scaleX(1).scaleY(1).translationX(0).translationY(0).setInterpolator(new AccelerateInterpolator());
            isSacel = false;
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


    public interface MoveViewListener {
        void onLongClick(View view);

        void onClick(View view);

        void animotionEnd(View view);

    }

}
