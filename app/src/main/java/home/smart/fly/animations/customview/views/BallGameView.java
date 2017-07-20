package home.smart.fly.animations.customview.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

/**
 * Created by engineer on 2017/7/15.
 */

public class BallGameView extends View {
    private static final String TAG = "BallGameView";

    private static final int PLAYER_COUNT = 11;

    private Paint mPaint;


    private int screenW;
    //整个view 宽高
    private int viewW, viewH;
    //球员宽高
    private int playW, playH;
    //背景图片原始大小
    private Rect bitmapRect;
    //绘制区域大小
    private Rect mViewRect;

    private Resources res;
    private int lastX, lastY;
    //背景图
    private Bitmap backgroundBitmap;
    //11名球员位图
    private Bitmap[] players = new Bitmap[PLAYER_COUNT];
    //被选中位置
    private Bitmap selectedBitmap;
    //11名球员位置
    private Point[] positions = new Point[PLAYER_COUNT];
    //
    private int currentPos = -1;
    //


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
        res = getResources();
        screenW = Tools.getScreenWidth(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);


        backgroundBitmap = BitmapFactory.decodeResource(res, R.drawable.battle_bg);
        //确保整张背景图，都能完整的显示出来
        bitmapRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());

        //初始化11个球员位置
        Bitmap play = BitmapFactory.decodeResource(res, R.drawable.battle_element_normal);
        playW = play.getWidth();
        playH = play.getHeight();

        for (int i = 0; i < PLAYER_COUNT; i++) {
            players[i] = play;
        }

        //
        selectedBitmap = BitmapFactory.decodeResource(res, R.drawable.battle_element_checked);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawBitmap(backgroundBitmap, bitmapRect, mViewRect, mPaint);
        //绘制初始的11个球员
        for (int i = 0; i < players.length; i++) {
            if (i == currentPos) {
                canvas.drawBitmap(selectedBitmap, positions[i].x - playW / 2, positions[i].y - playW / 2, mPaint);
            } else {
                canvas.drawBitmap(players[i], positions[i].x - playW / 2, positions[i].y - playW / 2, mPaint);
            }

        }
    }

    int position = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lastX = (int) event.getX();
        lastY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                for (int i = 0; i < positions.length; i++) {
                    int deltaX = positions[i].x - lastX;
                    int deltaY = positions[i].y - lastY;

                    if (Math.abs(deltaX) < playW / 2 && Math.abs(deltaY) < playW / 2) {
                        position = i;
                        currentPos = i;
                        setSelectedPlayer();

                        return true;
                    }


                }
                Log.e(TAG, "onTouchEvent: position= " + position);
                return false;

            case MotionEvent.ACTION_MOVE:

                if (position == -1) {
                    return true;
                }

                positions[position].x = lastX;
                positions[position].y = lastY;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                position = -1;
                break;
            default:
                break;


        }

        return false;

    }

    private void setSelectedPlayer() {
        invalidate();
    }


    public void updatePlayer(Bitmap bitmap) {
        players[currentPos] = bitmap;
        invalidate();
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
