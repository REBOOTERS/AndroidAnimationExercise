package home.smart.fly.animations.adapter;

import android.content.Context;
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
 * Created by rookie on 2017/5/17.
 */


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private Context mContext;
    private List<String> demos;


    public MyAdapter(List<String> demos) {
        this.demos = demos;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.demo_info_item, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.title.setText(demos.get(position));
        holder.itemshell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        public MyHolder(View itemView) {
            super(itemView);
            title = V.f(itemView, R.id.title);
            desc = V.f(itemView, R.id.desc);
            itemshell = V.f(itemView, R.id.itemshell);
        }
    }

}