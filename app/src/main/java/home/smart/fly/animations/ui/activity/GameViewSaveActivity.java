package home.smart.fly.animations.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.NotificationHelper;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.T;

public class GameViewSaveActivity extends AppCompatActivity {

    @BindView(R.id.save)
    ImageView save;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.content)
    FrameLayout content;


    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.finish)
    TextView finish;

    private Context mContext;
    private String picUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_game_view_save);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.transblue), 0);

        if (getIntent() != null) {
            picUrl = getIntent().getStringExtra("picUrl");
        }

        Glide.with(mContext).load(picUrl).into(imageView);
    }

    @OnClick({R.id.back, R.id.finish, R.id.save, R.id.share})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.finish:
            case R.id.back:
                finish();
                break;
            case R.id.save:
                SaveAndNotify();
                break;
            case R.id.share:
                Toast.makeText(mContext, "╮(╯_╰)╭", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void SaveAndNotify() {
        if (!TextUtils.isEmpty(picUrl)) {
            T.showSToast(mContext, "图片保存在：" + picUrl);


            Intent mIntent = new Intent();
            mIntent.setAction(Intent.ACTION_VIEW);
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 将文件转换成content://Uri的形式
                contentUri = FileProvider.getUriForFile(mContext, getPackageName() + ".fileprovider", new File(picUrl));
                // 申请临时访问权限
                mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                contentUri = Uri.fromFile(new File(picUrl));
            }

            mIntent.setDataAndType(contentUri, "image/*");


            PendingIntent mPendingIntent = PendingIntent.getActivity(mContext
                    , 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            new NotificationHelper(mContext).createNotification("点击查看","图片保存在: + path",mPendingIntent);

        } else {
            T.showSToast(mContext, "图片保存失败");
        }
    }
}
