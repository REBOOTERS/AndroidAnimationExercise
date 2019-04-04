package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import home.smart.fly.animations.R;

public class PhotoProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_process);
        String uri = getIntent().getStringExtra("url");
        ImageView image = (ImageView) findViewById(R.id.image);
        Glide.with(this).load(uri).into(image);
    }
}
