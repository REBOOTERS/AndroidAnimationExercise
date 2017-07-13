package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import home.smart.fly.animations.R;

public class PendingImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_img);
        PhotoView mImageView = (PhotoView) findViewById(R.id.img);
        mImageView.setImageResource(R.drawable.name);
//        if (getIntent() != null) {
//            ImageBean mBean = (ImageBean) getIntent().getSerializableExtra("bean");
//            String url = mBean.getFilepath();
//            Glide.with(this).load(url).into(mImageView);
//            Log.e("onCreate", "longitude=" + mBean.getLongitude());
//            Log.e("onCreate", "latitude=" + mBean.getLatitude());
//        }
    }
}
