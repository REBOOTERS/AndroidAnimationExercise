package home.smart.fly.animations.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.ExpandingCircleAnimationDrawable;

public class AnimateDrawableActivity extends AppCompatActivity {
    private ExpandingCircleAnimationDrawable circleAnimationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_drawable);
        ImageView imageView = findViewById(R.id.imageView);

        circleAnimationDrawable = new ExpandingCircleAnimationDrawable(200);
//        imageView.setImageDrawable(circleAnimationDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        circleAnimationDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        circleAnimationDrawable.stop();
    }
}
