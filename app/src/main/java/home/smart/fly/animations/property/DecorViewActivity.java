package home.smart.fly.animations.property;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import home.smart.fly.animations.R;

public class DecorViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor_view);


        ViewGroup DecorView = (ViewGroup) findViewById(android.R.id.content);
        ViewGroup subView = (ViewGroup) DecorView.getChildAt(0);
        ViewGroup rootView = (ViewGroup) findViewById(R.id.rootView);
        View subView1 = subView.getChildAt(0);
        View subView2 = subView.getChildAt(1);

        final ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(DecorView, "rotation", 0, 360);
        mObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mObjectAnimator.setDuration(1000);

        final ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(subView, "rotation", 0, 180);
        mObjectAnimator1.setRepeatMode(ObjectAnimator.RESTART);
        mObjectAnimator1.setDuration(500);

        final ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(subView1, "rotation", 0, 180);
        mObjectAnimator1.setRepeatMode(ObjectAnimator.RESTART);
        mObjectAnimator1.setDuration(500);

        final ObjectAnimator mObjectAnimator3 = ObjectAnimator.ofFloat(subView2, "rotation", 0, 180);
        mObjectAnimator1.setRepeatMode(ObjectAnimator.RESTART);
        mObjectAnimator1.setDuration(500);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObjectAnimator.start();
            }
        });

        findViewById(R.id.start1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObjectAnimator1.start();
            }
        });

    }
}
