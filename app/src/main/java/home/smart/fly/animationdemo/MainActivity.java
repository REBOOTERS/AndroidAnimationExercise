package home.smart.fly.animationdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import home.smart.fly.animationdemo.fragments.PropertyFragment;
import home.smart.fly.animationdemo.fragments.TraditionFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private static final String TAG = "MainActivity";
    private FragmentManager fm;
    private Button btn_message, btn_call;
    private TraditionFragment messageFragment;
    private PropertyFragment callFragment;
    public static final int MESSAGE_FRAGMENT_TYPE = 1;
    public static final int CALL_FRAGMENT_TYPE = 2;
    public int currentFragmentType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initData();
    }


    private void initData() {
        fm = getSupportFragmentManager();
        btn_message = (Button) findViewById(R.id.btn_message);
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_message.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment mainFragment = fragmentManager.findFragmentByTag("message");
        if (mainFragment != null) {
            transaction.replace(R.id.fl_content, mainFragment);
            transaction.commit();
        } else {
            loadFragment(MESSAGE_FRAGMENT_TYPE);
        }
    }

    private void switchFragment(int type) {
        switch (type) {
            case MESSAGE_FRAGMENT_TYPE:
                loadFragment(MESSAGE_FRAGMENT_TYPE);
                break;
            case CALL_FRAGMENT_TYPE:
                loadFragment(CALL_FRAGMENT_TYPE);
                break;
        }

    }

    private void loadFragment(int type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (type == CALL_FRAGMENT_TYPE) {
            if (callFragment == null) {
                callFragment = new PropertyFragment();

                transaction.add(R.id.fl_content, callFragment, "zhishi");
            } else {
                transaction.show(callFragment);
            }
            if (messageFragment != null) {
                transaction.hide(messageFragment);
            }
            currentFragmentType = MESSAGE_FRAGMENT_TYPE;
        } else {
            if (messageFragment == null) {
                messageFragment = new TraditionFragment();
                transaction.add(R.id.fl_content, messageFragment, "wenda");
            } else {
                transaction.show(messageFragment);
            }
            if (callFragment != null) {
                transaction.hide(callFragment);
            }
            currentFragmentType = CALL_FRAGMENT_TYPE;
        }
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_message:
                btn_message.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_call.setTextColor(Color.WHITE);
                btn_message.setBackgroundResource(R.drawable.left_bold);
                btn_call.setBackgroundResource(R.drawable.right_transparent);
                switchFragment(MESSAGE_FRAGMENT_TYPE);

                break;
            case R.id.btn_call:
                btn_message.setTextColor(Color.WHITE);
                btn_call.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_message.setBackgroundResource(R.drawable.left_transparent);
                btn_call.setBackgroundResource(R.drawable.right_bold);
                switchFragment(CALL_FRAGMENT_TYPE);
                break;
            default:
                break;
        }
    }
}
