package home.smart.fly.animations.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import home.smart.fly.animations.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShareIdActivity extends AppCompatActivity {
    private static final String TAG = "ShareIdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: savedInstanceState==" + savedInstanceState);
        setContentView(R.layout.activity_share_id);
        TextView textView;
        View view = findViewById(R.id.tabs);
        System.out.println("view is " + view.getClass().getName());
    }
}
