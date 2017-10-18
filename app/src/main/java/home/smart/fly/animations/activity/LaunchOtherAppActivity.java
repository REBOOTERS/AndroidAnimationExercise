package home.smart.fly.animations.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;

public class LaunchOtherAppActivity extends AppCompatActivity {
    private static final String TAG = "LaunchOtherAppActivity";

    @BindView(R.id.webView)
    WebView mWebView;
    private static final String LOCAL_URL = "file:///android_asset/launch_other_app.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_other_app);
        ButterKnife.bind(this);
        mWebView.loadUrl(LOCAL_URL);

//        mWebView.setWebViewClient(new MyWebViewClient());
    }


    private class MyWebViewClient extends WebViewClient {
        public MyWebViewClient() {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading: request" + request.toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(TAG, "onPageStarted: url=" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "onPageFinished: url=" + url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.e(TAG, "onReceivedError: request=" + request.toString());
            Log.e(TAG, "onReceivedError: error=" + error.toString());
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.e(TAG, "onReceivedHttpError: request=" + request.toString());
            Log.e(TAG, "onReceivedHttpError: errorResponse=" + errorResponse.toString());
        }
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
