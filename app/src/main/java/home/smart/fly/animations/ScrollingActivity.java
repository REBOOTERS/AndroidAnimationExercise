package home.smart.fly.animations;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ScrollingActivity extends AppCompatActivity {
    private static final String TAG = "ScrollingActivity";
    private TabLayout mTabLayout;
    private NestedScrollView content;

    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = findViewById(R.id.bottom_tab);
        content = findViewById(R.id.bottom);

        mAppBarLayout = findViewById(R.id.app_bar);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> Log.e(TAG, "onOffsetChanged: i==" + i));

//        findViewById(R.id.image).setOnClickListener(v -> {
//            mTabLayout.animate().translationYBy(-550).start();
//            content.animate().translationYBy(-550).start();
//        });

        findViewById(R.id.bottom_tab).setOnClickListener(m -> {
            mAppBarLayout.setExpanded(false,true);
        });

    }
}
