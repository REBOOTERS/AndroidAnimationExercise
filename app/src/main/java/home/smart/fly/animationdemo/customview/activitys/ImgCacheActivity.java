package home.smart.fly.animationdemo.customview.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.V;

public class ImgCacheActivity extends AppCompatActivity {

    private static final String IMG_URL = "https://timgsa.baidu.com/timg?image&quality=80&size" +
            "=b9999_10000&sec=1488975943578&di=e8f47f35afd64be8d2d36ab5d6627928&imgtype=0&src=" +
            "http%3A%2F%2Ftupian.enterdesk.com%2F2013%2Fxll%2F011%2F13%2F2%2F7.jpg";

    private ImageView img1, img2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_cache);
        initView();
    }

    private void initView() {
        img1 = V.f(this, R.id.img1);
        img2 = V.f(this, R.id.img2);
        Bitmap bitmap = BitmapCacheUtil.getBitmapFromMemCache(IMG_URL);
        if (bitmap != null) {
            img1.setImageBitmap(bitmap);
        } else {
            new ImageLoadTask().execute(IMG_URL);
        }

        Glide.with(this)
                .load(IMG_URL)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img2);
    }

    private class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img1.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                BitmapCacheUtil.addBitmapToMemoryCache(params[0], bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
