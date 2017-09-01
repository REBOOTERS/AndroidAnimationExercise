package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gw.swipeback.SwipeBackLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;

public class SwipeFinishActivity extends AppCompatActivity {


    @BindView(R.id.left)
    RadioButton mLeft;
    @BindView(R.id.right)
    RadioButton mRight;
    @BindView(R.id.selectDirect)
    RadioGroup mSelectDirect;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_finish);
        ButterKnife.bind(this);
        mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.attachToActivity(this);
        mSelectDirect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.left) {
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
                } else {
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_RIGHT);
                }
            }
        });
    }
}
