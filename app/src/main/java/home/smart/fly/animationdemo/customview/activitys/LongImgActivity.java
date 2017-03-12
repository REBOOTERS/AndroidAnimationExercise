package home.smart.fly.animationdemo.customview.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.longimgview.LongImageView;

public class LongImgActivity extends AppCompatActivity {
    private static final String TAG = "LongImgActivity";
    private static final float M_RATE = 1024 * 1024;
    private ImageView img;
    private LongImageView mImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_larger_img);
//        img = (ImageView) findViewById(R.id.img);
//        loadLargeImg();
        mImg = (LongImageView) findViewById(R.id.img);
//        mImg.setInputStream(getResources().openRawResource(R.raw.story));
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("world.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImg.setInputStream(inputStream);
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
            img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
