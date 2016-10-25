package home.smart.fly.animationdemo.property;

import android.os.Bundle;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.AlipaySuccessView;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by rookie on 2016/10/19.
 */

public class AliPaySuccessAnimActivity extends BaseActivity {
    private AlipaySuccessView alipayview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);

    }

    @Override
    public void initView() {
        alipayview = (AlipaySuccessView) findViewById(R.id.alipayview);
        alipayview.loadCircle();
    }
}
