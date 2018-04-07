package home.smart.fly.animations.property;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import home.smart.fly.animations.R;
import home.smart.fly.animations.property.basic.GoodItem;
import home.smart.fly.animations.property.basic.GoodsListAdapter;
import home.smart.fly.animations.property.basic.GoodsListAdapter1;
import home.smart.fly.animations.utils.BaseActivity;
import home.smart.fly.animations.utils.DpConvert;

/**
 * Created by rookie on 2016/10/24.
 */

public class ShopCarAddAnimActivity extends BaseActivity implements
        GoodsListAdapter.CallBackListener,
        GoodsListAdapter1.CallBackListener {
    private Context mContext;
    private RecyclerView shoplist;
    private ImageView type;
    private int typeValue = 0;

    private LinearLayoutManager manager;
    private GridLayoutManager manager1;

    //
    private RelativeLayout shellLayout;
    private TextView carCount;
    private ImageView carImage;
    private TextView carCount1;
    private ImageView carImage1;
    private RelativeLayout bottom1, bottom2;
    //
    private GoodsListAdapter mGoodsAdapter;
    private GoodsListAdapter1 mGoodsAdpater1;
    private ArrayList<GoodItem> mData;
    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;
    // 购物车商品数目
    private int goodsCount = 0;
    private int goodsCount1 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_shopcar_anim);
    }


    private void addToCarAnimation(ImageView goodsImg) {
        //获取需要进行动画的ImageView
        final ImageView animImg = new ImageView(mContext);
        animImg.setImageDrawable(goodsImg.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        shellLayout.addView(animImg, params);
        //
        final int shellLocation[] = new int[2];
        shellLayout.getLocationInWindow(shellLocation);
        int animImgLocation[] = new int[2];
        goodsImg.getLocationInWindow(animImgLocation);
        int carLocation[] = new int[2];
        carImage.getLocationInWindow(carLocation);
        //
        // 起始点：图片起始点-父布局起始点+该商品图片的一半-图片的marginTop || marginLeft 的值
        float startX = animImgLocation[0] - shellLocation[0] + goodsImg.getWidth() / 2 - DpConvert.dip2px(mContext, 10.0f);
        float startY = animImgLocation[1] - shellLocation[1] + goodsImg.getHeight() / 2 - DpConvert.dip2px(mContext, 10.0f);

        // 商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float endX = carLocation[0] - shellLocation[0] + carImage.getWidth() / 5;
        float endY = carLocation[1] - shellLocation[1];

        //控制点，控制贝塞尔曲线
        float ctrlX = (startX + endX) / 2;
        float ctrlY = startY - 100;

        Log.e("num", "-------->" + ctrlX + " " + startY + " " + ctrlY + " " + endY);

        Path path = new Path();
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线
        path.quadTo(ctrlX, ctrlY, endX, endY);
        mPathMeasure = new PathMeasure(path, false);

        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(animImg, "scaleX", 1, 0.5f, 0.2f);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(animImg, "scaleY", 1, 0.5f, 0.2f);


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，
                // pos会自动填充上坐标，这个方法很重要。
                // mCurrentPosition此时就是中间距离点的坐标值
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                animImg.setTranslationX(mCurrentPosition[0]);
                animImg.setTranslationY(mCurrentPosition[1]);
            }
        });


        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goodsCount++;
                if (goodsCount < 100) {
                    carCount.setText(String.valueOf(goodsCount));
                } else {
                    carCount.setText("99+");
                }

                // 把执行动画的商品图片从父布局中移除
                shellLayout.removeView(animImg);
                shopCarAnim();

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(scaleXanim, scaleYanim, valueAnimator);
        animatorSet.start();


    }

    private void addToCarAnimation1(ImageView goodsImg) {
        final ImageView animImg = new ImageView(mContext);
        animImg.setImageDrawable(goodsImg.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        shellLayout.addView(animImg, params);
        //
        final int shellLocation[] = new int[2];
        shellLayout.getLocationInWindow(shellLocation);
        int animImgLocation[] = new int[2];
        goodsImg.getLocationInWindow(animImgLocation);
        int carLocation[] = new int[2];
        carImage1.getLocationInWindow(carLocation);
        //
        float startX = animImgLocation[0] - shellLocation[0] + goodsImg.getWidth() / 2;
        float startY = animImgLocation[1] - shellLocation[1] + goodsImg.getHeight() / 2;

        float endX = carLocation[0] - shellLocation[0] + carImage1.getWidth() / 5;
        float endY = carLocation[1] - shellLocation[1];

        //控制点，控制贝塞尔曲线
        float ctrlX = (startX + endX) / 2;
        float ctrlY = startY - 100;

        Log.e("num", "-------->" + ctrlX + " " + startY + " " + ctrlY + " " + endY);

        Path path = new Path();
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线
        path.quadTo(ctrlX, ctrlY, endX, endY);
        mPathMeasure = new PathMeasure(path, false);

        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(animImg, "scaleX", 1, 0.5f, 0.2f);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(animImg, "scaleY", 1, 0.5f, 0.2f);


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                animImg.setTranslationX(mCurrentPosition[0]);
                animImg.setTranslationY(mCurrentPosition[1]);
            }
        });


        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goodsCount1++;
                if (goodsCount1 < 100) {
                    carCount1.setText(String.valueOf(goodsCount1));
                } else {
                    carCount1.setText("99+");
                }

                shellLayout.removeView(animImg);
                shopCarAnim1();

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(scaleXanim, scaleYanim, valueAnimator);
        animatorSet.start();


    }

    private void shopCarAnim() {
        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(carImage, "scaleX", 1.3f, 1.2f, 1);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(carImage, "scaleY", 1.3f, 1.2f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(scaleXanim, scaleYanim);
        animatorSet.start();
    }

    private void shopCarAnim1() {
        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(carImage1, "scaleX", 1.3f, 1.2f, 1);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(carImage1, "scaleY", 1.3f, 1.2f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(scaleXanim, scaleYanim);
        animatorSet.start();
    }


    @Override
    public void initView() {
        initDatas();
        shoplist = (RecyclerView) findViewById(R.id.shoplist);
        manager = new LinearLayoutManager(mContext);
        shoplist.setLayoutManager(manager);
        shellLayout = (RelativeLayout) findViewById(R.id.shellLayout);
        carCount = (TextView) findViewById(R.id.car_count);
        carImage = (ImageView) findViewById(R.id.car_view);
        mGoodsAdapter = new GoodsListAdapter(mContext, mData);
        mGoodsAdapter.setCallBackListener(this);
        shoplist.setAdapter(mGoodsAdapter);

        mGoodsAdpater1 = new GoodsListAdapter1(mContext, mData);
        manager1 = new GridLayoutManager(mContext, 2);

        mGoodsAdpater1.setCallBackListener(this);


        type = (ImageView) findViewById(R.id.type);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeValue == 0) {
                    type.setImageResource(R.drawable.type_1);
                    shoplist.setLayoutManager(manager1);
                    shoplist.setAdapter(mGoodsAdpater1);
                    typeValue = 1;
                    bottom1.setVisibility(View.GONE);
                    bottom2.setVisibility(View.VISIBLE);
                } else {
                    type.setImageResource(R.drawable.type_0);
                    shoplist.setLayoutManager(manager);
                    shoplist.setAdapter(mGoodsAdapter);
                    typeValue = 0;
                    bottom1.setVisibility(View.VISIBLE);
                    bottom2.setVisibility(View.GONE);
                }
            }
        });

        carCount1 = (TextView) findViewById(R.id.car_count1);
        carImage1 = (ImageView) findViewById(R.id.car_view1);
        bottom1 = (RelativeLayout) findViewById(R.id.bottom1);
        bottom2 = (RelativeLayout) findViewById(R.id.bottom2);

    }

    private void initDatas() {
        mData = new ArrayList<>();
        int[] icons = new int[]{R.drawable.goods_one, R.drawable.googs_two, R.drawable.goods_three,
                R.drawable.goods_four};
        for (int i = 0; i < 20; i++) {
            GoodItem goodItem = new GoodItem(BitmapFactory.decodeResource(getResources(), icons[i % 4]));
            mData.add(goodItem);
        }
    }

    @Override
    public void callBackPlus(ImageView goodsImg) {
        addToCarAnimation(goodsImg);
    }


    @Override
    public void callBackMinus(ImageView goodsImg) {
        if (goodsCount > 0) {
            goodsCount--;
            carCount.setText(String.valueOf(goodsCount));
        }

    }

    @Override
    public void callBackPlus1(ImageView goodsImg) {
        addToCarAnimation1(goodsImg);
    }

    @Override
    public void callBackMinus1(ImageView goodsImg) {

        if (goodsCount1 > 0) {
            goodsCount1--;
            carCount1.setText(String.valueOf(goodsCount1));
        }
    }
}
