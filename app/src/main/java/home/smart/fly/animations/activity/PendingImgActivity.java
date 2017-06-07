package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import home.smart.fly.animations.R;

public class PendingImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_img);
        ImageView mImageView = (ImageView) findViewById(R.id.img);
        if (getIntent() != null) {
            String url = getIntent().getStringExtra("imgUrl");
            Glide.with(this).load(url).into(mImageView);
        }
    }
}
