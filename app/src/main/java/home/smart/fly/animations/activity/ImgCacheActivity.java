package home.smart.fly.animations.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.ImageBean;
import home.smart.fly.animations.utils.T;
import home.smart.fly.animations.utils.V;

public class ImgCacheActivity extends AppCompatActivity {

    private static final String IMG_URL = "https://timgsa.baidu.com/timg?image&quality=80&size" +
            "=b9999_10000&sec=1488975943578&di=e8f47f35afd64be8d2d36ab5d6627928&imgtype=0&src=" +
            "http%3A%2F%2Ftupian.enterdesk.com%2F2013%2Fxll%2F011%2F13%2F2%2F7.jpg";



    private ImageView img1, img2;
    private FloatingActionButton fab;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageDownLoadTask().execute(IMG_URL);
            }
        });
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


    private class ImageDownLoadTask extends AsyncTask<String, Void, ImageBean> {

        @Override
        protected void onPostExecute(ImageBean imageBean) {
            super.onPostExecute(imageBean);

            if (imageBean != null) {
                String filepath = imageBean.getFilepath();
                T.showSToast(mContext, filepath);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
                mBuilder.setWhen(System.currentTimeMillis())
                        .setTicker("下载图片成功")
                        .setContentTitle("点击查看")
                        .setSmallIcon(R.mipmap.app_start)
                        .setContentText("图片保存在:" + filepath)
                        .setAutoCancel(true)
                        .setOngoing(false);
                //通知默认的声音 震动 呼吸灯
                mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
                Intent mIntent = new Intent(mContext, PendingImgActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("bean", imageBean);
                mIntent.putExtras(mBundle);
                PendingIntent mPendingIntent = PendingIntent.getActivity(mContext
                        , 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(mPendingIntent);
                Notification mNotification = mBuilder.build();
                mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mManager.notify(0, mNotification);
            } else {
                T.showSToast(mContext, "fail");
            }
        }

        @Override
        protected ImageBean doInBackground(String... params) {

            try {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
                String fileName = "IMG_" + timeStamp + ".jpg";
                File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + File.separator + fileName);
                OutputStream mOutputStream = new FileOutputStream(localFile);
                URL url = new URL(params[0]);
                HttpURLConnection mConnection = (HttpURLConnection) url.openConnection();
                InputStream mInputStream = mConnection.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                while ((len = mInputStream.read(buffer)) != -1) {
                    mOutputStream.write(buffer, 0, len);
                }
                mOutputStream.flush();
                mInputStream.close();
                mOutputStream.close();

                ImageBean mImageBean = new ImageBean();
                mImageBean.setFilepath(localFile.getAbsolutePath());
                mImageBean.setLongitude(116.400819);
                mImageBean.setLatitude(39.916263);
                return mImageBean;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
