package home.smart.fly.animations.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.FileUtil;

public class ScreenCaptureActivity extends AppCompatActivity {
    private ImageView ivScreenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_capture);
        ivScreenshot = (ImageView) findViewById(R.id.ivScreenshot);
        findViewById(R.id.getScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });
    }

    public void takeScreenshot() {
        View viewRoot = getWindow().getDecorView().getRootView();
        viewRoot.setDrawingCacheEnabled(true);
        Bitmap temp = viewRoot.getDrawingCache();
        ScreenParam screenInfo = getScreenInfo();
        int statusBarHeight = getStatusBarHeight();
        Bitmap bitmap = Bitmap.createBitmap(temp, 0, statusBarHeight, screenInfo.width, screenInfo.height - statusBarHeight);
        viewRoot.setDrawingCacheEnabled(false);

        if (!TextUtils.isEmpty(FileUtil.savaBitmap2SDcard(this, bitmap, "myfile"))) {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FullscreenActivity.class));
        } else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
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
