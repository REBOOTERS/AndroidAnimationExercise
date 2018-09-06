package home.smart.fly.animations.ui.activity.multifragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import home.smart.fly.animations.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment extends Fragment {
    private static final String TAG = "FourFragment";

    private View rootView;
    boolean a=true;

    public FourFragment() {
        // Required empty public constructor
    }


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
            rootView = inflater.inflate(R.layout.fragment_four, container, false);
            loadData();
        }
        final SubsamplingScaleImageView imageView = rootView.findViewById(R.id.image);
        imageView.setImage(ImageSource.resource(R.drawable.cat));

        rootView.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a){
                    imageView.setImage(ImageSource.resource(R.drawable.a5));
                }else {
                    imageView.setImage(ImageSource.resource(R.drawable.cat));
                }
                a=!a;
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
