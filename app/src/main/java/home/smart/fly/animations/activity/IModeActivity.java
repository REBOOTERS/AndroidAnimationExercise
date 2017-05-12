package home.smart.fly.animations.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.ArgbAnimator;
import home.smart.fly.animations.utils.DpConvert;

public class IModeActivity extends AppCompatActivity {
    private static final String TAG = "IModeActivity";
    private LinearLayout head;
    private NestedScrollView mScrollView;

    private Context mContext;
    private int headHeight = 0;

    //
    private ViewPager mViewPager;

    private ArgbAnimator argb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_imode);
        //

        argb = new ArgbAnimator(Color.TRANSPARENT, getResources().getColor(R.color.colorPrimary));
        initView();
        setupStatusBar();
        headHeight = DpConvert.dip2px(mContext, 200);
        Log.e(TAG, "onCreate: " + DpConvert.dip2px(mContext, 200));
    }

    private void initView() {
        head = (LinearLayout) findViewById(R.id.head);
        head.setVisibility(View.GONE);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollview);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                setToolbarBg(scrollY);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.banner);
        mViewPager.setAdapter(new MyPagerAdapter());


    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View itemView = inflater.inflate(R.layout.recycler_view_header, container, false);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return 2;
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
        float fraction = (float) scrollY / mViewPager.getHeight();
        int color = argb.getFractionColor(fraction);
        if (color != lastColor) {
            lastColor = color;
            head.setBackgroundColor(color);
        }
    }


    private void setupStatusBar() {
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT > 21) {
            Window window = getWindow();
            View decorview = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorview.setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
