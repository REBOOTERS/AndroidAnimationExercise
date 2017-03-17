package home.smart.fly.animationdemo.customview.jianshu;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.jianshu.helper.FakeWebView;
import home.smart.fly.animationdemo.customview.jianshu.helper.HtmlBean;
import home.smart.fly.animationdemo.utils.FileUtil;
import home.smart.fly.animationdemo.utils.T;

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
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(this);
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
            case R.id.home:
                finish();
                break;
            case R.id.save:
                Bitmap bitmap = mFakeWebView.getScreenView();
                new MyAsyncTask().execute(bitmap);
                break;
            default:
                break;
        }
    }

    class MyAsyncTask extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("保存中...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Bitmap... params) {
            FileUtil.savaBitmap2SDcard(mContext, params[0], bean.getTitle());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            T.showSToast(mContext, "保存图片成功!");
        }
    }


}
