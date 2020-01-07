package com.engineer.imitate.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.engineer.imitate.R;
import com.engineer.imitate.ui.widget.AbsBannerAdapter;

public class BannerAdapter extends AbsBannerAdapter {

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    protected View makeView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    protected void bind(View view, int position) {
        if (position == 0) {
            ((ImageView) view).setImageResource(R.drawable.girl);
        } else if (position == 1) {
            ((ImageView) view).setImageResource(R.drawable.good);
        } else {
            ((ImageView) view).setImageResource(R.drawable.comic);
        }
    }
}
