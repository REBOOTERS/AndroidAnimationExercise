package home.smart.fly.animationdemo.customview.jianshu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.jianshu.helper.FakeWebView;
import home.smart.fly.animationdemo.customview.jianshu.helper.HtmlBean;
import home.smart.fly.animationdemo.utils.FileUtil;

public class GenCaptureActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SAVE_PIC_PATH = Environment.getExternalStorageState()
            .equalsIgnoreCase(Environment.MEDIA_MOUNTED) ?
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera" : "";
    private Context mContext;
    FakeWebView mFakeWebView;
    private HtmlBean bean;


    //保存
    private static final int SAVE = 0;
    //分享
    private static final int SHARE = 1;
    //操作失败
    private static final int FAILE = 2;
    ProgressDialog pd;
    private Handler mHandler = new Handler_Capture(this);

    class Handler_Capture extends Handler {
        WeakReference<Activity> weakReference;

        public Handler_Capture(Activity activity) {
            weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                Bundle data = null;
                switch (msg.what) {
                    case SAVE:
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "图片已保存在相册!", Toast.LENGTH_LONG).show();
                        break;
                    case FAILE:
                        String strData = (String) msg.obj;
                        Toast.makeText(getApplicationContext(), strData, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_gen_capture);
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
                pd = new ProgressDialog(this);
                pd.setMessage("请稍后...");
                pd.show();
                Bitmap bitmap = mFakeWebView.getScreenView();
                FileUtil.savaBitmap2SDcard(mContext, bitmap, bean.getTitle());
//                save();
                break;
            default:
                break;
        }
    }

    private void save() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
                    final File realFile = saveBitmap(sdf.format(new Date()) + ".jpg");
                    if (realFile == null) {
                        Message message = mHandler.obtainMessage(FAILE);
                        message.obj = "保存失败,文件过大!";
                        message.sendToTarget();
                    } else {
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(GenCaptureActivity.this.getContentResolver(),
                                    realFile.getAbsolutePath(), realFile.getName(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
//                        realFile.delete();

                        Message message = mHandler.obtainMessage(SAVE);
                        Bundle data = new Bundle();
                        data.putString("path", realFile.getAbsolutePath());
                        message.setData(data);
                        message.sendToTarget();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    Message message = mHandler.obtainMessage(FAILE);
                    message.obj = "保存失败," + e.getMessage();
                    message.sendToTarget();
                } finally {
                    pd.dismiss();
                }
            }
        }).start();
    }


    /**
     * 保存图片到文件
     *
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
    private File saveBitmap(String fileName) throws Exception {
//        Bitmap bitmap = mFakeWebView.getScreen();
        Bitmap bitmap = mFakeWebView.getScreenView();
        if (bitmap == null)
            return null;
        File file = new File(SAVE_PIC_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        final File realFile = new File(file, fileName);
        if (!realFile.exists()) {
            realFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(realFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc(); // 通知系统回收
        }
        return realFile;
    }
}
