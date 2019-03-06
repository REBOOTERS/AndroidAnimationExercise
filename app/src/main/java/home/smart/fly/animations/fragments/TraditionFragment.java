package home.smart.fly.animations.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.swipeanim.FakeWeiBoActivity;
import home.smart.fly.animations.fragments.base.BaseFragment;
import home.smart.fly.animations.fragments.base.RoutePaths;
import home.smart.fly.animations.property.blur.BlurActivity;
import home.smart.fly.animations.tradition.FrameAnimationActivity;
import home.smart.fly.animations.tradition.SwitchAnimActivity;
import home.smart.fly.animations.tradition.TweenedAnimationActivity;
import home.smart.fly.animations.tradition.VPAnimActivity;
import home.smart.fly.animations.ui.activity.PrepareActivity;

/**
 * Created by rookie on 2016/8/12.
 */
@Route(path = RoutePaths.TRADITION)
public class TraditionFragment extends BaseFragment implements View.OnClickListener {
    private Context mContext;
    private View rootView;


    private Button frame, tweened, blur, activitySwitch, vpAnim, swipeAnim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frament_tradition, null);
        InitView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    protected void InitView() {
        frame = (Button) rootView.findViewById(R.id.frame);
        frame.setOnClickListener(this);
        tweened = (Button) rootView.findViewById(R.id.tween);
        tweened.setOnClickListener(this);

        blur = (Button) rootView.findViewById(R.id.blur);
        blur.setOnClickListener(this);
        blur = (Button) rootView.findViewById(R.id.blur);
        blur.setOnClickListener(this);
        activitySwitch = (Button) rootView.findViewById(R.id.activitySwitch);
        activitySwitch.setOnClickListener(this);
        vpAnim = (Button) rootView.findViewById(R.id.vpAnim);
        vpAnim.setOnClickListener(this);
        swipeAnim = (Button) rootView.findViewById(R.id.swipeAnim);
        swipeAnim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.frame:
                intent = new Intent(mContext, FrameAnimationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.tween:
                intent = new Intent(mContext, TweenedAnimationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_bottom);
                break;
            case R.id.blur:
                intent = new Intent(mContext, BlurActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.activitySwitch:
                intent = new Intent(mContext, SwitchAnimActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_bottom);
                break;
            case R.id.vpAnim:
                intent = new Intent(mContext, VPAnimActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.swipeAnim:
                intent = new Intent(mContext, PrepareActivity.class);
                startActivity(intent);
            default:
                break;
        }

    }

    @Override
    public int getBackgroundResId() {
        return R.drawable.miui_nine;
    }
}
