package home.smart.fly.animations.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.GifSizeFilter;
import home.smart.fly.animations.utils.Tools;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MatisseDemoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MatisseDemoActivity";

    private static final int REQUEST_CODE_CHOOSE = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_demo);
        findViewById(R.id.zhihu).setOnClickListener(this);
        findViewById(R.id.dracula).setOnClickListener(this);
        findViewById(R.id.custom).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            switch (v.getId()) {
                                case R.id.zhihu:
                                    Matisse.from(MatisseDemoActivity.this)
                                            .choose(MimeType.ofAll(), false)
                                            .countable(true)
                                            .capture(true)
                                            .captureStrategy(
                                                    new CaptureStrategy(true, getPackageName() + ".fileprovider"))
                                            .maxSelectable(9)
                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                            .gridExpectedSize(
                                                    getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                            .thumbnailScale(0.85f)
                                            .imageEngine(new GlideEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                    break;
                                case R.id.dracula:
                                    Matisse.from(MatisseDemoActivity.this)
                                            .choose(MimeType.of(MimeType.JPEG))
                                            .theme(R.style.Matisse_Dracula)
                                            .countable(false)
                                            .maxSelectable(1)
                                            .imageEngine(new PicassoEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                    break;
                                case R.id.custom:
                                    Matisse.from(MatisseDemoActivity.this)
                                            .choose(MimeType.ofImage())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            Toast.makeText(MatisseDemoActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> mUris = Matisse.obtainResult(data);
            List<String> mStrings = Matisse.obtainPathResult(data);

//            try {
//                ExifInterface mExifInterface = new ExifInterface(mStrings.get(0));
//                Log.e(TAG, "onActivityResult: " + mExifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Tools.getPhotoInfo(mStrings.get(0));


//            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));
//            UCrop.of(mUris.get(0), destinationUri)
//                    .withAspectRatio(16, 9)
//                    .withMaxResultSize(maxWidth, maxHeight)
//                    .start(this);
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Intent mIntent = new Intent(MatisseDemoActivity.this, PhotoProcessActivity.class);
            mIntent.putExtra("url", resultUri);
            startActivity(mIntent);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }


}
