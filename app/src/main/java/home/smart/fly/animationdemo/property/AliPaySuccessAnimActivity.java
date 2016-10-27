package home.smart.fly.animationdemo.property;

import android.graphics.Canvas;
import android.os.Bundle;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.AlipayFailureView;
import home.smart.fly.animationdemo.property.basic.AlipaySuccessView;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by rookie on 2016/10/19.
 */

public class AliPaySuccessAnimActivity extends BaseActivity {
    private AlipaySuccessView alipayview;
    private AlipayFailureView alipayFailureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);

        Canvas canvas;

    }

    @Override
    public void initView() {
        alipayview = (AlipaySuccessView) findViewById(R.id.successview);
        alipayview.loadCircle();
        alipayFailureView = (AlipayFailureView) findViewById(R.id.faliureview);
        alipayFailureView.loadCircle();
    }
}
