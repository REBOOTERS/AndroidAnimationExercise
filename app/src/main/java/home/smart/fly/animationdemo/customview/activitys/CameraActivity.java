package home.smart.fly.animationdemo.customview.activitys;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.T;
import home.smart.fly.animationdemo.utils.V;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private static final int REQUEST_CODE_TAKE_PIC_CAMERA = 100;
    private static final int REQUEST_CODE_CROP_PIC = 101;
    private static final int REQUEST_CODE_PICK_FROM_GALLEY = 102;
    private static final int M_RATE = 1024 * 1024;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_WRITE_STORAGE = 2;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //
    private ImageView mImageView;
    private AppCompatCheckBox edit, compress, insert;
    //压缩
    private AppCompatSeekBar compressSeekBar;
    private LinearLayout rateShell;
    private TextView rate;

    //裁剪
    private LinearLayout cropShell;
    private AppCompatSeekBar cropValue;
    private TextView crop;


    private TextView info;

    //
    private boolean needEdit;
    private boolean needCompress;
    private boolean insertToGallery;
    private int compressRate;
    private int cropTargetHeight;
    private int cropTargetWidth;
    private int destType = FileHelper.JPEG;
    private Uri imageUrl;
    private Uri copyUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_camera);
        InitView();
    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //创建一个临时文件夹存储拍摄的照片
        File file = FileHelper.createFileByType(mContext, destType, "test");
        imageUrl = Uri.fromFile(file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PIC_CAMERA);
        }
    }

    private void selectFromGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, REQUEST_CODE_PICK_FROM_GALLEY);

    }

    private void CropTheImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(imageUrl, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("outputX", cropTargetWidth);
        cropIntent.putExtra("outputY", cropTargetHeight);


        File copyFile = FileHelper.createFileByType(mContext, destType, String.valueOf(System.currentTimeMillis()));
        copyUrl = Uri.fromFile(copyFile);
        cropIntent.putExtra("output", copyUrl);

        startActivityForResult(cropIntent, REQUEST_CODE_CROP_PIC);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //Camera
            case REQUEST_CODE_TAKE_PIC_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (needEdit) {
                        CropTheImage();
                    } else {
                        ProcessResult(imageUrl);
                    }
                }
                break;
            //Crop
            case REQUEST_CODE_CROP_PIC:
                if (resultCode == Activity.RESULT_OK) {
                    ProcessResult(copyUrl);
                }
                break;
            //gallery
            case REQUEST_CODE_PICK_FROM_GALLEY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        ProcessResult(uri);
                    }
                }
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void ProcessResult(Uri destUrl) {
        Bitmap bitmap = BitmapFactory.decodeFile(FileHelper.stripFileProtocol(destUrl.toString()));
        if (bitmap != null) {

            //添加图片到相册
            if (insertToGallery) {
                Uri galleryUri = Uri.fromFile(new File(FileHelper.getPicutresPath(destType)));
                boolean result = FileHelper.copyResultToGalley(mContext, imageUrl, galleryUri);
                if (result) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(galleryUri);
                    sendBroadcast(mediaScanIntent);
                }
            }


            if (needCompress) {
                bitmap = getCompressedBitmap(bitmap);
            }
            mImageView.setImageBitmap(bitmap);
            float byteCount = bitmap.getByteCount() / M_RATE;
            float all = bitmap.getAllocationByteCount() / M_RATE;
            String result = "这张图片占用内存大小:\nbitmap.getByteCount()= " + byteCount + "M\n"
                    + "bitmap.getAllocationByteCount()= " + all + "M";
            info.setText(result);
            bitmap = null;
        } else {
            T.showLToast(mContext, "fail");
        }
    }


    private Bitmap getCompressedBitmap(Bitmap bitmap) {
        try {
            File compressedFile = FileHelper.createFileByType(mContext, destType, "compressed");
            Uri uri = Uri.fromFile(compressedFile);
            OutputStream os = getContentResolver().openOutputStream(uri);
            Bitmap.CompressFormat format = destType == FileHelper.JPEG ?
                    Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG;
            boolean success = bitmap.compress(format, compressRate, os);
            if (success) {
                T.showLToast(mContext, "success");
            }
            bitmap = BitmapFactory.decodeFile(FileHelper.stripFileProtocol(uri.toString()));
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                if (hasPermission(permissions)) {
                    mImageView.setImageDrawable(null);
                    openCamera();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, permissions, REQUEST_CAMERA);
                }
                break;
            case R.id.select:
                if (hasPermission(permissions)) {
                    mImageView.setImageDrawable(null);
                    selectFromGalley();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, permissions, REQUEST_WRITE_STORAGE);
                }
            default:
                break;
        }
    }


    private void InitView() {
        findViewById(R.id.open).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);
        mImageView = V.f(this, R.id.image);
        info = V.f(this, R.id.info);
        rate = V.f(this, R.id.rate);
        rateShell = V.f(this, R.id.rateShell);
        compressSeekBar = V.f(this, R.id.compressRate);
        edit = V.f(this, R.id.edit);
        compress = V.f(this, R.id.compress);
        insert = V.f(this, R.id.insert);
        cropShell = V.f(this, R.id.cropShell);
        cropValue = V.f(this, R.id.cropValue);
        crop = V.f(this, R.id.crop);

        compressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                compressRate = progress;
                rate.setText("quality: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cropValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cropTargetWidth = progress;
                cropTargetHeight = progress;

                crop.setText("width: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needEdit = isChecked;
                if (isChecked) {
                    cropShell.setVisibility(View.VISIBLE);
                    cropValue.setProgress(320);
                } else {
                    cropShell.setVisibility(View.GONE);
                }
            }
        });

        compress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needCompress = isChecked;
                if (isChecked) {
                    rateShell.setVisibility(View.VISIBLE);
                } else {
                    rateShell.setVisibility(View.GONE);
                }
            }
        });

        insert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                insertToGallery = isChecked;

            }
        });
    }
}
