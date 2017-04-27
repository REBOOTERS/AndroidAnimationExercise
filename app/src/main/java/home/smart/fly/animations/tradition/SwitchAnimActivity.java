package home.smart.fly.animations.tradition;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;

import home.smart.fly.animations.R;

/**
 * Created by rookie on 2016/10/28.
 */

public class SwitchAnimActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Transition transition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_switch_anim);
        initView();
    }

    public void initView() {
        findViewById(R.id.exlpode).setOnClickListener(this);
        findViewById(R.id.slide).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (v.getId()) {
                case R.id.exlpode:
                    transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
                    break;
                case R.id.slide:
                    transition = TransitionInflater.from(this).inflateTransition(R.transition.slide);
                    break;
                default:
                    break;
            }
            //退出时使用
            getWindow().setExitTransition(transition);
            //第一次进入时使用
            getWindow().setEnterTransition(transition);
            //再次进入时使用
            getWindow().setReenterTransition(transition);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Intent intent = new Intent(mContext, TweenedAnimationActivity.class);
            startActivity(intent,bundle);
        }

    }
}
