package home.smart.fly.animations.ui.activity.transtions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.StackView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

public class StackViewActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.stackView)
    StackView mStackView;

    //
    private List<String> pics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_stack_view);
        ButterKnife.bind(this);
        initData();
        mStackView.setAdapter(new ImageAdapter(pics, mContext));
    }

    private class ImageAdapter extends BaseAdapter {

        private List<String> pics = new ArrayList<>();
        private Context mContext;

        public ImageAdapter(List<String> pics, Context context) {
            this.pics = pics;
            mContext = context;
        }

        @Override
        public int getCount() {
            return this.pics.size();
        }

        @Override
        public Object getItem(int position) {
            return this.pics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView = new ImageView(mContext);
//            Glide.with(mContext).load(pics.get(position)).placeholder(R.drawable.a5).into(mImageView);
            mImageView.setImageResource(R.drawable.a5);
            return mImageView;
        }
    }

    private void initData() {
        String json = Tools.readStrFromAssets("pics.json", mContext);
        Gson gson = new Gson();
        pics = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }
}
