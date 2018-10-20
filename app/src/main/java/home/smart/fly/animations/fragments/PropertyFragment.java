package home.smart.fly.animations.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;

import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.AnimateDrawableActivity;
import home.smart.fly.animations.ui.activity.PhysicsViewActivity;
import home.smart.fly.animations.fragments.base.RoutePaths;
import home.smart.fly.animations.property.AliPayAnimActivity;
import home.smart.fly.animations.property.DecorViewActivity;
import home.smart.fly.animations.property.LayoutAnimationsActivity;
import home.smart.fly.animations.property.PropertyAnimationActivity;
import home.smart.fly.animations.property.RevealAnimatorActivity;
import home.smart.fly.animations.property.ShopCarAddAnimActivity;
import home.smart.fly.animations.property.ValueAnimationActivity;

/**
 * Created by rookie on 2016/8/12.
 */
@Route(path = RoutePaths.PROPERTY)
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
        rootView.findViewById(R.id.RevealAnimator).setOnClickListener(this);
        rootView.findViewById(R.id.DecorView).setOnClickListener(this);
        rootView.findViewById(R.id.PhysicsAnim).setOnClickListener(this);
        rootView.findViewById(R.id.animate_drawable).setOnClickListener(this);
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
                intent = new Intent(mContext, AliPayAnimActivity.class);
                break;
            case R.id.shopcar_add_anim:
                intent = new Intent(mContext, ShopCarAddAnimActivity.class);
                break;
            case R.id.LayoutAnimations:
                intent = new Intent(mContext, LayoutAnimationsActivity.class);
                break;
            case R.id.RevealAnimator:
                intent = new Intent(mContext, RevealAnimatorActivity.class);
                break;
            case R.id.DecorView:
                intent = new Intent(mContext, DecorViewActivity.class);
                break;
            case R.id.PhysicsAnim:
                intent=new Intent(mContext, PhysicsViewActivity.class);
                break;
            case R.id.animate_drawable:
                intent = new Intent(mContext, AnimateDrawableActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
