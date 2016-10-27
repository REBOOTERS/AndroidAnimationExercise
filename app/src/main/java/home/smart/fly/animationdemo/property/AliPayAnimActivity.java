package home.smart.fly.animationdemo.property;

import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.TextView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.AlipayFailureView;
import home.smart.fly.animationdemo.property.basic.AlipaySuccessView;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by rookie on 2016/10/19.
 */

public class AliPayAnimActivity extends BaseActivity {
    private AlipaySuccessView alipaySuccessView;
    private AlipayFailureView alipayFailureView;

    private TextView success, failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);

        Canvas canvas;

    }

    @Override
    public void initView() {
        alipaySuccessView = (AlipaySuccessView) findViewById(R.id.successview);
        alipaySuccessView.loadCircle();
        alipayFailureView = (AlipayFailureView) findViewById(R.id.faliureview);
        alipayFailureView.loadCircle();
        success = (TextView) findViewById(R.id.success);
        failure = (TextView) findViewById(R.id.failure);

        alipaySuccessView.addCircleAnimatorEndListner(new AlipaySuccessView.OnDoneCircleAnimListner() {
            @Override
            public void onCircleDone() {
                success.setText("支付成功！");
            }
        });

        alipayFailureView.addCircleAnimatorEndListner(new AlipayFailureView.OnDoneCircleAnimListner() {
            @Override
            public void onCircleDone() {
                failure.setText("支付失败！");
            }
        });


    }
}
