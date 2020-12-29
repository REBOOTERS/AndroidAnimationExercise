package com.engineer.imitate.ui.activity.ninepoint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.engineer.imitate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NinePointActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnSend; //发送按钮
    private Button mBtnBack; //返回按钮
    private EditText mEditTextContent;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter; //消息视图的适配器
    private List<ChatMsgEntity> mDataArrays = new ArrayList<>(); //消息对象数组

    private static final int COUNT = 12;

    private String[] msgArray = new String[]{
            "有大吗", "有！你呢？", "我也有", "那上吧", "打啊！你放大啊！",
            "你TM咋不放大呢？留大抢人头啊？CAO！你个菜B", "2B不解释", "尼滚...",
            "今晚去网吧包夜吧？", "有XX吗？", "XX一大堆啊~还怕没XX？", "OK,搞起！！"
    };

    private String[] dateArray = new String[]{
            "2012-09-22 18:00:02", "2017-08-22 18:10:22",
            "2017-08-22 18:11:24", "2017-08-22 18:20:23",
            "2017-08-22 18:30:31", "2017-08-22 18:35:37",
            "2017-08-22 18:40:13", "2017-08-22 18:50:26",
            "2017-08-22 18:52:57", "2017-08-22 18:55:11",
            "2017-08-22 18:56:45", "2017-08-22 18:57:33"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏标题栏(不起作用)
//        getSupportActionBar().hide();  //隐藏标题栏
        setContentView(R.layout.activity_nine_point);

        initView();
        initData();
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    public void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    /**
     * 模拟加载消息历史，实际开发可从数据库中取出
     */
    public void initData() {
        for (int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dateArray[i]);
            if (i % 2 == 0) {
                entity.setName("AA");
                entity.setComMsg(true); //收到的消息
            } else {
                entity.setName("TYL");
                entity.setComMsg(false); //自己发送的消息
            }
            entity.setMessage(msgArray[i]);
            mDataArrays.add(entity);
        }

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send: //发送按钮点击事件
                send();
                break;
            case R.id.btn_back: //返回按钮点击事件
                finish(); //结束，实际开发过程中可返回主界面
                break;
            default:
                break;
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName("TYL");
            entity.setDate(getDate());
            entity.setMessage(contString);
            entity.setComMsg(false);

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged(); //通知ListView,数据已发生改变

            mEditTextContent.setText(""); //清空编辑框数据

            mListView.setSelection(mListView.getCount() - 1); //发送一条消息时，ListView显示选择最后一项
        }
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(new Date());
    }
}
