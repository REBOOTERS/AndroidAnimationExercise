package home.smart.fly.animations.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.FileUtil;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.T;
import home.smart.fly.animations.utils.Tools;
import home.smart.fly.animations.utils.V;

public class AllWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AllWebViewActivity";

    private static final String WEB_URL = "https://www.baidu.com";
    private static final String ERROR_URL = "https://www.badu.com";
    private static final String LOCAL_URL = "file:///android_asset/index.html";

    private Context mContext;
    private WebView mWebView;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_all_web_view);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.cpb_green), 0);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("WebView");
        mWebView = V.f(this, R.id.webview);
        mButton = V.f(this, R.id.button);
        mButton.setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

        setUpWebView();
        loadData();
    }

    private void setUpWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //
        mWebView.addJavascriptInterface(new JsObject(mContext), "myObj");
        mWebView.addJavascriptInterface(new LoadHtmlObject(), "myHtml");
        //

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

    }

    private void loadData() {
        mWebView.loadUrl(LOCAL_URL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men_web_load_url, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.local:
                mWebView.loadUrl(LOCAL_URL);
                break;
            case R.id.net:
                mWebView.loadUrl(WEB_URL);
                break;
            case R.id.error:
                mWebView.loadUrl(ERROR_URL);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String time = Tools.getCurrentTime();
                String version = Tools.getVersion(mContext);
                String name = Tools.getName(mContext);
                String currentUrl = mWebView.getUrl();
                String info = "Application Info: \n\n version: " + version
                        + "\n name: " + name
                        + "\n curTime: " + time
                        + "\n curUrl: " + currentUrl;
                mWebView.loadUrl("javascript:showAlert('" + info + "')");
                break;
            case R.id.save:
                Picture snapShot = mWebView.capturePicture();
                Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                snapShot.draw(canvas);
                if (!TextUtils.isEmpty(FileUtil.savaBitmap2SDcard(mContext, bmp, "1111"))) {
                    T.showSToast(mContext, "success");
                }
                break;
            default:
                break;
        }
    }
}
