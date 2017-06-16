package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.ImageBean;

public class PendingImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_img);
        ImageView mImageView = (ImageView) findViewById(R.id.img);
        if (getIntent() != null) {
            ImageBean mBean = (ImageBean) getIntent().getSerializableExtra("bean");
            String url = mBean.getFilepath();
            Glide.with(this).load(url).into(mImageView);
            Log.e("onCreate", "longitude=" + mBean.getLongitude());
            Log.e("onCreate", "latitude=" + mBean.getLatitude());
        }
    }
}
