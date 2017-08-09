package home.smart.fly.animations.activity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.SendEmail;

public class MailActivity extends AppCompatActivity {
    private static final String TAG = "MailActivity";

    @BindView(R.id.sendMail)
    Button mSendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        ButterKnife.bind(this);

        Intent receiveIntent = getIntent();

        if (receiveIntent != null) {

            if (receiveIntent.getClipData() != null) {
                ClipData mClipData = receiveIntent.getClipData();

                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item mItem = mClipData.getItemAt(i);
                    CharSequence text = mItem.getText();

                    String[] datas = text.toString().split("http");

                    if (datas.length > 1) {
                        Log.e(TAG, "onCreate: " + datas[0]);
                        Log.e(TAG, "onCreate: " + "http" + datas[1]);
                    }


                }


            }
        }
    }

    @OnClick(R.id.sendMail)
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.sendMail:
                newSendMail();
                break;
            default:
                break;
        }
    }

    private void newSendMail() {
        //耗时操作要起线程...有几个新手都是这个问题
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    SendEmail sender = new SendEmail();
                    //设置服务器地址和端口，网上搜的到
                    sender.setProperties("smtp.163.com", "25");
                    //分别设置发件人，邮件标题和文本内容
                    sender.setMessage("foreverlove.zyl@163.com", "EmailSender", "Java Mail ！");
                    //设置收件人
                    sender.setReceiver(new String[]{"zhuyongqing89@163.com"});
                    //添加附件
                    //这个附件的路径是我手机里的啊，要发你得换成你手机里正确的路径
//                          sender.addAttachment("/sdcard/DCIM/Camera/asd.jpg");
                    //发送邮件
                    sender.sendEmail("smtp.163.com", "foreverlove.zyl@163.com", "");
                    //sender.setMessage("你的163邮箱账号", "EmailS//ender", "Java Mail ！");这里面两个邮箱账号要一致</span>

                } catch (AddressException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (MessagingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
