package home.smart.fly.animations.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import home.smart.fly.animations.R;
import home.smart.fly.animations.internal.annotations.Cat;
import home.smart.fly.animations.utils.GifSizeFilter;
import home.smart.fly.animations.utils.Glide4Engine;
import home.smart.fly.animations.utils.Tools;


public class MatisseDemoActivity extends AppCompatActivity implements View.OnClickListener, BGAOnRVItemClickListener {
    private static final String TAG = "MatisseDemoActivity";

    private static final int REQUEST_CODE_CHOOSE = 23;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private MyAdapter mMyAdapter;
    private List<String> mStrings;

    @Override
    @Cat
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_demo);
        ButterKnife.bind(this);
        findViewById(R.id.zhihu).setOnClickListener(this);
        findViewById(R.id.dracula).setOnClickListener(this);

        mMyAdapter = new MyAdapter(mRecyclerView);
        mMyAdapter.setOnRVItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mMyAdapter);


    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent mIntent = new Intent(this, MatissePhotoActivity.class);
        mIntent.putExtra("url", mStrings.get(position));
        startActivity(mIntent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    private class MyAdapter extends BGARecyclerViewAdapter<String> {


        public MyAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.image_item);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, String model) {
            Glide.with(mContext).load(model).into(helper.getImageView(R.id.image));
        }
    }


    @Override
    public void onClick(final View v) {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
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
                                        .imageEngine(new Glide4Engine())
                                        .forResult(REQUEST_CODE_CHOOSE);
                                break;
                            case R.id.dracula:
                                Matisse.from(MatisseDemoActivity.this)
                                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
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
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> mUris = Matisse.obtainResult(data);
            mStrings = Matisse.obtainPathResult(data);

//            try {
//                ExifInterface mExifInterface = new ExifInterface(mStrings.get(0));
//                Log.e(TAG, "onActivityResult: " + mExifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            mMyAdapter.setData(mStrings);

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
