package home.smart.fly.animations.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.zhihu.matisse.internal.utils.PathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import home.smart.fly.animations.R;
import home.smart.fly.animations.internal.loader.AlbumMediaLoader;
import home.smart.fly.animations.internal.loader.GalleryPhoto;
import home.smart.fly.animations.utils.SysUtil;

public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "LoaderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaderctivity);
        setSupportActionBar(findViewById(R.id.toolbar));

        handlePermission();
    }

    private void handlePermission() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        ActivityResultLauncher<String[]> launcher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), o -> {
            boolean allDone = true;

            for (Boolean value : o.values()) {
                if (!value) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                LoaderManager.getInstance(LoaderActivity.this).initLoader(101, null, LoaderActivity.this);
            }
        });
        if (PermissionChecker.checkSelfPermission(this, Arrays.toString(permissions)) == PermissionChecker.PERMISSION_GRANTED) {
            LoaderManager.getInstance(LoaderActivity.this).initLoader(101, null, LoaderActivity.this);
        } else {
            launcher.launch(permissions);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return AlbumMediaLoader.newInstance(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ArrayList<Uri> uriList = new ArrayList<>();
        if (data.moveToFirst()) {
            do {
                Uri uri = getUri(data);
                uriList.add(uri);
            } while (data.moveToNext());
        }
        Log.d(TAG, "total = " + uriList.size());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @SuppressLint("Range")
    private Uri getUri(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
        String size = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
        String dateAdd = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        String path = PathUtils.getPath(this, uri);
        GalleryPhoto photo = new GalleryPhoto(uri, path, size, mimeType, SysUtil.timeStampToDate(dateAdd));
        Log.d(TAG, "photo " + photo);
        return uri;
    }
}
