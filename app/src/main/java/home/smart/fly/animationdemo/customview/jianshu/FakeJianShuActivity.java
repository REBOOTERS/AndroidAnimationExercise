package home.smart.fly.animationdemo.customview.jianshu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.jianshu.helper.Constant;
import home.smart.fly.animationdemo.customview.jianshu.helper.HtmlBean;
import home.smart.fly.animationdemo.utils.FileUtil;
import home.smart.fly.animationdemo.utils.T;


public class FakeJianShuActivity extends AppCompatActivity {
    private static final String TAG = "FakeJianShuActivity";

    private Context mContext;
    private WebView mWebView;

    private CircleImageView userImg;
    private TextView username, publichsTime, titleTv, words;
    private LinearLayout head, genImg;
    private FloatingActionButton fab;

    private long lastTime;

    private HtmlBean mHtmlBean;
    private ProgressDialog mDialog;
    private AlertDialog inputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_fake_jianshu);
        initView();
        new MyAsyncTask().execute(Constant.URL);

    }


    private class MyAsyncTask extends AsyncTask<String, Integer, HtmlBean> {
        @Override
        protected HtmlBean doInBackground(String... params) {
            mHtmlBean = getHtmlBean(params[0]);
            if (mHtmlBean == null) {
                String json = FileUtil.getLocalResponse(mContext, Constant.LOCAL_DATA);
                Gson gson = new Gson();
                mHtmlBean = gson.fromJson(json, HtmlBean.class);
            }
            return mHtmlBean;
        }

        @Override
        protected void onPostExecute(HtmlBean s) {
            super.onPostExecute(s);
            Glide.with(mContext).load(s.getUserImg()).into(userImg);
            String text = s.getContent();
            mWebView.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");
            username.setText(s.getUsername());
            publichsTime.setText(s.getPublishTime());
            titleTv.setText(s.getTitle());
            words.setText(s.getWords() + "字");
        }
    }

    @Nullable
    private HtmlBean getHtmlBean(String param) {
        HtmlBean htmlBean = null;

        try {
            //获取指定网址的页面内容
            Document document = Jsoup.connect(param).timeout(50000).get();
            String title = document.getElementsByClass("title").get(0).text();
            String username = document.getElementsByClass("name").get(0).getElementsByTag("a").get(0).text();
            String userImg = document.getElementsByClass("avatar").get(0).getElementsByTag("img").get(0).attr("src");
            String publishTime = document.getElementsByClass("publish-time").text();
            String words = document.getElementsByClass("wordage").text();
            Elements content = document.getElementsByClass("show-content");
            Element element = content.get(0);
            Elements imgs = element.getElementsByTag("img");
            for (Element ele_img : imgs) {
                ele_img.attr("style", "max-width:100%;height:auto;");
            }
            String contentStr = content.toString();
            //
            htmlBean = new HtmlBean();
            htmlBean.setContent(contentStr);
            htmlBean.setUsername(username);
            htmlBean.setTitle(title);
            htmlBean.setUserImg(userImg);
            htmlBean.setPublishTime(publishTime.split(" ")[0]);
            htmlBean.setWords(words.split(" ")[1]);
            return htmlBean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlBean;
    }

    private void initView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("");
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setOnCreateContextMenuListener(new MyMenuListener());
        userImg = (CircleImageView) findViewById(R.id.userImg);
        username = (TextView) findViewById(R.id.username);
        publichsTime = (TextView) findViewById(R.id.publishTime);
        titleTv = (TextView) findViewById(R.id.titleTv);
        words = (TextView) findViewById(R.id.words);
        head = (LinearLayout) findViewById(R.id.head);
        genImg = (LinearLayout) findViewById(R.id.genImg);
        genImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genImg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(FakeJianShuActivity.this, GenCaptureActivity.class);
                intent.putExtra("data", mHtmlBean);
                startActivity(intent);
            }
        });


        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTime = SystemClock.uptimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (SystemClock.uptimeMillis() - lastTime < 300) {
                            genImg.setVisibility(View.GONE);
                        }
                        break;
                }
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mDialog.dismiss();
                    head.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }

            }
        });
        mDialog = new ProgressDialog(mContext);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        final EditText input = new EditText(mContext);
        input.setPadding(10, 10, 10, 10);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("输入简书网址");
        builder.setView(input);
        builder.setPositiveButton("加载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    dialog.dismiss();
                    if (url.startsWith("http://www.jianshu")) {

                        if (mDialog != null) {
                            mDialog.show();
                            head.setVisibility(View.INVISIBLE);
                            new MyAsyncTask().execute(url);
                        }
                    }
                } else {
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        inputDialog = builder.create();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputDialog.show();
            }
        });
    }


    private class MyMenuListener implements View.OnCreateContextMenuListener {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            genImg.setVisibility(View.VISIBLE);
            T.showSToast(mContext, "再次点击文章可隐藏图片分享");
        }
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
                Intent intent = new Intent(FakeJianShuActivity.this, GenCaptureActivity.class);
                intent.putExtra("data", mHtmlBean);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
