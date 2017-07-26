package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewStub;

import home.smart.fly.animations.R;

public class CircleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_view);

        initView();
    }

    private void initView() {

        LayoutInflater inflater=getLayoutInflater();

        inflater = LayoutInflater.from(this);

        ViewStub view;


    }
}
