package home.smart.fly.animations.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import home.smart.fly.animations.utils.TT;

/**
 * Created by Rookie on 2017/10/19.
 */

public class MyWebViewClient extends WebViewClient {

    private Context mContext;

    private static final String TAG = "MyWebViewClient";


    public MyWebViewClient(Context context) {
        mContext = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Log.e(TAG, "shouldOverrideUrlLoading: url=" + url);

        // WebView 唤起微信支付页面
        if (url.startsWith("https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?") || url.startsWith("weixin://wap/pay?")) {

            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                TT.showSToast(mContext, "未安装微信");
                Log.e(TAG, "shouldOverrideUrlLoading: e=" + e.toString());
            }


            return true;
        } else if (url.contains("platformapi/startapp")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
            } catch (Exception e) {
                TT.showSToast(mContext, "未安装支付宝");
                e.printStackTrace();
            }
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
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
