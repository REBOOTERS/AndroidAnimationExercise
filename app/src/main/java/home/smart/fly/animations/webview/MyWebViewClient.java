package home.smart.fly.animations.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Rookie on 2017/10/19.
 */

public class MyWebViewClient extends WebViewClient {

    private static final String TAG = "MyWebViewClient";
    public MyWebViewClient() {
        super();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.e(TAG, "shouldOverrideUrlLoading: request" + request.toString());
        return false;
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
