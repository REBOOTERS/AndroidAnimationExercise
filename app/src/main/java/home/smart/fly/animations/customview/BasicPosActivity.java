package home.smart.fly.animations.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.views.MyView;
import home.smart.fly.animations.utils.BaseActivity;
import home.smart.fly.animations.utils.V;

/**
 * Created by rookie on 2016/12/20.
 */

public class BasicPosActivity extends BaseActivity {
    private static final String TAG = "BasicPosActivity";
    private Context mContext;
    private MyView mMyView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_pos);
    }

    @Override
    public void initView() {
        mMyView = V.f(this, R.id.myView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int left = mMyView.getLeft();
        int right = mMyView.getRight();
        int top = mMyView.getTop();
        int bottom = mMyView.getBottom();

        Log.e(TAG, "onWindowFocusChanged: \n" +
                "left: " + left +
                "\nright: " + right +
                "\ntop: " + top +
                "\nbottom: " + bottom);
    }
}
