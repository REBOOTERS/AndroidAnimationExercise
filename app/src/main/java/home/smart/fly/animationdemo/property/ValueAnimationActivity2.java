package home.smart.fly.animationdemo.property;

import android.os.Bundle;
import android.support.annotation.Nullable;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.PointAnimView2;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by co-mall on 2016/8/9.
 */
public class ValueAnimationActivity2 extends BaseActivity {
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
