package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import home.smart.fly.animations.R;


public class LongImgActivity extends AppCompatActivity {
    private static final String TAG = "LongImgActivity";
    private static final float M_RATE = 1024 * 1024;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_larger_img);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.raw.story));
        Test();
    }

    private void Test() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = format.format(date);

        Log.e(TAG, "Test: " + timeStr);

        try {
            Date newDate = format.parse("2017-03-12 11:17:33");

            double result = -(newDate.getTime() - date.getTime()) / (1000 * 24 * 3600.0f);

            Log.e(TAG, "Test: " + result);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
