package home.smart.fly.animations.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.ArgbAnimator;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.Tools;

public class IModeActivity extends AppCompatActivity {
    private static final String URL = "http://upload.jianshu.io/admin_banners/web_images/3107/a9416a7506d328428321ffb84712ee5eb551463e.jpg";
    private static final String TAG = "IModeActivity";
    private LinearLayout head;
    private NestedScrollView mScrollView;

    private Context mContext;

    //
    private ViewPager mViewPager;

    private ArgbAnimator argb;
    private TextView title;

    private MyHandler mMyHandler = new MyHandler();

    private List<String> pics = new ArrayList<>();

    private ScheduledExecutorService mScheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_imode);
        //
        StatusBarUtil.setColor(this, R.color.transparent, 0);
        argb = new ArgbAnimator(Color.TRANSPARENT, getResources().getColor(R.color.colorPrimaryDark));
        initDatas();
        initView();
    }

    private void initDatas() {
        String data = Tools.readStrFromAssets("pics.data", mContext);
        pics = new Gson().fromJson(data, new TypeToken<List<String>>() {
        }.getType());

        pics = pics.subList(0, 4);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        head = (LinearLayout) findViewById(R.id.head);
        head.setBackgroundColor(Color.TRANSPARENT);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollview);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                setToolbarBg(scrollY);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.banner);
        mViewPager.setAdapter(new MyPagerAdapter());

        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new MyTask(), 3, 3, TimeUnit.SECONDS);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int position = mViewPager.getCurrentItem();
                    final int lastPosition = mViewPager.getAdapter().getCount() - 1;
                    Log.e(TAG, "onPageScrollStateChanged: lastPosition " + lastPosition);
                    if (position == 0) {
                        mViewPager.setCurrentItem(lastPosition == 0 ? 0 : lastPosition - 1, false);
                    } else if (position == lastPosition) {
                        mViewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
        mViewPager.setPageTransformer(true,new MyTransformer());
    }


    private class MyTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            if (position < -1) {
                ViewCompat.setScaleY(page, 0.9f);
            } else if (position <= 1) {
                float scale = Math.max(0.9f, 1 - Math.abs(position));
                ViewCompat.setScaleY(page, scale);
            } else {
                ViewCompat.setScaleY(page, 0.9f);
            }
        }
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int current = mViewPager.getCurrentItem();
            Log.e(TAG, "handleMessage: current " + current);
            mViewPager.setCurrentItem((current + 1));
        }
    }


    private class MyTask implements Runnable {

        @Override
        public void run() {
            mMyHandler.sendEmptyMessage(0);
        }
    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem: " + position);
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View itemView = inflater.inflate(R.layout.recycler_view_header, container, false);
            ImageView mImageView = (ImageView) itemView.findViewById(R.id.image);
            TextView mTextView = (TextView) itemView.findViewById(R.id.index);

            if (position == 0) {
                position = pics.size() - 1;
            } else if (position == pics.size() + 1) {
                position = 0;
            } else {
                position = position - 1;
            }

            Glide.with(mContext).load(pics.get(position)).into(mImageView);
            mTextView.setText("Index-" + position);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return pics.size() + 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    /**
     * 根据偏移值计算toolbar的颜色过渡值
     *
     * @param scrollY
     */
    private int lastColor = Color.TRANSPARENT;

    private void setToolbarBg(int scrollY) {
        float fraction = (float) scrollY / (mViewPager.getHeight());
        int color = argb.getFractionColor(fraction);
        if (color != lastColor) {
            lastColor = color;
            head.setBackgroundColor(color);
            StatusBarUtil.setColor(this,color,0);
        }

        if (fraction < 1) {
            title.setVisibility(View.INVISIBLE);
        } else {
            title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdownNow();
        }
    }
}
