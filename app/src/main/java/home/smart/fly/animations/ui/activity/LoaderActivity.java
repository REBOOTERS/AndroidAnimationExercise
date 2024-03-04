package home.smart.fly.animations.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import home.smart.fly.animations.R;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaderctivity);
        setSupportActionBar(findViewById(R.id.toolbar));
    }
}
