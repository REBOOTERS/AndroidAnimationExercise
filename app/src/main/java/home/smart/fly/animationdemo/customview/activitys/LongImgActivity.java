package home.smart.fly.animationdemo.customview.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.IOException;
import java.io.InputStream;

import home.smart.fly.animationdemo.R;



public class LongImgActivity extends AppCompatActivity {
    private static final String TAG = "LongImgActivity";
    private static final float M_RATE = 1024 * 1024;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_larger_img);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.raw.story));

    }

    private void loadLargeImg() {
        InputStream is = getResources().openRawResource(R.raw.story);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeStream(is, null, options);
        int imgWidth = options.outWidth;
        int imgHeight = options.outHeight;
        options.inJustDecodeBounds = false;

        try {
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(is, false);

            Rect rect = new Rect(0, 0, imgWidth, imgHeight / 2);
            Bitmap bitmap = regionDecoder.decodeRegion(rect, options);
            Log.e(TAG, "Momory count: " + bitmap.getByteCount() / M_RATE + "M");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
