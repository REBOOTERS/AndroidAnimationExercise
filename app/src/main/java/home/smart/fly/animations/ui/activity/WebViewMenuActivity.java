package home.smart.fly.animations.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

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

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ActionMode mode = mWebView.startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }
                });

                Log.e(TAG, "onLongClick: mode=="+mode );

                return false;
            }
        });

    }



}
