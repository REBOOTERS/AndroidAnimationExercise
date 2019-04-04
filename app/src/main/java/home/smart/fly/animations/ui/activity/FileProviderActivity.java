package home.smart.fly.animations.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import home.smart.fly.animations.R;

public class FileProviderActivity extends AppCompatActivity {
    private static final String TAG = "FileProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_provider);
        Intent mIntent = getIntent();
        if (mIntent != null) {
            Uri str = mIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            Log.e(TAG, "onCreate: str=" + str);



            String file = getRealFilePath(this, str);
            Log.e(TAG, "onCreate: file=" + file);
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    null, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
