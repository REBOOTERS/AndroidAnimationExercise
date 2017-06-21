package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.LiteSwipeMenu;


public class MySwipeMenuActivity extends AppCompatActivity {
    private static final String TAG = "MySwipeMenuActivity";
    @BindView(R.id.toolbar)
    RelativeLayout mToolbar;
    @BindView(R.id.cover)
    FrameLayout mCover;
    @BindView(R.id.mSwipeMenu)
    LiteSwipeMenu mMSwipeMenu;
    @BindView(R.id.animations)
    RadioGroup mAnimations;
    @BindView(R.id.content)
    FrameLayout mContent;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_slide_view);
        ButterKnife.bind(this);
        LiteSwipeMenu mSwipeMenu = (LiteSwipeMenu) findViewById(R.id.mSwipeMenu);
        mSwipeMenu.setMenuOffset(0.2f);
        mSwipeMenu.attachWithActivity(this);
        mSwipeMenu.setOnSwipeProgressListener(new LiteSwipeMenu.onMenuSwipeListener() {

            @Override
            public void onProgressChange(float progress, int scrollX, View menuView) {
                Log.e(TAG, "onProgressChange: " + progress);

                Log.e(TAG, "onProgressChange: scrollX=" + scrollX);


                ViewCompat.setAlpha(mCover, progress / 2);


                switch (mAnimations.getCheckedRadioButtonId()) {
                    case R.id.qq:

                        ViewCompat.setTranslationX(menuView, scrollX * progress);
                        if (progress > 0.5) {
                            mToolbar.setBackgroundResource(R.color.white);
                        } else {
                            mToolbar.setBackgroundResource(R.color.colorPrimary);
                        }
                        break;
                    case R.id.scale:
                        mToolbar.setBackgroundResource(R.color.colorPrimary);
                        ViewCompat.setPivotX(menuView, menuView.getWidth() / 2);
                        ViewCompat.setPivotY(menuView, menuView.getHeight() / 2);
                        ViewCompat.setScaleX(menuView, progress);
                        ViewCompat.setScaleY(menuView, progress);
                        break;
                    case R.id.threeD:
                        mToolbar.setBackgroundResource(R.color.colorPrimary);
                        ViewCompat.setPivotX(menuView, 0);
                        ViewCompat.setPivotY(menuView, menuView.getHeight() / 2);
                        ViewCompat.setRotationY(menuView, progress * -(90 - 60) + (90 - 60));
                        break;
                    case R.id.contentScale:
                        mToolbar.setBackgroundResource(R.color.colorPrimary);
                        ViewCompat.setPivotX(mContent, mContent.getWidth() / 2);
                        ViewCompat.setPivotY(mContent, mContent.getHeight() / 2);
                        ViewCompat.setScaleX(mContent, 1 - progress / 4);
                        ViewCompat.setScaleY(mContent, 1 - progress / 4);
                        break;
                    case R.id.drawerLayout:
                        mToolbar.setBackgroundResource(R.color.colorPrimary);
                        ViewCompat.setTranslationZ(menuView, 1);
                        ViewCompat.setTranslationX(mContent,scrollX);
                        ViewCompat.setTranslationX(menuView, -scrollX+960);
                        break;
                    default:
                        break;
                }


            }
        });


    }
}
