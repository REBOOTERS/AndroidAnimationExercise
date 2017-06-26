package home.smart.fly.animations.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.FlowLayout;
import home.smart.fly.animations.utils.T;

public class FlowLayoutActivity extends AppCompatActivity {

    @BindView(R.id.flowlayout)
    FlowLayout mFlowlayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);
        ButterKnife.bind(this);
        mContext = this;
        final String[] datas = getResources().getStringArray(R.array.flows);

        for (int mI = 0; mI < datas.length; mI++) {
            final TextView mTextView = (TextView) LayoutInflater
                    .from(mContext).inflate(R.layout.flow_item_layout, mFlowlayout, false);
            mTextView.setText(datas[mI]);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.showSToast(mContext, mTextView.getText().toString());
                }
            });
            mFlowlayout.addView(mTextView);
        }
    }
}
