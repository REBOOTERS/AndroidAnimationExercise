package home.smart.fly.animationdemo.webview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.Tools;
import home.smart.fly.animationdemo.utils.V;

public class AllWebViewActivity extends AppCompatActivity {
    private static final String TAG = "AllWebViewActivity";

    private static final String WEB_URL = "https://www.baidu.com";
    private static final String LOCAL_URL = "file:///android_asset/index.html";

    private Context mContext;
    private WebView mWebView;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_all_web_view);
        mWebView = V.f(this, R.id.webview);
        mButton = V.f(this, R.id.button);
        setUpWebView();
        loadData();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = Tools.getCurrentTime();
                String version = Tools.getVersion(mContext);
                String name = Tools.getName(mContext);
                String info = "Application Info: \n\n version: " + version + "\n name: " + name + "\n curTime: " + time;
                mWebView.loadUrl("javascript:showAlert('" + info + "')");
            }
        });
    }

    private void setUpWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //
        mWebView.addJavascriptInterface(new JsObject(mContext), "myObj");
        //
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    private void loadData() {
//        mWebView.loadUrl(WEB_URL);
        mWebView.loadUrl(LOCAL_URL);
    }
}
