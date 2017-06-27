package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;

public class InputActivity extends AppCompatActivity {

    @BindView(R.id.input)
    EditText mInput;
    @BindView(R.id.result)
    TextView mResult;
    @BindView(R.id.format)
    Button mFormat;
    @BindView(R.id.get)
    Button mGet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);


        mFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String temp = mResult.getText().toString();

                temp = temp.replace("\n", "");

                mResult.setText(temp);
            }
        });


        mGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult.setText(mInput.getText().toString());
            }
        });

    }


}
