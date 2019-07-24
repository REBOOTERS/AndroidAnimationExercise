package home.smart.fly.animations.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.V;

import java.util.List;

/**
 * Created by rookie on 2017/5/17.
 */


public class SimpleRecyclerViewAdapter extends BaseRecyclerViewAdapter<String> {

    private Context mContext;

    public SimpleRecyclerViewAdapter(List<String> datas) {
        super(datas);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Glide.with(mContext)
//                .load(demos.get(position))
//                .apply(new RequestOptions().placeholder(R.drawable.a6))
//                .into(holder.mImageView);


//        holder.itemshell.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VegaRecyclerViewActivity.class)));
    }

    class MyHolder extends RecyclerView.ViewHolder {
        View itemshell;
        ImageView mImageView;

        public MyHolder(View itemView) {
            super(itemView);
            mImageView = V.f(itemView, R.id.image);
            itemshell = V.f(itemView, R.id.itemshell);
        }
    }

}