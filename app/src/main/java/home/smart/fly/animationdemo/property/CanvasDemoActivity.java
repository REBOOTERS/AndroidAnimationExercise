package home.smart.fly.animationdemo.property;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.basic.CanvasView;

/**
 * Created by co-mall on 2016/8/12.
 */
public class CanvasDemoActivity extends AppCompatActivity {
    private CanvasView anim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_demo);
        anim = (CanvasView) findViewById(R.id.anim);
        anim.startAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (anim != null) {
            anim.stopAnim();
        }
    }
}
