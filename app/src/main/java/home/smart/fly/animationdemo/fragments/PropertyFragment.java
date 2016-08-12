package home.smart.fly.animationdemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.PropertyAnimationActivity;
import home.smart.fly.animationdemo.property.ValueAnimationActivity;
import home.smart.fly.animationdemo.property.ValueAnimationActivity2;

/**
 * Created by co-mall on 2016/8/12.
 */
public class PropertyFragment extends Fragment implements View.OnClickListener{
    private Context mContext;
    private View rootView;


    private Button property, value,value2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frament_property, null);
        InitView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void InitView() {
        property = (Button) rootView.findViewById(R.id.property);
        property.setOnClickListener(this);
        value = (Button) rootView.findViewById(R.id.value);
        value.setOnClickListener(this);
        value2 = (Button) rootView.findViewById(R.id.value2);
        value2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.property:
                intent = new Intent(mContext, PropertyAnimationActivity.class);
                break;
            case R.id.value:
                intent = new Intent(mContext, ValueAnimationActivity.class);
                break;
            case R.id.value2:
                intent = new Intent(mContext, ValueAnimationActivity2.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
