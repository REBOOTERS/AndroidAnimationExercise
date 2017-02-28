package home.smart.fly.animationdemo.customview.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.T;
import home.smart.fly.animationdemo.utils.V;


public class CameraActivity extends AppCompatActivity {
    private Context mContext;
    private static final int REQUEST_CODE_TAKE_PIC = 100;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_WRITE_STORAGE = 2;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //
    private ImageView mImageView;

    //
    private boolean needEdit;
    private int destType = FileHelper.JPEG;
    private Uri imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_camera);
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasPermission(permissions)) {
                    openCamera();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, permissions, REQUEST_CAMERA);
                }


            }
        });

        mImageView = V.f(this, R.id.image);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //创建一个临时文件夹存储拍摄的照片
        File file = FileHelper.createFileByType(mContext, destType, "test");
        imageUrl = Uri.fromFile(file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PIC:
                if (resultCode == Activity.RESULT_OK) {
                    //这样只是获取了拍照之后的Thumbnail
//                    Bundle bundle = data.getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    mImageView.setImageBitmap(bitmap);

                    //获取拍摄的图片
                    if (needEdit) {
                        //// TODO: 2017/2/28 cropCapture 
                    } else {
                        // TODO: 2017/2/28 getCaptureFile
                        Bitmap bitmap = BitmapFactory.decodeFile(imageUrl.toString());
                        if (bitmap != null) {
                            mImageView.setImageBitmap(bitmap);
                        } else {
                            T.showLToast(mContext, "fail");
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取Camera 返回的照片
     *
     * @param destType
     * @param data
     */
    private Bitmap processFromCamera(int destType, Intent data) {
        Bitmap bitmap = null;
        String sourcePath = FileHelper.stripFileProtocol(imageUrl.toString());
        try {
            InputStream inputStream = FileHelper.getInputStreamFromUriString(imageUrl.toString(), mContext);
            bitmap = BitmapFactory.decodeFile(imageUrl.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PermissionChecker.PERMISSION_DENIED) {
                        String reason = permissions[i] + " is denied !";
                        T.showLToast(mContext, reason);
                        return;
                    }
                }
                openCamera();
                break;
            default:
                break;
        }
    }

    private boolean hasPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PermissionChecker.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
