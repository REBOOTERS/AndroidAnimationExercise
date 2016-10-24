package home.smart.fly.animationdemo.property;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.PointAnimView;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by co-mall on 2016/8/9.
 */
public class ValueAnimationActivity extends BaseActivity implements View.OnClickListener {
    PointAnimView view;
    //
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animation);

    }

    @Override
    public void initView() {
        view = (PointAnimView) findViewById(R.id.view);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);
        rb6 = (RadioButton) findViewById(R.id.rb6);
        rb7 = (RadioButton) findViewById(R.id.rb7);
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (view != null) {
//            view.pauseAnimation();
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (view != null) {
            view.stopAnimation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                if (view != null) {
                    if (rb1.isChecked()) {
                        type = 1;
                    } else if (rb2.isChecked()) {
                        type = 2;
                    } else if (rb3.isChecked()) {
                        type = 3;
                    } else if (rb4.isChecked()) {
                        type = 4;
                    } else if (rb5.isChecked()) {
                        type = 5;
                    } else if (rb6.isChecked()) {
                        type = 6;
                    } else if (rb7.isChecked()) {
                        type = 7;
                    } else {
                        type = 0;
                    }
                    view.stopAnimation();
                    view.setInterpolatorType(type);
                    view.StartAnimation();
                }
                break;
            case R.id.stop:
                if (view != null) {
                    view.stopAnimation();
                }
                break;
            default:
                break;
        }
    }


}
