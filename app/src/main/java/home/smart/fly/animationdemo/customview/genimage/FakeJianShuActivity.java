package home.smart.fly.animationdemo.customview.genimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.genimage.helper.Article;
import home.smart.fly.animationdemo.customview.genimage.helper.WebViewHelper;
import home.smart.fly.animationdemo.utils.Tools;

public class FakeJianShuActivity extends AppCompatActivity {
    private static final String TAG = "FakeJianShuActivity";
    private static final String URL = "http://www.jianshu.com/p/869fbab4b006";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_image);
        loadView();
    }

    private void loadView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl(URL);


        WebViewHelper.getInstance().setUpWebView(mWebView, new WebViewHelper.OnGetDataListener() {
            @Override
            public void getDataListener(String text) {
                Tools.saveToSDCard("html.txt", text);
                Intent intent = new Intent(FakeJianShuActivity.this, Activity_Capture.class);
                Article article = new Article(text, TextUtils.isEmpty(WebViewHelper.getInstance().getTitle()) ? "" : "《" + WebViewHelper.getInstance().getTitle() + "》");
                intent.putExtra("data", article);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jianshu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        WebViewHelper.getInstance().getSelectedData(mWebView);
                    }
                });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
