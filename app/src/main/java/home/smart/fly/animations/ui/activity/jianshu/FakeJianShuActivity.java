package home.smart.fly.animations.ui.activity.jianshu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.jianshu.helper.Constant;
import home.smart.fly.animations.ui.activity.jianshu.helper.HackTool;
import home.smart.fly.animations.ui.activity.jianshu.helper.HtmlBean;
import home.smart.fly.animations.utils.FileUtil;
import home.smart.fly.animations.utils.TT;


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
    private AlertDialog selectDialog;

    private String url = Constant.LINKS[0];

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
            HtmlBean bean = HackTool.getInfoFromUrl(params[0]);
            if (bean == null) {
                String json = FileUtil.getLocalResponse(mContext, Constant.LOCAL_DATA);

                Gson gson = new Gson();
                bean = gson.fromJson(json, HtmlBean.class);
            }


            return bean;
        }

        @Override
        protected void onPostExecute(HtmlBean s) {
            super.onPostExecute(s);
            mHtmlBean = s;
            Glide.with(mContext).load("http:" + s.getUserImg())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.default_avtar)
                            .error(R.drawable.default_avtar))
                    .into(userImg);
            String text = s.getContent();
//            text = text.replace("data-original-src", "src");
//            text = text.replace("//upload-images", "http://upload-images");
            mWebView.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");
            username.setText(s.getUsername());
            publichsTime.setText(s.getPublishTime());
            titleTv.setText(s.getTitle());
            words.setText(s.getWords() + "字");
            setTitle(s.getTitle());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("");
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                genImg.setVisibility(View.VISIBLE);
                TT.showSToast(mContext, "再次点击文章可隐藏图片分享");
            }
        });
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
                Intent intent = new Intent(FakeJianShuActivity.this, GenScreenShotActivity.class);
                intent.putExtra("data", mHtmlBean);
                startActivity(intent);
            }
        });

        // 点击隐藏底部按钮
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
                    default:
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
                    mWebView.setVisibility(View.VISIBLE);
                }

            }
        });
        mDialog = new ProgressDialog(mContext);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();


        setUpChoiceDialog();
        setUpInputDialog();
    }

    /**
     * 设置单选Dialog
     */
    private void setUpChoiceDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请选择内容");
        builder.setPositiveButton("加载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                upDateView();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setSingleChoiceItems(Constant.ITEMS, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                url = Constant.LINKS[which];
            }
        });
        selectDialog = builder.create();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialog.show();
            }
        });
    }

    /**
     * 设置Input Dialog
     */
    private void setUpInputDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_item, null);
        final AppCompatEditText input = (AppCompatEditText) view.findViewById(R.id.input);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("输入简书网址");
        builder.setView(view);
        builder.setPositiveButton("加载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                url = input.getText().toString();
                dialog.dismiss();
                upDateView();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        inputDialog = builder.create();
    }

    /**
     * 刷新视图
     */
    private void upDateView() {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://www.jianshu")) {

                if (mDialog != null) {
                    mDialog.show();
                    head.setVisibility(View.INVISIBLE);
                    mWebView.setVisibility(View.INVISIBLE);
                    new MyAsyncTask().execute(url);
                }
            } else {
                TT.showSToast(mContext, "输入内容非简书文章链接！");
            }

        } else {
            TT.showSToast(mContext, "你没有输入任何内容 ！");
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
                Intent intent = new Intent(FakeJianShuActivity.this, GenScreenShotActivity.class);
                intent.putExtra("data", mHtmlBean);
                startActivity(intent);
                break;
            case R.id.input:
                inputDialog.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
