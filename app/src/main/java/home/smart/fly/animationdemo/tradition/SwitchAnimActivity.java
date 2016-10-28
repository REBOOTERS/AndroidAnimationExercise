package home.smart.fly.animationdemo.tradition;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by rookie on 2016/10/28.
 */

public class SwitchAnimActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private Transition transition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_switch_anim);
    }

    @Override
    public void initView() {
        findViewById(R.id.exlpode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = null;
            switch (v.getId()) {
                case R.id.exlpode:
                    transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
                    break;
                default:
                    break;
            }
            getWindow().setEnterTransition(transition);
            bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Intent intent = new Intent(mContext, FrameAnimationActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}
