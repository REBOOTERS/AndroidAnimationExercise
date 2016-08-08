package home.smart.fly.animationdemo.tradition;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import home.smart.fly.animationdemo.R;

/**
 * Created by co-mall on 2016/8/8.
 */
public class FrameAnimationActivity extends AppCompatActivity {
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
