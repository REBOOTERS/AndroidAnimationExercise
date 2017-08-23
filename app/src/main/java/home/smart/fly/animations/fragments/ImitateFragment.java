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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.activity.CircleViewActivity;
import home.smart.fly.animations.activity.FakeFootballActivity;
import home.smart.fly.animations.activity.FlipViewActivity;
import home.smart.fly.animations.activity.IModeActivity;
import home.smart.fly.animations.activity.MatisseDemoActivity;
import home.smart.fly.animations.activity.MySwipeMenuActivity;
import home.smart.fly.animations.activity.PhotoBrowse;
import home.smart.fly.animations.activity.PullRecyclerViewActivity;
import home.smart.fly.animations.activity.jianshu.FakeJianShuActivity;
import home.smart.fly.animations.activity.jianshu.JianShuHeadActivity;
import home.smart.fly.animations.customview.swipeanim.FakeWeiBoActivity;
import home.smart.fly.animations.customview.wheel.WheelViewActivity;
import home.smart.fly.animations.utils.V;


public class ImitateFragment extends Fragment {
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
        demos.add(new ItemInfo(R.string.fake_weibo, FakeWeiBoActivity.class));
        demos.add(new ItemInfo(R.string.ball, CircleViewActivity.class));
        demos.add(new ItemInfo(R.string.fake_football, FakeFootballActivity.class));
        demos.add(new ItemInfo(R.string.jianshu, FakeJianShuActivity.class));
        demos.add(new ItemInfo(R.string.wheelView, WheelViewActivity.class));

        demos.add(new ItemInfo(R.string.imode, IModeActivity.class));
        demos.add(new ItemInfo(R.string.jianshuhead, JianShuHeadActivity.class));
        demos.add(new ItemInfo(R.string.swipemenu, MySwipeMenuActivity.class));
        demos.add(new ItemInfo(R.string.pullzoom, PullRecyclerViewActivity.class));
        demos.add(new ItemInfo(R.string.flipView, FlipViewActivity.class));
        demos.add(new ItemInfo(R.string.Matisse, MatisseDemoActivity.class));
        demos.add(new ItemInfo(R.string.Matisse, PhotoBrowse.class));

        recyclerView = V.f(rootView, R.id.recyclerView);
        MyAdapter mAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        Toast.makeText(mContext, getResources().getString(R.string.toast), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
