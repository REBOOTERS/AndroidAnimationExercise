package home.smart.fly.animations.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.muddzdev.pixelshot.PixelShot;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;

import java.io.File;
import java.util.concurrent.TimeUnit;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.FileUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ScreenCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ScreenCaptureActivity";

    private ImageView ivScreenshot;
    private Disposable mDisposable;
    private Context mContext;

    private CheckBox mCheckBox;
    private Disposable timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_screen_capture);
        ivScreenshot = (ImageView) findViewById(R.id.ivScreenshot);
        findViewById(R.id.getScreen).setOnClickListener(v -> takeScreenshot());

        findViewById(R.id.getScreenPixel).setOnClickListener(v -> takeScreenShotWithPixel());

        findViewById(R.id.getScreenShot).setOnClickListener(v -> takScreenShotForInvisibleView(R.layout.activity_screen_capture));

        mCheckBox = findViewById(R.id.checkbox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ivScreenshot.setVisibility(View.VISIBLE);
                } else {
                    ivScreenshot.setVisibility(View.GONE);
                }
            }
        });
    }

    private void takScreenShotForInvisibleView(int layoutResId) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        final LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(layoutResId, null);
        layout.setDrawingCacheEnabled(true);
        layout.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY));
        layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());
        canvas.drawBitmap(layout.getDrawingCache(), 0, 0, new Paint());
        ivScreenshot.setImageBitmap(bmp);
//        return bmp;
    }

    private void takeScreenShotWithPixel() {
        View viewRoot = getWindow().getDecorView().getRootView();
        PixelShot.of(viewRoot)
                .setFilename("screen" + System.currentTimeMillis())
                .setResultListener(new PixelShot.PixelShotListener() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onPixelShotSuccess(String path) {
                        Toast.makeText(mContext, path, Toast.LENGTH_SHORT).show();

                        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_floating_window, null);
                        ImageView imageView = view.findViewById(R.id.target_view);
                        Glide.with(mContext).load(path).into(imageView);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FloatWindow.get().hide();
                                FloatWindow.destroy();
                                if (timer != null && timer.isDisposed()) {
                                    timer.dispose();
                                }
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
                                startActivity(mIntent);
                            }
                        });


                        FloatWindow
                                .with(getApplicationContext())
                                .setView(view)
                                .setWidth(Screen.width, 0.3f)
                                .setHeight(Screen.width, 0.4f)
                                .setX(Screen.width, 0.1f)
                                .setY(Screen.height, 0.7f)
                                .setMoveType(MoveType.inactive)
                                .setFilter(true, ScreenCaptureActivity.class)
                                .setPermissionListener(mPermissionListener)
                                .setDesktopShow(false)
                                .build();

                        FloatWindow.get().show();

                        timer = Observable.timer(3, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong ->
                                        {
                                            Log.e(TAG, "accept: aLong==" + aLong);
                                            FloatWindow.get().hide();
                                            FloatWindow.destroy();
                                        },
                                        throwable -> {

                                        });


                    }

                    @Override
                    public void onPixelShotFailed() {
                        Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }).save();
    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };

    public void takeScreenshot() {
        View viewRoot = getWindow().getDecorView().getRootView();
        viewRoot.setDrawingCacheEnabled(true);
        Bitmap temp = viewRoot.getDrawingCache();
        ScreenParam screenInfo = getScreenInfo();
        int statusBarHeight = getStatusBarHeight();
        Bitmap bitmap = Bitmap.createBitmap(temp, 0, statusBarHeight, screenInfo.width, screenInfo.height - statusBarHeight);
        viewRoot.setDrawingCacheEnabled(false);

        RxPermissions rxPermissions = new RxPermissions(this);
        mDisposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (!TextUtils.isEmpty(FileUtil.savaBitmap2SDcard(ScreenCaptureActivity.this, bitmap, "myfile"))) {
                            Toast.makeText(ScreenCaptureActivity.this, "success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScreenCaptureActivity.this, FullscreenActivity.class));
                        } else {
                            Toast.makeText(ScreenCaptureActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Throwable::printStackTrace);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }

//        FloatWindow.destroy();
    }

    private int getStatusBarHeight() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    private ScreenParam getScreenInfo() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new ScreenParam(dm.widthPixels, dm.heightPixels);
    }

    private class ScreenParam {
        int width, height;

        ScreenParam(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
