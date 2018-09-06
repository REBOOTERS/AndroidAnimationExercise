package home.smart.fly.animations.ui.activity.multifragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.CustomTintImageView;


public class TwoFragment extends Fragment {

    private static final String TAG = "TwoFragment";
    public TwoFragment() {
        // Required empty public constructor
    }

    private View rootView;
    private CustomTintImageView mCustomTintImageView;
    private boolean selected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "onCreateView: ");

        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_two, container, false);
            loadData();
        }
        mCustomTintImageView=rootView.findViewById(R.id.check);
        mCustomTintImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected=!selected;
                mCustomTintImageView.setChecked(selected);

            }
        });

        return rootView;
    }

    private void loadData() {
        Log.e(TAG, "loadData: ");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }
}
