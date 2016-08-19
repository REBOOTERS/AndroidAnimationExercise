package home.smart.fly.animationdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import home.smart.fly.animationdemo.fragments.PropertyFragment;
import home.smart.fly.animationdemo.fragments.TraditionFragment;
import home.smart.fly.animationdemo.utils.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private Button btn_message, btn_call;
    private TraditionFragment messageFragment;
    private PropertyFragment callFragment;
    public static final int TRADITION_TYPE = 1;
    public static final int PROPERTY_TYPE = 2;
    public int currentFragmentType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initData();
    }


    private void initData() {
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
            loadFragment(TRADITION_TYPE);
        }
    }

    private void switchFragment(int type) {
        switch (type) {
            case TRADITION_TYPE:
                loadFragment(TRADITION_TYPE);
                break;
            case PROPERTY_TYPE:
                loadFragment(PROPERTY_TYPE);
                break;
        }

    }

    private void loadFragment(int type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (type == PROPERTY_TYPE) {
            if (callFragment == null) {
                callFragment = new PropertyFragment();

                transaction.add(R.id.fl_content, callFragment, "zhishi");
            } else {
                transaction.show(callFragment);
            }
            if (messageFragment != null) {
                transaction.hide(messageFragment);
            }
            currentFragmentType = TRADITION_TYPE;
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
            currentFragmentType = PROPERTY_TYPE;
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
                switchFragment(TRADITION_TYPE);

                break;
            case R.id.btn_call:
                btn_message.setTextColor(Color.WHITE);
                btn_call.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_message.setBackgroundResource(R.drawable.left_transparent);
                btn_call.setBackgroundResource(R.drawable.right_bold);
                switchFragment(PROPERTY_TYPE);
                break;
            default:
                break;
        }
    }
}
