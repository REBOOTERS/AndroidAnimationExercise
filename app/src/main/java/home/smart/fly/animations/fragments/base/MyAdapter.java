package home.smart.fly.animations.fragments.base;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skydoves.transformationlayout.TransformationLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.FullscreenADActivity;
import home.smart.fly.animations.utils.V;

/**
 * @version V1.0
 * @author: Rookie
 * @date: 2018-08-20 11:01
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private Context mContext;

    private List<String> whiteList = new ArrayList<>();
    private List<ItemInfo> demos;
    private int backgroundRes = R.drawable.girl;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_AD = 1;


    public MyAdapter(List<ItemInfo> demos) {
        this.demos = demos;
        String[] datas = {"FakeFootballActivity", "PolygonViewActivity"};
        Collections.addAll(whiteList, datas);
    }

    @Override
    public int getItemViewType(int position) {
        if (demos.get(position).activitys == FullscreenADActivity.class) {
            return TYPE_AD;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case TYPE_NORMAL:
                return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.demo_info_item, null));
            case TYPE_AD:
                return new ADViewHolder(LayoutInflater.from(mContext).inflate(R.layout.demo_ad_item, null));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_NORMAL:
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                normalViewHolder.title.setText(demos.get(position).activitys.getSimpleName());
                normalViewHolder.desc.setText(demos.get(position).desc);
                normalViewHolder.itemshell.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, demos.get(position).activitys);

                    if (whiteList.contains(demos.get(position).activitys.getSimpleName())) {
                        Bundle bundle = normalViewHolder
                                .transformationLayout
                                .withView(normalViewHolder.itemView, "myTransitionName");
                        intent.putExtra("TransformationParams",
                                normalViewHolder.transformationLayout.getParcelableParams());
                        mContext.startActivity(intent, bundle);
                    } else {
                        mContext.startActivity(intent);
                    }

                });
                break;
            case TYPE_AD:
                ADViewHolder adViewHolder = (ADViewHolder) holder;
                adViewHolder.mAdImageView.setImageResource(backgroundRes);
                adViewHolder.mAdImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FullscreenADActivity.class);
                        intent.putExtra("url", backgroundRes);
                        Bundle bundle = adViewHolder
                                .transformationLayout
                                .withView(adViewHolder.itemView, "myTransitionName");
                        intent.putExtra("TransformationParams",
                                adViewHolder.transformationLayout.getParcelableParams());
                        mContext.startActivity(intent, bundle);
//                        mContext.startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }

    public void setAmazingBackground(@DrawableRes int resId) {
        backgroundRes = resId;
    }


    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return demos.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        MyHolder(View itemView) {
            super(itemView);
        }
    }

    class NormalViewHolder extends MyHolder {
        TextView title, desc;
        RelativeLayout itemshell;
        TransformationLayout transformationLayout;

        NormalViewHolder(View itemView) {
            super(itemView);
            title = V.f(itemView, R.id.title);
            desc = V.f(itemView, R.id.desc);
            itemshell = V.f(itemView, R.id.itemshell);
            transformationLayout = itemView.findViewById(R.id.transformationLayout);
        }
    }

    class ADViewHolder extends MyHolder {
        AdImageView mAdImageView;
        TransformationLayout transformationLayout;

        ADViewHolder(View itemView) {
            super(itemView);
            mAdImageView = V.f(itemView, R.id.ad_view);
            transformationLayout = itemView.findViewById(R.id.transformationLayout);
        }
    }


}
