package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.LiteSwipeMenu;


public class MySwipeMenuActivity extends AppCompatActivity {
    private static final String TAG = "MySwipeMenuActivity";
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_slide_view);
        color = getResources().getColor(R.color.colorPrimary);
//        StatusBarUtil.setColor(this, Color.TRANSPARENT, 0);
        LiteSwipeMenu mSwipeMenu = (LiteSwipeMenu) findViewById(R.id.mSwipeMenu);
        mSwipeMenu.setMenuOffset(0.2f);
        mSwipeMenu.setStatusBarViewColor(this, color);
        mSwipeMenu.setOnSwipeProgressListener(new LiteSwipeMenu.onMenuSwipeListener() {

            @Override
            public void onProgressChange(float progress, View menuView, View contentView) {
                Log.e(TAG, "onProgressChange: " + progress);

//                menuView.setPivotX(0);
//                menuView.setPivotY(menuView.getHeight() / 2);
//                menuView.setRotationY(progress * -(90 - 60) + (90 - 60));

                ViewCompat.setPivotX(menuView, menuView.getWidth() / 2);
                ViewCompat.setPivotY(menuView, menuView.getHeight() / 2);
                ViewCompat.setScaleX(menuView, progress);
                ViewCompat.setScaleY(menuView, progress);

//                ViewCompat.setTranslationZ(menuView,1);

            }
        });


    }
}
