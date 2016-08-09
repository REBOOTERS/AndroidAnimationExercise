package home.smart.fly.animationdemo.property;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import home.smart.fly.animationdemo.R;

/**
 * Created by co-mall on 2016/8/9.
 */
public class ValueAnimationActivity extends AppCompatActivity {
    PointAnimView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animation);
        view = (PointAnimView) findViewById(R.id.view);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (view != null) {
            view.stopAnimation();
        }
    }
}
