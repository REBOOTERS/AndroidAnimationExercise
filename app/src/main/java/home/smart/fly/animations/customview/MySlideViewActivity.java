package home.smart.fly.animations.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import home.smart.fly.animations.R;


public class MySlideViewActivity extends AppCompatActivity {
    private static final String TAG = "MySlideViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_slide_view);
        LiteSwipeMenu mSwipeMenu = (LiteSwipeMenu) findViewById(R.id.mSwipeMenu);
        mSwipeMenu.setMenuOffset(0.2f);
        mSwipeMenu.setOnSwipeProgressListener(new LiteSwipeMenu.onMenuSwipeListener() {
            @Override
            public void onProgressChange(float progress) {
                Log.e(TAG, "onProgressChange: " + progress);
            }
        });
    }
}
