package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.helper.SwipeBackLayout;


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
        mSelectDirect.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.left:
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
                    break;
                case R.id.right:
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_RIGHT);
                    break;
                case R.id.up:
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
                    break;
                case R.id.down:
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_BOTTOM);
                    break;
                default:
                    mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_ANY);
                    break;

            }
        });
    }
}
