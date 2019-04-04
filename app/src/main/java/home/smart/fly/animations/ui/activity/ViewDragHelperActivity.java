package home.smart.fly.animations.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import home.smart.fly.animations.R;
import home.smart.fly.animations.widget.DragHelperView;

public class ViewDragHelperActivity extends AppCompatActivity {
    private boolean isReverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drag_helper);


        DragHelperView mViewGroup = findViewById(R.id.shell);
        findViewById(R.id.image).setOnClickListener(v -> {
            mViewGroup.testSmoothSlide(isReverse);
            isReverse = !isReverse;
        });
    }
}
