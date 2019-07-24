package home.smart.fly.animations.ui.activity.jianshu;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.io.File;

import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.jianshu.helper.FakeWebView;
import home.smart.fly.animations.ui.activity.jianshu.helper.HtmlBean;
import home.smart.fly.animations.utils.FileUtil;
import home.smart.fly.animations.utils.NotificationHelper;
import home.smart.fly.animations.utils.TT;

public class GenScreenShotActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    FakeWebView mFakeWebView;
    private HtmlBean bean;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_gen_screen_shot);
        initView();
    }

    private void initView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        LinearLayout sava = (LinearLayout) findViewById(R.id.save);
        sava.setOnClickListener(this);
        mFakeWebView = (FakeWebView) findViewById(R.id.fakeWebView);
        bean = (HtmlBean) getIntent().getSerializableExtra("data");
        RadioGroup changeMode = (RadioGroup) findViewById(R.id.changeMode);
        changeMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_day) {
                    mFakeWebView.setMode(FakeWebView.MODE_DAY);
                } else {
                    mFakeWebView.setMode(FakeWebView.MODE_NIGHT);
                }
            }
        });
        mFakeWebView.loadData(bean);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("保存中...");
                mProgressDialog.show();
                Bitmap bitmap = mFakeWebView.getScreenView();
                new MyAsyncTask().execute(bitmap);
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAsyncTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {
            return FileUtil.savaBitmap2SDcard(mContext, params[0], bean.getTitle());
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            mProgressDialog.dismiss();


            if (!TextUtils.isEmpty(path)) {

                TT.showSToast(mContext, path);


                Intent mIntent = new Intent();
                mIntent.setAction(android.content.Intent.ACTION_VIEW);
                Uri contentUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 将文件转换成content://Uri的形式
                    contentUri = FileProvider.getUriForFile(mContext, getPackageName() + ".fileprovider", new File(path));
                    // 申请临时访问权限
                    mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    contentUri = Uri.fromFile(new File(path));
                }

                mIntent.setDataAndType(contentUri, "image/*");
//                startActivity(mIntent);
                PendingIntent mPendingIntent = PendingIntent.getActivity(mContext
                        , 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                new NotificationHelper(mContext).createNotification("点击查看", "图片保存在: " + path, mPendingIntent);

            } else {
                TT.showSToast(mContext, "fail");
            }

        }
    }


}
