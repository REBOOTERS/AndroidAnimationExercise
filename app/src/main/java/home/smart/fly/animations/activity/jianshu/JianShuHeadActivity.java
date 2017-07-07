package home.smart.fly.animations.activity.jianshu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.ColorAnimator;


public class JianShuHeadActivity extends AppCompatActivity {
    private static final String TAG = "JianShuHeadActivity";
    @BindView(R.id.searchShell)
    LinearLayout mSearchShell;
    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.image)
    ImageView mImage;


    @BindView(R.id.head)
    RelativeLayout mHead;
    int lastColor = Color.TRANSPARENT;
    @BindView(R.id.searchTv)
    TextView mSearchTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jian_shu_head);
        ButterKnife.bind(this);
        MIUISetStatusBarLightMode(getWindow(), true);
//        StatusBarUtil.setColor(this, Color.BLACK, 0);

        final ColorAnimator mColorAnimator = new ColorAnimator(Color.TRANSPARENT, Color.WHITE);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float fraction = (float) 2 * scrollY / (mImage.getHeight());
                int color = mColorAnimator.getFractionColor(fraction);
                if (color != lastColor) {
                    lastColor = color;
                    mHead.setBackgroundColor(color);
//                    StatusBarUtil.addStatusBar(JianShuHeadActivity.this, color, 0);
                }

                Log.e(TAG, "onScrollChange: fraction=" + fraction);

                if (fraction >= 0.7f) {
                    mSearchTv.setText("搜索简书的内容和朋友");
                    ViewGroup.LayoutParams mParams = mSearchShell.getLayoutParams();
                    mParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    mSearchShell.setLayoutParams(mParams);
                    TransitionManager.beginDelayedTransition(mSearchShell);
                }

                if (fraction <= 0.3f) {
                    mSearchTv.setText("搜索");
                    ViewGroup.LayoutParams mParams = mSearchShell.getLayoutParams();
                    mParams.width = RecyclerView.LayoutParams.WRAP_CONTENT;
                    mSearchShell.setLayoutParams(mParams);
                    TransitionManager.beginDelayedTransition(mSearchShell);
                }
            }
        });

    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

}
