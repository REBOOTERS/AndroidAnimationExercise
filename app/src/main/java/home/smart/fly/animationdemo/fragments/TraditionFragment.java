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
import home.smart.fly.animationdemo.blur.BlurActivity;
import home.smart.fly.animationdemo.customview.swipeanim.FakeWeiBoActivity;
import home.smart.fly.animationdemo.tradition.FrameAnimationActivity;
import home.smart.fly.animationdemo.tradition.SwitchAnimActivity;
import home.smart.fly.animationdemo.tradition.TweenedAnimationActivity;

/**
 * Created by rookie on 2016/8/12.
 */
public class TraditionFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View rootView;


    private Button frame, tweened, blur, activitySwitch, swipeAnim;

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

    private void InitView() {
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
            case R.id.swipeAnim:
                intent = new Intent(mContext, FakeWeiBoActivity.class);
                startActivity(intent);
            default:
                break;
        }

    }
}
