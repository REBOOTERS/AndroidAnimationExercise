package home.smart.fly.animationdemo.property;

import android.app.Activity;
import android.os.Bundle;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.AlipaySuccessView;

/**
 * Created by co-mall on 2016/10/19.
 */

public class DemoViewActivity extends Activity {
    private AlipaySuccessView alipayview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);
        alipayview = (AlipaySuccessView) findViewById(R.id.alipayview);
        alipayview.loadCircle();
    }
}
