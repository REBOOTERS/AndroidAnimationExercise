package home.smart.fly.animationdemo.swipeanim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.swipeanim.lib.SwipeCardAdapter;


public class SmartAdapter extends SwipeCardAdapter<SmartAdapter.MyHolder> {
    private Context mContext;

    public SmartAdapter(Context context, List<String> list) {
        super(list);
        mContext = context;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.swipe_card_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setData((String) mList.get(position));
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

        public void setData(String text) {
            mTextView.setText(text);
        }
    }
}
