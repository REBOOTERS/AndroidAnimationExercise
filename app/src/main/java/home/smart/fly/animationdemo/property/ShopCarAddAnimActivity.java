package home.smart.fly.animationdemo.property;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.GoodItem;
import home.smart.fly.animationdemo.property.basic.GoodsListAdapter;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by co-mall on 2016/10/24.
 */

public class ShopCarAddAnimActivity extends BaseActivity implements GoodsListAdapter.CallBackListener {
    private Context mContext;
    private RecyclerView shoplist;

    //
    private RelativeLayout shellLayout;
    private TextView carCount;
    private ImageView carImage;
    //
    private GoodsListAdapter mGoodsAdapter;
    private ArrayList<GoodItem> mData;
    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;
    // 购物车商品数目
    private int goodsCount = 0;

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
        // 开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = animImgLocation[0] - shellLocation[0] + goodsImg.getWidth() / 2;
        float startY = animImgLocation[1] - shellLocation[1] + goodsImg.getHeight() / 2;

        // 商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float endX = carLocation[0] - shellLocation[0] + carImage.getWidth() / 5;
        float endY = carLocation[1] - shellLocation[1];

        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        // mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(animImg, "scaleX", 1, 0.5f, 0.2f);
        ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(animImg, "scaleY", 1, 0.5f, 0.2f);


        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
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
                carCount.setText(String.valueOf(goodsCount));
                // 把执行动画的商品图片从父布局中移除
                shellLayout.removeView(animImg);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(scaleXanim, scaleYanim, valueAnimator);
        animatorSet.start();


    }


    @Override
    public void initView() {
        initDatas();


        shoplist = (RecyclerView) findViewById(R.id.shoplist);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        shoplist.setLayoutManager(manager);
        shellLayout = (RelativeLayout) findViewById(R.id.shellLayout);
        carCount = (TextView) findViewById(R.id.car_count);
        carImage = (ImageView) findViewById(R.id.car_view);
        mGoodsAdapter = new GoodsListAdapter(mContext, mData);
        mGoodsAdapter.setCallBackListener(this);
        shoplist.setAdapter(mGoodsAdapter);

    }

    private void initDatas() {
        mData = new ArrayList<>();
        int[] icons = new int[]{R.drawable.goods_one, R.drawable.goods_two, R.drawable.goods_three};
        for (int i = 0; i < 20; i++) {
            GoodItem goodItem = new GoodItem(BitmapFactory.decodeResource(getResources(), icons[i % 3]));
            mData.add(goodItem);
        }
    }

    @Override
    public void callBackImg(ImageView goodsImg) {
        addToCarAnimation(goodsImg);
    }

}
