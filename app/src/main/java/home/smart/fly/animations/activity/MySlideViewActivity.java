package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.LiteSwipeMenu;


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
            public void onProgressChange(float progress, View menuView, View contentView) {
                Log.e(TAG, "onProgressChange: " + progress);
                ViewCompat.setPivotX(contentView, contentView.getWidth() / 2);
                ViewCompat.setPivotY(contentView, contentView.getHeight() / 2);
                ViewCompat.setScaleX(contentView, 0.9f);
                ViewCompat.setScaleY(contentView, 0.9f);
            }
        });
    }
}
