package home.smart.fly.animations.recyclerview.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.DpConvert;
import home.smart.fly.animations.utils.V;

/**
 * Created by rookie on 2017/5/17.
 */


public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.MyHolder> {

    private Context mContext;
    private List<String> demos;
    private List<Integer> heights = new ArrayList<>();


    public StaggeredRecyclerViewAdapter(List<String> demos) {
        this.demos = demos;


    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        initRandomHeights(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    private void initRandomHeights(Context context) {
        int base = DpConvert.dip2px(context, 160);
        int factor = DpConvert.dip2px(context, 50);
        for (int i = 0; i < demos.size(); i++) {
            int height = (int) (base + Math.random() * factor);
            heights.add(height);
        }
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();
        params.height = heights.get(position);
        holder.mImageView.setLayoutParams(params);

        Glide.with(mContext).
                load(demos.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.a6))
                .into(holder.mImageView);
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
        LinearLayout itemshell;
        ImageView mImageView;

        public MyHolder(View itemView) {
            super(itemView);
            mImageView = V.f(itemView, R.id.image);
            itemshell = V.f(itemView, R.id.itemshell);
        }
    }

}