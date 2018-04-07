package home.smart.fly.animations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;

public class FlipAdapter extends BaseAdapter implements OnClickListener {

    public interface Callback {
        void onPageRequested(int page);
    }

    static class Item {
        static long id = 0;

        long mId;

        public Item() {
            mId = id++;
        }

        long getId() {
            return mId;
        }
    }

    private LayoutInflater inflater;
    private Callback callback;
    private List<Item> items = new ArrayList<Item>();

    private List<String> pics;
    private Context mContext;

    public FlipAdapter(Context context, List<String> pics) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < pics.size(); i++) {
            items.add(new Item());
        }
        this.pics = pics;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.page, parent, false);

            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.firstPage = (Button) convertView.findViewById(R.id.first_page);
            holder.lastPage = (Button) convertView.findViewById(R.id.last_page);

            holder.firstPage.setOnClickListener(this);
            holder.lastPage.setOnClickListener(this);

            holder.page = (ImageView) convertView.findViewById(R.id.page);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        //TODO set a text with the id as well
//        holder.text.setText(items.get(position).getId() + ":" + position);
        Glide.with(mContext).load(pics.get(position)).into(holder.page);
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        Button firstPage;
        Button lastPage;
        ImageView page;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_page:
                if (callback != null) {
                    callback.onPageRequested(0);
                }
                break;
            case R.id.last_page:
                if (callback != null) {
                    callback.onPageRequested(getCount() - 1);
                }
                break;
        }
    }

    public void addItems(int amount) {
        for (int i = 0; i < amount; i++) {
            items.add(new Item());
        }
        notifyDataSetChanged();
    }

    public void addItemsBefore(int amount) {
        for (int i = 0; i < amount; i++) {
            items.add(0, new Item());
        }
        notifyDataSetChanged();
    }

}
