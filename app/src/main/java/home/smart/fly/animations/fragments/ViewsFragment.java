package home.smart.fly.animations.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.activity.CollegeActivity;
import home.smart.fly.animations.activity.FlowLayoutActivity;
import home.smart.fly.animations.activity.PlayActivity;
import home.smart.fly.animations.activity.WavaAnimActivity;
import home.smart.fly.animations.activity.demos.AnimationsDemo;
import home.smart.fly.animations.activity.transtions.StackViewActivity;
import home.smart.fly.animations.activity.transtions.SwitcherActivity;
import home.smart.fly.animations.customview.BasicPosActivity;
import home.smart.fly.animations.property.RevealAnimatorActivity;
import home.smart.fly.animations.utils.V;


/**
 * Created by rookie on 2016/8/12.
 */
public class ViewsFragment extends Fragment {
    private Context mContext;
    private View rootView;

    private RecyclerView recyclerView;

    private List<ItemInfo> demos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frament_custom_views, null);
        InitView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void InitView() {
        demos.add(new ItemInfo(R.string.app_name, PlayActivity.class));
        demos.add(new ItemInfo(R.string.self_view, BasicPosActivity.class));
        demos.add(new ItemInfo(R.string.waveAnim, WavaAnimActivity.class));
        demos.add(new ItemInfo(R.string.app_name, RevealAnimatorActivity.class));
        demos.add(new ItemInfo(R.string.app_name, CollegeActivity.class));
        demos.add(new ItemInfo(R.string.app_name, AnimationsDemo.class));
        demos.add(new ItemInfo(R.string.flowlayout, FlowLayoutActivity.class));
        demos.add(new ItemInfo(R.string.flowlayout, SwitcherActivity.class));
        demos.add(new ItemInfo(R.string.flowlayout, StackViewActivity.class));


        recyclerView = V.f(rootView, R.id.recyclerView);
        MyAdapter mAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.demo_info_item, null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            holder.title.setText(demos.get(position).activitys.getSimpleName());
            holder.desc.setText(demos.get(position).desc);
            holder.itemshell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, demos.get(position).activitys);
                    startActivity(intent);
                }
            });
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
            TextView title, desc;
            LinearLayout itemshell;

            MyHolder(View itemView) {
                super(itemView);
                title = V.f(itemView, R.id.title);
                desc = V.f(itemView, R.id.desc);
                itemshell = V.f(itemView, R.id.itemshell);
            }
        }

    }


    private class ItemInfo {
        private final int desc;
        private final Class<? extends Activity> activitys;

        ItemInfo(int desc, Class<? extends Activity> demoClass) {
            this.desc = desc;
            this.activitys = demoClass;
        }
    }
}
