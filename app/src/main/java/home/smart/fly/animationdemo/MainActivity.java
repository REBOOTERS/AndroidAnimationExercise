package home.smart.fly.animationdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import home.smart.fly.animationdemo.property.PropertyAnimationActivity;
import home.smart.fly.animationdemo.tradition.FrameAnimationActivity;
import home.smart.fly.animationdemo.tradition.TweenedAnimationActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Button frame, tweened, property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        frame = (Button) findViewById(R.id.frame);
        frame.setOnClickListener(this);
        tweened = (Button) findViewById(R.id.tween);
        tweened.setOnClickListener(this);
        property = (Button) findViewById(R.id.property);
        property.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.frame:
                intent = new Intent(mContext, FrameAnimationActivity.class);
                break;
            case R.id.tween:
                intent = new Intent(mContext, TweenedAnimationActivity.class);
                break;
            case R.id.property:
                intent = new Intent(mContext, PropertyAnimationActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
