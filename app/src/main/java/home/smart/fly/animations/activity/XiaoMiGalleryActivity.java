package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.gallery.DrayViewLayout;

public class XiaoMiGalleryActivity extends AppCompatActivity {


    ImageView ImageView;
    TextView mTop;
    DrayViewLayout mScaleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao_mi_gallery);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_xiao_mi_gallery);


        mScaleLayout = (DrayViewLayout) findViewById(R.id.scale_layout);
//        mScaleLayout.setSuggestScaleEnable(true);
        ImageView = (ImageView) findViewById(R.id.scaleLayout_center);
        mScaleLayout.setOnGetCanScaleListener(new DrayViewLayout.OnGetCanScaleListener() {
            @Override
            public boolean onGetCanScale(boolean isScrollDown) {
//                return !touchImageView.isZoomed();

                return true;
            }
        });

        mTop = (TextView) findViewById(R.id.scaleLayout_top);
        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScaleLayout.setState(DrayViewLayout.STATE_CLOSE);
            }
        });
    }

}
