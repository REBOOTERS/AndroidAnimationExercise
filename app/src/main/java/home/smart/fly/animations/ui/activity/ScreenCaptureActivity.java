package home.smart.fly.animations.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.permissionx.guolindev.PermissionX;
import com.yhao.floatwindow.PermissionListener;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.FileUtil;
import home.smart.fly.animations.utils.GenBitmapDelegate;
import home.smart.fly.animations.utils.SysUtil;
import home.smart.fly.animations.widget.DrawingThread;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

//import com.muddzdev.pixelshot.PixelShot;

public class ScreenCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ScreenCaptureActivity";

    private ImageView ivScreenshot;
    private Disposable mDisposable;
    private Context mContext;

    private CheckBox mCheckBox;
    private Disposable timer;

    private GenBitmapDelegate genBitmapDelegate;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genBitmapDelegate = new GenBitmapDelegate(this);
        mContext = this;
        setContentView(R.layout.activity_screen_capture);
        ivScreenshot = findViewById(R.id.ivScreenshot);
        mCheckBox = findViewById(R.id.checkbox);
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ivScreenshot.setVisibility(View.VISIBLE);
            } else {
                ivScreenshot.setVisibility(View.GONE);
            }
        });

        setUpSurfaceView();

        findViewById(R.id.getScreen).setOnClickListener(v -> takeScreenshot());

        findViewById(R.id.getScreenPixel).setOnClickListener(v -> takeScreenShotWithPixel());

        findViewById(R.id.getScreenShot).setOnClickListener(v ->
                takScreenShotForInvisibleView(R.layout.activity_screen_capture));

        findViewById(R.id.getSurfaceView).setOnClickListener(v -> taskSurfaceView());

        findViewById(R.id.assemble).setOnClickListener(v -> assemble());
    }

    /**
     * SurfaceView 的截图和普通 view 截图的合成
     */
    private void assemble() {
        View viewRoot = getWindow().getDecorView().getRootView();
        Bitmap background = genBitmapDelegate.getBitmapByDrawCache(viewRoot);


        int[] position = new int[2];
        surfaceView.getLocationInWindow(position);

        if (SysUtil.Android8OrLater()) {
            genBitmapDelegate.getBitmapFromSurfaceView(surfaceView, foreground -> {
                Bitmap bitmap = genBitmapDelegate.composeBitmap(background, foreground, position[0], position[1]);
                saveBitmap(bitmap);
                return Unit.INSTANCE;
            });
        } else {
            Toast.makeText(mContext, "不支持截图", Toast.LENGTH_SHORT).show();
        }
    }

    private void taskSurfaceView() {
        if (SysUtil.Android8OrLater()) {
            genBitmapDelegate.getBitmapFromSurfaceView(surfaceView, bitmap -> {
                saveBitmap(bitmap);
                return Unit.INSTANCE;
            });
        } else {
            Toast.makeText(mContext, "不支持截图", Toast.LENGTH_SHORT).show();
        }
    }

    private DrawingThread mDrawingThread;

    // <editor-fold defaultstate="collapsed" desc="surface View">
    private void setUpSurfaceView() {
        surfaceView = findViewById(R.id.surface_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mDrawingThread = new DrawingThread(mContext, surfaceHolder);
                mDrawingThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mDrawingThread.quit();
            }
        });
    }
    // </editor-fold>

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
//        PixelShot.of(viewRoot)
//                .setFilename("screen" + System.currentTimeMillis())
//                .setResultListener(new PixelShot.PixelShotListener() {
//                    @SuppressLint("CheckResult")
//                    @Override
//                    public void onPixelShotSuccess(String path) {
//                        Toast.makeText(mContext, path, Toast.LENGTH_SHORT).show();
//
//                        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_floating_window, null);
//                        ImageView imageView = view.findViewById(R.id.target_view);
//                        Glide.with(mContext).load(path).into(imageView);
//                        view.setOnClickListener(v -> {
//                            FloatWindow.get().hide();
//                            FloatWindow.destroy();
//                            if (timer != null && timer.isDisposed()) {
//                                timer.dispose();
//                            }
//                            Intent mIntent = new Intent();
//                            mIntent.setAction(Intent.ACTION_VIEW);
//                            Uri contentUri;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                // 将文件转换成content://Uri的形式
//                                contentUri = FileProvider.getUriForFile(mContext, getPackageName() + ".fileprovider", new File(path));
//                                // 申请临时访问权限
//                                mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                            } else {
//                                contentUri = Uri.fromFile(new File(path));
//                            }
//
//                            mIntent.setDataAndType(contentUri, "image/*");
//                            startActivity(mIntent);
//                        });
//
//
//                        FloatWindow
//                                .with(getApplicationContext())
//                                .setView(view)
//                                .setWidth(Screen.width, 0.3f)
//                                .setHeight(Screen.width, 0.4f)
//                                .setX(Screen.width, 0.1f)
//                                .setY(Screen.height, 0.7f)
//                                .setMoveType(MoveType.inactive)
//                                .setFilter(true, ScreenCaptureActivity.class)
//                                .setPermissionListener(mPermissionListener)
//                                .setDesktopShow(false)
//                                .build();
//
//                        FloatWindow.get().show();
//
//                        timer = Observable.timer(3, TimeUnit.SECONDS)
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(aLong ->
//                                        {
//                                            Log.e(TAG, "accept: aLong==" + aLong);
//                                            FloatWindow.get().hide();
//                                            FloatWindow.destroy();
//                                        },
//                                        throwable -> {
//
//                                        });
//
//
//                    }
//
//                    @Override
//                    public void onPixelShotFailed() {
//                        Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
//                    }
//                }).save();
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
        Bitmap bitmap = genBitmapDelegate.getBitmapByDrawCache(viewRoot);
        saveBitmap(bitmap);

    }

    private void saveBitmap(Bitmap bitmap) {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((allGranted, grantedList, deniedList) -> {
                    String path = FileUtil.savaBitmap2SDcard(ScreenCaptureActivity.this, bitmap, "myfile");
                    if (!TextUtils.isEmpty(path)) {
                        Toast.makeText(ScreenCaptureActivity.this, "success path is " + path, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ScreenCaptureActivity.this, FullscreenActivity.class);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ScreenCaptureActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }

//        FloatWindow.destroy();
    }
}
