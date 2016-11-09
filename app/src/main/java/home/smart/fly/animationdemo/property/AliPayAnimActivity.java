package home.smart.fly.animationdemo.property;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    private Button success, failure;
    private int radius1, radius2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);
    }

    @Override
    public void initView() {
        alipaySuccessView = (AlipaySuccessView) findViewById(R.id.successview);
        alipayFailureView = (AlipayFailureView) findViewById(R.id.faliureview);
        success = (Button) findViewById(R.id.success);
        failure = (Button) findViewById(R.id.failure);



        alipaySuccessView.addCircleAnimatorEndListner(new AlipaySuccessView.OnCircleFinishListener() {
            @Override
            public void onCircleDone() {
                success.setText("支付成功！");
                alipaySuccessView.setPaintColor(Color.GREEN);
            }
        });

        alipayFailureView.addCircleAnimatorEndListner(new AlipayFailureView.OnCircleFinishListener() {
            @Override
            public void onCircleDone() {
                failure.setText("支付失败！");
                alipayFailureView.setPaintColor(getResources().getColor(R.color.cpb_red));
            }
        });

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipaySuccessView.loadCircle(radius1 / 2);
            }
        });

        failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alipayFailureView.startAnim(radius2 / 2);
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Log.e("measure ", ".......>  " + alipaySuccessView.getWidth() + "  " + alipaySuccessView.getHeight());
            //在布局中取长宽中较小的值作为圆的半径
            radius1 = Math.min(alipaySuccessView.getWidth(), alipaySuccessView.getHeight());

            radius2 = Math.min(alipayFailureView.getWidth(), alipayFailureView.getHeight());

        }
    }
}
