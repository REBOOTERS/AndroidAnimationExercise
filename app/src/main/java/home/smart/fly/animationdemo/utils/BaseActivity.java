package home.smart.fly.animationdemo.utils;

import android.support.v7.app.AppCompatActivity;

import home.smart.fly.animationdemo.R;


public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

}
