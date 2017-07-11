package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.gallery.ScaleLayout;
import home.smart.fly.animations.customview.gallery.TouchImageView;

public class XiaoMiGalleryActivity extends AppCompatActivity {


    TouchImageView touchImageView;
    TextView mTop;
    ScaleLayout mScaleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao_mi_gallery);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_xiao_mi_gallery);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mScaleLayout = (ScaleLayout) findViewById(R.id.scale_layout);
        mScaleLayout.setSuggestScaleEnable(true);
        touchImageView = (TouchImageView) findViewById(R.id.scaleLayout_center);
        mScaleLayout.setOnGetCanScaleListener(new ScaleLayout.OnGetCanScaleListener() {
            @Override
            public boolean onGetCanScale(boolean isScrollDown) {
                return !touchImageView.isZoomed();
            }
        });

        mTop = (TextView) findViewById(R.id.scaleLayout_top);
        mTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScaleLayout.setState(ScaleLayout.STATE_CLOSE);
            }
        });
    }

}
