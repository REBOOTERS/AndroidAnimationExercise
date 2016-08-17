package home.smart.fly.animationdemo.tradition;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by co-mall on 2016/8/8.
 */
public class FrameAnimationActivity extends BaseActivity {
    private ImageView animationImg;
    //
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
        animationImg = (ImageView) findViewById(R.id.animation);
        animationImg.setImageResource(R.drawable.frame_anim);
        animationDrawable = (AnimationDrawable) animationImg.getDrawable();
        animationDrawable.start();
    }
}
