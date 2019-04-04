package home.smart.fly.animations.ui.activity.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.CircleMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleMenuFragment extends Fragment {


    @BindView(R.id.circleMenu)
    CircleMenu circleMenu;
    Unbinder unbinder;

    public CircleMenuFragment() {
        // Required empty public constructor
    }

    private String[] mItemTexts={"微信","微信","微信","微信","微信","微信"};
    private int[] mItemImgs={R.drawable.logo_wechat_n,R.drawable.logo_wechat_n,
            R.drawable.logo_wechat_n,R.drawable.logo_wechat_n,R.drawable.logo_wechat_n,
            R.drawable.logo_wechat_n};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        circleMenu.setMenuItemIconsAndTexts(mItemImgs,mItemTexts);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
