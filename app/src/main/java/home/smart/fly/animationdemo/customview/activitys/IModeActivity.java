package home.smart.fly.animationdemo.customview.activitys;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.DpConvert;

public class IModeActivity extends AppCompatActivity {
    private static final String TAG = "IModeActivity";
    private LinearLayout head;
    private NestedScrollView mScrollView;

    private Context mContext;
    private int headHeight = 0;

    //
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_imode);
        //


        initView();
        setupStatusBar();
        headHeight = DpConvert.dip2px(mContext, 48);
        Log.e(TAG, "onCreate: " + DpConvert.dip2px(mContext, 48));
    }

    private void initView() {
        head = (LinearLayout) findViewById(R.id.head);
        head.setVisibility(View.GONE);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollview);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {


                    if (Math.abs(scrollY) > headHeight) {
                        head.setVisibility(View.VISIBLE);
                        head.setAlpha(1.0f);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                float alpha = Math.abs(scrollY) * 1000 / headHeight;
                                Log.e(TAG, "onScrollChange: alpha " + alpha / 1000);
                                head.setAlpha(alpha);

                                if (alpha > 0) {
                                    head.setVisibility(View.VISIBLE);
                                } else {
                                    head.setVisibility(View.GONE);
                                }
                            }
                        }, 300);
                    }


                }
            });
        }

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
