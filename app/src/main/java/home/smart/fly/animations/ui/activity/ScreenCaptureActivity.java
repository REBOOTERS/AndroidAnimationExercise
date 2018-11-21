package home.smart.fly.animations.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.muddzdev.pixelshot.PixelShot;
import com.tbruyelle.rxpermissions2.RxPermissions;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.FileUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ScreenCaptureActivity extends AppCompatActivity {
    private ImageView ivScreenshot;
    private Disposable mDisposable;
    private Context mContext;

    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_screen_capture);
        ivScreenshot = (ImageView) findViewById(R.id.ivScreenshot);
        findViewById(R.id.getScreen).setOnClickListener(v -> takeScreenshot());

        findViewById(R.id.getScreenPixel).setOnClickListener(v -> takeScreenShotWithPixel());

        findViewById(R.id.getScreenShot).setOnClickListener(v -> takScreenShotForInvisibleView(R.layout.activity_screen_capture));

        mCheckBox=findViewById(R.id.checkbox);
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
        final Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        final LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(layoutResId,null);
        layout.setDrawingCacheEnabled(true);
        layout.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.getWidth(),View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(),View.MeasureSpec.EXACTLY));
        layout.layout(0,0,layout.getMeasuredWidth(),layout.getMeasuredHeight());
        canvas.drawBitmap(layout.getDrawingCache(),0,0,new Paint());
        ivScreenshot.setImageBitmap(bmp);
//        return bmp;
    }

    private void takeScreenShotWithPixel() {
        View viewRoot = getWindow().getDecorView().getRootView();
        PixelShot.of(viewRoot)
                .setFilename("screen"+System.currentTimeMillis())
                .setResultListener(new PixelShot.PixelShotListener() {
            @Override
            public void onPixelShotSuccess(String path) {
                Toast.makeText(mContext, path, Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(ivScreenshot);
            }

            @Override
            public void onPixelShotFailed() {
                Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }).save();
    }

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
