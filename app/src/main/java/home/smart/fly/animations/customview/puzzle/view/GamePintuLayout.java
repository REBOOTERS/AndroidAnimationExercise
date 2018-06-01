package home.smart.fly.animations.customview.puzzle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import home.smart.fly.animations.R;


public class GamePintuLayout extends RelativeLayout implements OnClickListener {

    /**
     * 设置Item的数量n*n；默认为2
     */
    private int mColumn = 3;
    /**
     * 布局的宽度
     */
    private int mWidth;
    /**
     * 布局的padding
     */
    private int mPadding;
    /**
     * 存放所有的Item
     */
    private ImageView[] mGamePintuItems;
    /**
     * Item的宽度
     */
    private int mItemWidth;

    /**
     * Item横向与纵向的边距
     */
    private int mMargin = 3;

    /**
     * 拼图的图片
     */
    private Bitmap mBitmap;
    /**
     * 存放切完以后的图片bean
     */
    private List<ImagePiece> mItemBitmaps;

    private boolean once;

    public GamePintuLayout(Context context) {
        this(context, null);
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());
        // 设置Layout的内边距，四边一致，设置为四内边距中的最小值
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获得游戏布局的边长
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());

        if (!once) {
            initBitmap();
            initItem();
        }
        once = true;
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 初始化图片
     */
    private void initBitmap() {
        if (mBitmap == null)
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.totoro);

        /**
         * 将图片切成mColumn*mColumn份
         */
        mItemBitmaps = ImageSplitter.split(mBitmap, mColumn);

        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 初始化Item
     */
    private void initItem() {
        // 获得Item的宽度
        int childWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;
        mItemWidth = childWidth;
        mGamePintuItems = new ImageView[mColumn * mColumn];
        // 放置Item
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView item = new ImageView(getContext());

            item.setOnClickListener(this);

            item.setImageBitmap(mItemBitmaps.get(i).bitmap);
            mGamePintuItems[i] = item;

            item.setId(i + 1);
            item.setTag(i + "_" + mItemBitmaps.get(i).index);

            LayoutParams lp = new LayoutParams(mItemWidth,
                    mItemWidth);
            // 设置横向边距,不是最后一列
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;
            }
            // 如果不是第一列
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF,//
                        mGamePintuItems[i - 1].getId());
            }
            // 如果不是第一行，//设置纵向边距，非最后一行
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW,//
                        mGamePintuItems[i - mColumn].getId());
            }
            addView(item, lp);
        }

    }

    /**
     * 得到多值中的最小值
     *
     * @param params
     * @return
     */
    private int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (min > param) {
                min = param;
            }
        }
        return min;
    }

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        // 如果正在执行动画，则屏蔽
        if (isAniming)
            return;
        /**
         * 如果两次点击是同一个
         */
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        // 点击第一个Item
        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        } else
        // 点击第二个Item
        {
            mSecond = (ImageView) v;
            exchangeView();
        }

    }

    /**
     * 动画运行的标志位
     */
    private boolean isAniming;
    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;

    /**
     * 交换两个Item的图片
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);
        setUpAnimLayout();
        // 添加FirstView
        ImageView first = new ImageView(getContext());
        first.setImageBitmap(mItemBitmaps
                .get(getImageIndexByTag((String) mFirst.getTag())).bitmap);
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp);
        mAnimLayout.addView(first);
        // 添加SecondView
        ImageView second = new ImageView(getContext());
        second.setImageBitmap(mItemBitmaps
                .get(getImageIndexByTag((String) mSecond.getTag())).bitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        // 设置动画
        TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
                - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0,
                mFirst.getLeft() - mSecond.getLeft(), 0, mFirst.getTop()
                - mSecond.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);
        // 添加动画监听
        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                String[] firstParams = firstTag.split("_");
                String[] secondParams = secondTag.split("_");

                mFirst.setImageBitmap(mItemBitmaps.get(Integer
                        .parseInt(secondParams[0])).bitmap);
                mSecond.setImageBitmap(mItemBitmaps.get(Integer
                        .parseInt(firstParams[0])).bitmap);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);
                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                checkSuccess();
                isAniming = false;
            }
        });

    }

    /**
     * 判断游戏是否成功
     */
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView first = mGamePintuItems[i];
            Log.e("TAG", getIndexByTag((String) first.getTag()) + "");
            if (getIndexByTag((String) first.getTag()) != i) {
                isSuccess = false;
            }
        }

        if (isSuccess) {
            Toast.makeText(getContext(), "Success , Level Up !",
                    Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 获得图片的真正索引
     *
     * @param tag
     * @return
     */
    private int getIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    private int getImageIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);

    }


    public void setColumn(int mColumn) {
        this.mColumn = mColumn;
        this.removeAllViews();
        mAnimLayout = null;
//        mColumn++;
        initBitmap();
        initItem();
    }

    /**
     * 创建动画层
     */
    private void setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            mAnimLayout.setBackgroundResource(R.color.cpb_green);
            addView(mAnimLayout);
        }

    }

}
