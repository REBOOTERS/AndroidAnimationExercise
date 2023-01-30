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
import android.graphics.RectF;

import androidx.annotation.Nullable;

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
    private Paint mRectPaint;
    private TextPaint mTextPaint;
    private TextPaint mTipPaint;

    private String tips = "先点气泡，再点球员";


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
    //透明圆角背景 RectF
    private RectF mRoundRect;
    //可移动位置，在纵垂直方向的上下边界
    private int minY, maxY;

    private Resources res;
    //背景图
    private Bitmap backgroundBitmap;
    //被选中带金色底座的位图
    private Bitmap selectedBitmap;
    //球员被选中标记
    private Bitmap playSelectedBitmap;
    //球员背景图
    private Bitmap playeBgBitmap;
    //11名球员位图
    private Player[] players = new Player[PLAYER_COUNT];

    //11名球员位置
    private Point[] positions = new Point[PLAYER_COUNT];
    //当前选中球员
    private int currentPos = -1;
    //
    private GestureDetector m_gestureDetector;
    private boolean moveEnable;
    //是否全部气泡完成 设置
    private boolean isFull;
    // 属性动画
    private ValueAnimator mValueAnimator;

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
        isFull = false;
        m_gestureDetector = new GestureDetector(context, onGestureListener);
        res = getResources();
        screenW = Tools.getScreenWidth(context);
        //
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //
        mTextPaint = new TextPaint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(35);

        //
        mTipPaint = new TextPaint();
        mTipPaint.setTextAlign(Paint.Align.CENTER);
        mTipPaint.setColor(Color.YELLOW);
        mTipPaint.setTextSize(35);
        //
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.argb(128, 0, 0, 0));


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
        //绘制提示文字透明背景
        canvas.drawRoundRect(mRoundRect, 8, 8, mRectPaint);
        //绘制底部提示文字 ( TextPiant 文字垂直居中实现 http://blog.csdn.net/hursing/article/details/18703599)
        Paint.FontMetricsInt fontMetrics = mTipPaint.getFontMetricsInt();
        float baseY = (mRoundRect.bottom + mRoundRect.top) / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f;
        canvas.drawText(tips, screenW / 2f, baseY, mTipPaint);


        //绘制初始的11个气泡
        for (int i = 0; i < players.length; i++) {
            //绘制当前选中的球员
            if (i == currentPos) {

                if (players[i].isSetReal()) {
                    //绘制球员头像
                    canvas.drawBitmap(players[i].getBitmap(), positions[i].x - playW / 2f,
                            positions[i].y - playW / 2f, mPaint);
                    //绘制选中球员金色底座
                    canvas.drawBitmap(playSelectedBitmap, positions[i].x - goldW / 2f,
                            positions[i].y - goldH / 2f, mPaint);

                    //绘制球员姓名
                    canvas.drawText(players[i].getName(), positions[i].x,
                            positions[i].y + playW, mTextPaint);

                } else {
                    canvas.drawBitmap(selectedBitmap, positions[i].x - playW / 2f,
                            positions[i].y - playW / 2f, mPaint);
                }


            } else {
                canvas.drawBitmap(players[i].getBitmap(), positions[i].x - playW / 2f,
                        positions[i].y - playW / 2f, mPaint);

                //设置了真实头像的气泡
                if (players[i].isSetReal()) {

                    //绘制球员姓名
                    canvas.drawText(players[i].getName(), positions[i].x,
                            positions[i].y + playW, mTextPaint);
                    //绘制已设置正常图片球员背景
                    canvas.drawBitmap(playeBgBitmap, positions[i].x - grayW / 2f,
                            positions[i].y + 200, mPaint);
                }
            }
        }
    }

    int position = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mValueAnimator != null) {
            if (mValueAnimator.isRunning()) {
                return false;
            }
        }


        m_gestureDetector.onTouchEvent(event);

        int lastX = (int) event.getX();
        int lastY = (int) event.getY();


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < positions.length; i++) {
                int deltaX = positions[i].x - lastX;
                int deltaY = positions[i].y - lastY;

                // 手指 -- ACTION_DOWN 时，落在了某一个气泡上时，刷新选中气泡（球员）的bitmap
                if (Math.abs(deltaX) < playW / 2f && Math.abs(deltaY) < playW / 2f) {
                    position = i;
                    currentPos = i;
                    invalidate();
                    moveEnable = true;
                    Log.e(TAG, "onTouchEvent: position= " + position);
                    return true;
                }


            }

            //没有点击中任意一个气泡，点击在外部是，重置气泡（球员）状态
            resetBubbleView();
            moveEnable = false;
            return false;
        }


        return super.onTouchEvent(event);

    }

    private void resetBubbleView() {
        currentPos = -1;
        invalidate();
    }

    /**
     * 清除画布上的辅助元素（包括底部提示，金色底座等，方便生成最终的截图）
     */
    public void clearInvalidView() {
        currentPos = -1;
        mTipPaint.setColor(Color.TRANSPARENT);
        mRectPaint.setColor(Color.TRANSPARENT);
        invalidate();
    }


    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (moveEnable) {
                positions[position].x -= distanceX;
                positions[position].y -= distanceY;


                //滑动时，考虑一下上下边界的问题，不要把球员移除场外
                // 横向就不考虑了，因为底图是3D 摆放的，上窄下宽，无法计算
                // 主要限制一下，纵向滑动值
                if (positions[position].y < minY) {
                    positions[position].y = minY;
                } else if (positions[position].y > maxY) {
                    positions[position].y = maxY;
                }

                Log.e(TAG, "onScroll: y=" + positions[position].y);

                //跟随手指，移动气泡（球员）
                invalidate();
            }
            return true;
        }
    };

    /**
     * 在下方球员区域，选中球员后，根据位置执行动画，将球员放置在选中的气泡中
     *
     * @param bitmap      被选中球员bitmap
     * @param name        被选中球员名字
     * @param location    被选中球员在屏幕中位置
     * @param contentView 根视图（方便实现动画）
     */
    public void updatePlayer(final Bitmap bitmap, final String name, int[] location, final ViewGroup contentView) {

        Path mPath = new Path();
        mPath.moveTo(location[0] + bitmap.getWidth() / 2f, location[1] - bitmap.getHeight() / 2f);
        mPath.lineTo(positions[currentPos].x - playW / 2f, positions[currentPos].y - playW / 2f);


        final ImageView animImage = new ImageView(getContext());
        animImage.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 120);
        contentView.addView(animImage, params);


        final float[] animPositions = new float[2];
        final PathMeasure mPathMeasure = new PathMeasure(mPath, false);

        mValueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
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

                updateBubbleAndTips();


            }
        });
        mValueAnimator.setDuration(500);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.start();


    }

    /**
     * 根据气泡数据，更新视图
     */
    private void updateBubbleAndTips() {
        for (int i = 0; i < PLAYER_COUNT; i++) {
            if (!players[i].isSetReal()) {
                // 还有某个气泡没有设置真实的球员 头像
                isFull = false;
                break;
            }

            isFull = true;
        }

        if (isFull) {
            //当全部气泡都有文字时，修改底部提示文字
            mRoundRect.left = mRoundRect.left - 100;
            mRoundRect.right = mRoundRect.right + 100;

            mRoundRect.left = mRoundRect.left < screenW / 2f - 300 ? screenW / 2f - 300 : mRoundRect.left;
            mRoundRect.right = mRoundRect.right > screenW / 2f + 300 ? screenW / 2f + 300 : mRoundRect.right;

            //绘制底部提示文字
            tips = "点击右上角下一步，预览惊喜";
        }

        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            //动画未结束时，Activity-finish，强制结束动画，避免内存泄漏
            mValueAnimator.cancel();
        }
    }

    public int getCurrentPos() {
        return currentPos;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;

        initSize(w, h);

        Log.e(TAG, "onSizeChanged: w= " + w);
        Log.e(TAG, "onSizeChanged: h= " + h);

    }

    private void initSize(int w, int h) {
        initBubblePositions(w, h);
        //目标区域，在整个视图的大小中，绘制Bitmap
        mViewRect = new Rect(0, 0, viewW, viewH);
        mRoundRect = new RectF(screenW / 2f - 200, maxY + 15, screenW / 2f + 200, getHeight() - 15);
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


        //此处滑动时上下边界，正常情况下应该是背景图中上下两条边线的那个位置的值
        // 但是，实在是不知道如何从一张图中获取某条线的位置（哪位大神如果知道，不吝赐教）
        //因此，这里的上下边界 是根据自己手机上滑动位置,取得大概值 o(╯□╰)o
        minY = h / 5;
        maxY = (int) (h * 0.948);
    }
}
