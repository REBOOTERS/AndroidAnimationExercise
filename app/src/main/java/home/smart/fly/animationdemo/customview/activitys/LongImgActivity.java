package home.smart.fly.animationdemo.customview.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import home.smart.fly.animationdemo.R;


public class LongImgActivity extends AppCompatActivity {
    private static final String TAG = "LongImgActivity";
    private static final float M_RATE = 1024 * 1024;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_larger_img);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.raw.story));
    }

}
