package home.smart.fly.animations.ui.activity.multifragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.CustomImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {
    private static final String TAG = "OneFragment";

    public OneFragment() {
        // Required empty public constructor
    }

    private View rootView;
    private CustomImageView check;
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
            rootView = inflater.inflate(R.layout.fragment_one, container, false);


            loadData();
        }



        return rootView;
    }

    private void loadData() {
        Log.e(TAG, "loadData: ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = view.findViewById(R.id.web);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://www.baidu.com");
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
