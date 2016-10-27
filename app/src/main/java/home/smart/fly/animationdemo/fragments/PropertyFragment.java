package home.smart.fly.animationdemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.property.AliPaySuccessAnimActivity;
import home.smart.fly.animationdemo.property.CustomViewActivity;
import home.smart.fly.animationdemo.property.LayoutAnimationsActivity;
import home.smart.fly.animationdemo.property.PropertyAnimationActivity;
import home.smart.fly.animationdemo.property.ShopCarAddAnimActivity;
import home.smart.fly.animationdemo.property.ValueAnimationActivity;

/**
 * Created by rookie on 2016/8/12.
 */
public class PropertyFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View rootView;


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
        rootView.findViewById(R.id.property).setOnClickListener(this);
        rootView.findViewById(R.id.value).setOnClickListener(this);
        rootView.findViewById(R.id.demo).setOnClickListener(this);
        rootView.findViewById(R.id.shopcar_add_anim).setOnClickListener(this);
        rootView.findViewById(R.id.LayoutAnimations).setOnClickListener(this);
        rootView.findViewById(R.id.CustomView).setOnClickListener(this);
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
            case R.id.demo:
                intent = new Intent(mContext, AliPaySuccessAnimActivity.class);
                break;
            case R.id.shopcar_add_anim:
                intent = new Intent(mContext, ShopCarAddAnimActivity.class);
                break;
            case R.id.LayoutAnimations:
                intent = new Intent(mContext, LayoutAnimationsActivity.class);
                break;
            case R.id.CustomView:
                intent = new Intent(mContext, CustomViewActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
