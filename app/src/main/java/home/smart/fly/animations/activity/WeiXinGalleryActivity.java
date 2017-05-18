package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import home.smart.fly.animations.R;

public class WeiXinGalleryActivity extends AppCompatActivity {
    private static final String PIC_URL="http://180.76.188.116/assets/upload/user/89bbc02d764d1e2b2103d4e6dba8e0e2.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xin_gallery);
        final CircleImageView userAvatar = (CircleImageView) findViewById(R.id.userAvatar);
        final ImageView userAvatar1 = (ImageView) findViewById(R.id.userAvatar1);


        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(WeiXinGalleryActivity.this).load(PIC_URL).into(userAvatar);
//                Glide.with(WeiXinGalleryActivity.this).load(PIC_URL).into(userAvatar1);
            }
        });
    }
}
