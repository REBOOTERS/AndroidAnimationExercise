package home.smart.fly.animations.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import home.smart.fly.animations.R;

public class WeiXinGalleryActivity extends AppCompatActivity {
    private static final String PIC_URL = "http://f.hiphotos.baidu.com/zhidao/pic/item/8b82b9014a90f60326b707453b12b31bb051eda9.jpg";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_wei_xin_gallery);
        final CircleImageView userAvatar = (CircleImageView) findViewById(R.id.userAvatar);
        final ImageView userAvatar1 = (ImageView) findViewById(R.id.userAvatar1);





    }
}
