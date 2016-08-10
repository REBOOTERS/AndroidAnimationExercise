package home.smart.fly.animationdemo.property;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.PointAnimView2;

/**
 * Created by co-mall on 2016/8/9.
 */
public class ValueAnimationActivity2 extends AppCompatActivity {
    PointAnimView2 view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animation2);
        view = (PointAnimView2) findViewById(R.id.view);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (view != null) {
            view.stopAnimation();
        }
    }
}
