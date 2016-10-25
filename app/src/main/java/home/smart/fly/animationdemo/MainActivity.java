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
    private Button btn_tradition, btn_property;
    private TraditionFragment traditionFragment;
    private PropertyFragment propertyFragment;
    public static final int TRADITION_TYPE = 1;
    public static final int PROPERTY_TYPE = 2;
    public int currentFragmentType = -1;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        btn_tradition = (Button) findViewById(R.id.btn_message);
        btn_property = (Button) findViewById(R.id.btn_call);
        btn_tradition.setOnClickListener(this);
        btn_property.setOnClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment mainFragment = fragmentManager.findFragmentByTag("message");
        if (mainFragment != null) {
            transaction.replace(R.id.fl_content, mainFragment);
            transaction.commit();
        } else {
            loadFragment(PROPERTY_TYPE);
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
            if (propertyFragment == null) {
                propertyFragment = new PropertyFragment();

                transaction.add(R.id.fl_content, propertyFragment, "property");
            } else {
                transaction.show(propertyFragment);
            }
            if (traditionFragment != null) {
                transaction.hide(traditionFragment);
            }
            currentFragmentType = TRADITION_TYPE;
        } else {
            if (traditionFragment == null) {
                traditionFragment = new TraditionFragment();
                transaction.add(R.id.fl_content, traditionFragment, "traditional");
            } else {
                transaction.show(traditionFragment);
            }
            if (propertyFragment != null) {
                transaction.hide(propertyFragment);
            }
            currentFragmentType = PROPERTY_TYPE;
        }
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_message:
                btn_tradition.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_property.setTextColor(Color.WHITE);
                btn_tradition.setBackgroundResource(R.drawable.left_bold);
                btn_property.setBackgroundResource(R.drawable.right_transparent);
                switchFragment(TRADITION_TYPE);

                break;
            case R.id.btn_call:
                btn_tradition.setTextColor(Color.WHITE);
                btn_property.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_tradition.setBackgroundResource(R.drawable.left_transparent);
                btn_property.setBackgroundResource(R.drawable.right_bold);
                switchFragment(PROPERTY_TYPE);
                break;
            default:
                break;
        }
    }


}
