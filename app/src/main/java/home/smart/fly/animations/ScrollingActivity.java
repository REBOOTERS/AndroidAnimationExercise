package home.smart.fly.animations;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import home.smart.fly.animations.utils.DpConvert;

public class ScrollingActivity extends AppCompatActivity {
    private static final String TAG = "ScrollingActivity";
    private TabLayout mTabLayout;
    private LinearLayout mLinearLayout;
    private View content;
    private TextView mTextView;
    private ViewPager mViewPager;

    private AppBarLayout mAppBarLayout;
    private int screenHeight;

    private boolean open = false;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = findViewById(R.id.bottom_tab);
        mLinearLayout = findViewById(R.id.bottom_container);
        content = findViewById(R.id.bottom);
        mTextView = findViewById(R.id.text);

        mAppBarLayout = findViewById(R.id.app_bar);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> Log.e(TAG, "onOffsetChanged: i==" + i));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!open) {
                    mLinearLayout.animate().translationYBy(-getRealH()).start();
                    content.animate().translationYBy(-getRealH()).start();
                    open = true;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findViewById(R.id.image).setOnClickListener(v -> {
            if (open) {
                mLinearLayout.animate().translationYBy(getRealH()).start();
                content.animate().translationYBy(getRealH()).start();
            } else {
                mLinearLayout.animate().translationYBy(-getRealH()).start();
                content.animate().translationYBy(-getRealH()).start();
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) content.getLayoutParams();
                layoutParams.topMargin = DpConvert.dip2px(this, -120);
                content.requestLayout();
            }

            open = !open;

        });

        findViewById(R.id.image2).setOnClickListener(m -> {
            mAppBarLayout.setExpanded(false, false);
        });

        content.setOnClickListener(v -> mAppBarLayout.setExpanded(true));

    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getStatusBarHeight() {
        int resourcesId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourcesId);
    }

    private int getRealH() {
        int h = screenHeight - getStatusBarHeight() - DpConvert.dip2px(this, 48) - mLinearLayout.getMeasuredHeight();
        return h;
    }
}
