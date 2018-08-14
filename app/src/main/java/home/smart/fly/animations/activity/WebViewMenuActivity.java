package home.smart.fly.animations.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import java.util.Optional;

import home.smart.fly.animations.R;

public class WebViewMenuActivity extends AppCompatActivity {
    private static final String TAG = "WebViewMenuActivity";

    private WebView mWebView;


    private ActionMode mActionMode1;
    private ActionMode mActionMode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_menu);

        mWebView = findViewById(R.id.webView);
        mWebView.loadUrl("file:///android_asset/article.html");
        registerForContextMenu(mWebView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        WebView.HitTestResult testResult = mWebView.getHitTestResult();
        Log.e(TAG, "onCreateContextMenu: testResult==" + testResult.getExtra());
        Log.e(TAG, "onCreateContextMenu: type==" + testResult.getType());
    }
}
