package home.smart.fly.animations.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.LiteMenuHelper;
import home.smart.fly.animations.customview.LiteSwipeMenu;


public class MySwipeMenuActivity extends AppCompatActivity {
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
    @BindView(R.id.menuView)
    LinearLayout menuView;


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_my_slide_view);
        ButterKnife.bind(this);
        LiteSwipeMenu mSwipeMenu = (LiteSwipeMenu) findViewById(R.id.mSwipeMenu);
        mSwipeMenu.setMenuOffset(0.2f);
        mSwipeMenu.attachWithActivity(this);
        mSwipeMenu.setOnSwipeProgressListener(new LiteSwipeMenu.onMenuSwipeListener() {

            @Override
            public void onProgressChange(float progress, int scrollX) {

                //结合滑动比，视图区域变暗效果
                ViewCompat.setAlpha(mCover, progress / 2);


                switch (mAnimations.getCheckedRadioButtonId()) {
                    case R.id.qq:
                        //菜单视图按比例进行轻微移动，呈现滑动时菜单栏和内容区域的视差移动效果
                        ViewCompat.setTranslationX(menuView, scrollX * progress);
                        if (progress > 0.5) {
                            //滑动到一半时，修改标题栏背景色
                            mToolbar.setBackgroundResource(R.color.white);
                        } else {
                            mToolbar.setBackgroundResource(R.color.colorPrimary);
                        }
                        break;
                    case R.id.scale:
                        //修改参考点为菜单栏中心，实现缩放动画
                        ViewCompat.setPivotX(menuView, menuView.getWidth() / 2);
                        ViewCompat.setPivotY(menuView, menuView.getHeight() / 2);
                        ViewCompat.setScaleX(menuView, progress);
                        ViewCompat.setScaleY(menuView, progress);
                        break;
                    case R.id.threeD:
                        // 以x轴菜单宽度1/3处为结点，沿着y轴做旋转
                        int base = 60;
                        ViewCompat.setPivotX(menuView, menuView.getWidth() / 3);
                        ViewCompat.setRotationY(menuView, -progress * (90 - base) + (90 - base));
                        break;
                    case R.id.contentScale:
                        // 内容视图做缩放动画
                        ViewCompat.setPivotX(mContent, mContent.getWidth() / 2);
                        ViewCompat.setPivotY(mContent, mContent.getHeight() / 2);
                        ViewCompat.setScaleX(mContent, 1 - progress / 4);
                        ViewCompat.setScaleY(mContent, 1 - progress / 4);
                        break;
                    case R.id.drawerLayout:
                        //将菜单栏上移至内容视图上层，实现DrawerLayout 效果
                        ViewCompat.setTranslationZ(menuView, 1);
                        float space = LiteMenuHelper.getScreenWidth(mContext) * 0.8f;
                        //内容视图反向滑动，抵消整个控件的滑动，使得内容视图看起来保持不同，而菜单栏滑动
                        ViewCompat.setTranslationX(mContent, scrollX - space);
                        ViewCompat.setTranslationX(menuView, -scrollX);
                        break;
                    default:
                        break;
                }


            }
        });

        mAnimations.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mToolbar.setBackgroundResource(R.color.colorPrimary);
                if (checkedId == R.id.drawerLayout) {
                    ViewCompat.setTranslationZ(menuView, 1);
                } else {
                    ViewCompat.setTranslationZ(menuView, 0);
                }

            }
        });


    }

    @OnClick({R.id.avatar, R.id.smallAvatar})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                startActivity(new Intent(this, PullRecyclerViewActivity.class));
                break;
            case R.id.smallAvatar:

                mMSwipeMenu.openMenu();
                break;
        }

    }

}
