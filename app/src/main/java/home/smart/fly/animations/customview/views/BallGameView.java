package home.smart.fly.animations.customview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.Player;
import home.smart.fly.animations.utils.Tools;

/**
 * Created by engineer on 2017/7/15.
 */

public class BallGameView extends View {
    private static final String TAG = "BallGameView";

    private static final int PLAYER_COUNT = 11;

    private Paint mPaint;
    private TextPaint mTextPaint;


    private int screenW;
    //整个view 宽高
    private int viewW, viewH;
    //球员宽高
    private int playW, playH;
    //金色背景宽高
    private int goldW, goldH;
    // 普通背景kg
    private int grayW, grayH;
    //背景图片原始大小
    private Rect bitmapRect;
    //绘制区域大小
    private Rect mViewRect;

    private Resources res;
    //背景图
    private Bitmap backgroundBitmap;
    //被选中位置
    private Bitmap selectedBitmap;
    //球员被选中标记
    private Bitmap playSelectedBitmap;
    //球员背景图
    private Bitmap playeBgBitmap;
    //11名球员位图
    private Player[] players = new Player[PLAYER_COUNT];

    //11名球员位置
    private Point[] positions = new Point[PLAYER_COUNT];
    //
    private int currentPos = -1;
    //
    private GestureDetector m_gestureDetector;
    private boolean moveEnable;

    public BallGameView(Context context) {
        super(context);
        init(context);
    }

    public BallGameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BallGameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        m_gestureDetector = new GestureDetector(context, onGestureListener);
        res = getResources();
        screenW = Tools.getScreenWidth(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTextPaint = new TextPaint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(35);


        backgroundBitmap = BitmapFactory.decodeResource(res, R.drawable.battle_bg);
        //确保整张背景图，都能完整的显示出来
        bitmapRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());

        //初始化11个球员位置
        Bitmap play = BitmapFactory.decodeResource(res, R.drawable.battle_element_normal);
        playW = play.getWidth();
        playH = play.getHeight();

        for (int i = 0; i < PLAYER_COUNT; i++) {
            Player mPlayer = new Player();
            mPlayer.setBitmap(play);
            players[i] = mPlayer;
        }

        //
        selectedBitmap = BitmapFactory.decodeResource(res, R.drawable.battle_element_checked);

