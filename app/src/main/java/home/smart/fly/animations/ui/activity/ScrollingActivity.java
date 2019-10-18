package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.DpConvert;
import home.smart.fly.animations.widget.CustomCoordinatorLayout;
import home.smart.fly.animations.widget.CustomTabLayout;

public class ScrollingActivity extends AppCompatActivity {
    private static final String TAG = "ScrollingActivity";
    private CustomTabLayout mTabLayout;
    private LinearLayout mLinearLayout;
    private View content;
    private TextView mTextView;
    private ViewPager mViewPager;

    private AppBarLayout mAppBarLayout;
    private int screenHeight;

    private CustomCoordinatorLayout mViewGroup;

    private NestedScrollView mNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);


//        mTabLayout = findViewById(R.id.bottom_tab);
//        mLinearLayout = findViewById(R.id.bottom_container);
//        content = findViewById(R.id.bottom);
//        mTextView = findViewById(R.id.text);
//
//        mAppBarLayout = findViewById(R.id.app_bar);
//
//        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) ->
//                Log.e(TAG, "onOffsetChanged: i==" + i)
//
//        );
//
//
//        mViewGroup = findViewById(R.id.shell);
        WebView webView = findViewById(R.id.web);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://www.qq.com");
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
}
