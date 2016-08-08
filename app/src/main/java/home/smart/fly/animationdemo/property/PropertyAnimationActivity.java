package home.smart.fly.animationdemo.property;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import home.smart.fly.animationdemo.R;

/**
 * Created by co-mall on 2016/8/8.
 */
public class PropertyAnimationActivity extends AppCompatActivity implements OnClickListener {
    private Context mContext;
    //
    private TextView myView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        findViewById(R.id.alpha).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.translate).setOnClickListener(this);
        findViewById(R.id.rotate).setOnClickListener(this);
        myView = (TextView) findViewById(R.id.myView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alpha:
                AlpahAnimation();
                break;
            case R.id.translate:
                TranslationAnimation();
                break;
            case R.id.scale:
//                ScaleAnimation();
                break;
            case R.id.rotate:
                RotateAnimation();
                break;
            default:
                break;
        }
    }

    private void TranslationAnimation() {
        float curX=myView.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(myView, "translationX", curX, curX + 100, curX + 500, curX + 900, -400,curX);
        anim.setDuration(1000);
        anim.start();
    }

    private void RotateAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(myView, "rotation", 0f, 360f);
        anim.setDuration(1000);
        anim.start();
    }

    private void AlpahAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(myView,"alpha", 1.0f, 0.8f, 0.6f, 0.4f, 0.2f, 0.0f);
        anim.setRepeatCount(-1);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setDuration(2000);
        anim.start();
    }
}
