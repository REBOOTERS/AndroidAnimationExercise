package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;

public class LaunchOtherAppActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView mWebView;
    private static final String LOCAL_URL = "file:///android_asset/launch_other_app.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_other_app);
        ButterKnife.bind(this);
        mWebView.loadUrl(LOCAL_URL);
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }

    }
}
