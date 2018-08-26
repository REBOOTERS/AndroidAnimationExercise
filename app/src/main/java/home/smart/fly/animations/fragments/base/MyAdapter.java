package home.smart.fly.animations.fragments.base;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.V;

/**
 * @version V1.0
 * @author: Rookie
 * @date: 2018-08-20 11:01
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{

    private Context mContext;

    private List<ItemInfo> demos;

    public MyAdapter(List<ItemInfo> demos) {
        this.demos = demos;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.demo_info_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.title.setText(demos.get(position).activitys.getSimpleName());
        holder.desc.setText(demos.get(position).desc);
        holder.itemshell.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, demos.get(position).activitys);
            mContext.startActivity(intent);
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
