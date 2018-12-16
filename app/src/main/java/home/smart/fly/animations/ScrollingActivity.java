package home.smart.fly.animations;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {
    private static final String TAG = "ScrollingActivity";
    private TabLayout mTabLayout;
    private LinearLayout mLinearLayout;
    private NestedScrollView content;
    private TextView mTextView;
    private ViewPager mViewPager;

    private AppBarLayout mAppBarLayout;

    private boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    mLinearLayout.animate().translationYBy(-750).start();
                    content.animate().translationYBy(-750).start();
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
                mLinearLayout.animate().translationYBy(750).start();
                content.animate().translationYBy(750).start();
            } else {
                mLinearLayout.animate().translationYBy(-750).start();
                content.animate().translationYBy(-950).start();
            }

            open = !open;

        });

//        findViewById(R.id.image).setOnClickListener(m -> {
//            mAppBarLayout.setExpanded(false,true);
//        });

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