        playSelectedBitmap = BitmapFactory.decodeResource(res, R.drawable.bg_battle_element_selected);
        goldW = playSelectedBitmap.getWidth();
        goldH = playSelectedBitmap.getHeight();
        playeBgBitmap = BitmapFactory.decodeResource(res, R.drawable.bg_battle_element_normal);
        grayW = playeBgBitmap.getWidth();
        grayH = playeBgBitmap.getHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawBitmap(backgroundBitmap, bitmapRect, mViewRect, mPaint);
        //绘制初始的11个球员
        for (int i = 0; i < players.length; i++) {
            if (i == currentPos) {

                if (players[i].isSetReal()) {
                    //绘制球员头像
                    canvas.drawBitmap(players[i].getBitmap(), positions[i].x - playW / 2,
                            positions[i].y - playW / 2, mPaint);
                    //绘制选中球员金色底座
                    canvas.drawBitmap(playSelectedBitmap, positions[i].x - goldW / 2,
                            positions[i].y - goldH / 2, mPaint);

                    //绘制球员姓名
                    canvas.drawText(players[i].getName(), positions[i].x,
                            positions[i].y + playW, mTextPaint);

                } else {
                    canvas.drawBitmap(selectedBitmap, positions[i].x - playW / 2,
                            positions[i].y - playW / 2, mPaint);
                }


            } else {
                canvas.drawBitmap(players[i].getBitmap(), positions[i].x - playW / 2,
                        positions[i].y - playW / 2, mPaint);
                if (players[i].isSetReal()) {
                    canvas.drawText(players[i].getName(), positions[i].x,
                            positions[i].y + playW, mTextPaint);

                    //绘制已设置正常图片球员背景
                    canvas.drawBitmap(playeBgBitmap, positions[i].x - grayW / 2,
                            positions[i].y + 200, mPaint);
                }
            }

        }
    }

    int position = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_gestureDetector.onTouchEvent(event);

        int lastX = (int) event.getX();
        int lastY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                for (int i = 0; i < positions.length; i++) {
                    int deltaX = positions[i].x - lastX;
                    int deltaY = positions[i].y - lastY;

                    if (Math.abs(deltaX) < playW / 2 && Math.abs(deltaY) < playW / 2) {
                        position = i;
                        currentPos = i;
                        setSelectedPlayer();
                        moveEnable = true;
                        Log.e(TAG, "onTouchEvent: position= " + position);
                        return true;
                    }


                }
                moveEnable = false;
                Log.e(TAG, "onTouchEvent: position= " + position);
                return false;


            case MotionEvent.ACTION_UP:
                position = -1;
                break;
            default:
                break;


        }

        return super.onTouchEvent(event);

    }


    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (moveEnable) {
                positions[position].y -= distanceY;
                positions[position].x -= distanceX;
                //处理边界问题
//                if (positions[position].x < radius) {
//                    positions[position].x = radius;
//                } else if (centerX > getWidth() - radius) {
//                    positions[position].x = getWidth() - radius;
//                }
//                if (positions[position].y < radius) {
//                    positions[position].y = radius;
//                } else if (centerY > getHeight() - radius) {
//                    positions[position].y = getHeight() - radius;
//                }
                //修改圆心后，通知重绘
                postInvalidate();
            }
            return true;
        }
    };

    /**
     * 计算两点间的距离
     */
    private int getDistanceByPoint(int x1, int y1, int x2, int y2) {
        double temp = Math.abs((x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1));
        int result = (int) Math.sqrt(temp);
        Log.e(TAG, "getDistanceByPoint: result=" + result);
        return result;
    }


    private void setSelectedPlayer() {
        invalidate();
    }


    public void updatePlayer(final Bitmap bitmap, final String name, int[] location, final ViewGroup contentView) {

        Path mPath = new Path();
        mPath.moveTo(location[0] + bitmap.getWidth() / 2, location[1] - bitmap.getHeight() / 2);
        mPath.lineTo(positions[currentPos].x - playW / 2, positions[currentPos].y - playW / 2);


        final ImageView animImage = new ImageView(getContext());
        animImage.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 120);
        contentView.addView(animImage, params);


        final float[] animPositions = new float[2];
        final PathMeasure mPathMeasure = new PathMeasure(mPath, false);

        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, animPositions, null);

                animImage.setTranslationX(animPositions[0]);
                animImage.setTranslationY(animPositions[1]);

            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                contentView.removeView(animImage);

                players[currentPos].setBitmap(bitmap);
                players[currentPos].setSetReal(true);
                players[currentPos].setName(name);

                invalidate();
            }
        });
        mValueAnimator.setDuration(500);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.start();


    }

    public int getCurrentPos() {
        return currentPos;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewW = screenW;
        int viewH = (int) (screenW * 1.3);
        setMeasuredDimension(viewW, viewH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
        //目标区域，在整个视图的大小中，绘制Bitmap
        mViewRect = new Rect(0, 0, viewW, viewH);

        Log.e(TAG, "onSizeChanged: w= " + w);
        Log.e(TAG, "onSizeChanged: h= " + h);

        initBubblePositions(w, h);
    }

    /**
     * 根据整个View的宽高，大体估算出一个4-4-2 阵型位置，放置初始的11名球员位置
     *
     * @param w
     * @param h
     */
    private void initBubblePositions(int w, int h) {
        int x = w / 2;
        int y = h / 2;

        int spaceX = w / 8;
        int spaceY = h / 8;

        positions[0] = new Point(x - spaceX, y - spaceX * 2);
        positions[1] = new Point(x + spaceX, y - spaceX * 2);

        positions[2] = new Point(x - spaceX * 3, y);
        positions[3] = new Point(x - spaceX, y);
        positions[4] = new Point(x + spaceX, y);
        positions[5] = new Point(x + spaceX * 3, y);

        positions[6] = new Point(x - spaceX * 3, y + spaceX * 2);
        positions[7] = new Point(x - spaceX, y + spaceX * 2);
        positions[8] = new Point(x + spaceX, y + spaceX * 2);
        positions[9] = new Point(x + spaceX * 3, y + spaceX * 2);

        positions[10] = new Point(x, h - spaceY);
    }
}
