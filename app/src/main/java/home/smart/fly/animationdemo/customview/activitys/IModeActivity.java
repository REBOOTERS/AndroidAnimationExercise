package home.smart.fly.animationdemo.customview.activitys;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.DpConvert;

public class IModeActivity extends AppCompatActivity {
    private static final String TAG = "IModeActivity";
    private LinearLayout head;
    private ScrollView mScrollView;

    private Context mContext;
    private int headHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_imode);


        initView();
        setupStatusBar();
        headHeight = DpConvert.dip2px(mContext, 48);
        Log.e(TAG, "onCreate: " + DpConvert.dip2px(mContext, 48));
    }

    private void initView() {
        head = (LinearLayout) findViewById(R.id.head);
        head.setAlpha(0.0f);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    float alpha = Math.abs(scrollY) * 1000 / headHeight;
                    Log.e(TAG, "onScrollChange: alpha " + alpha / 1000);
                    head.setAlpha(alpha);
                }
            });
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
