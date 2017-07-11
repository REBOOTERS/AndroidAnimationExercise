package home.smart.fly.animations.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

public class WeiXinGalleryActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.grllery)
    RecyclerView mGrllery;

    private SimpleAdapter mAdapter;


    private List<String> pics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_wei_xin_gallery);
        ButterKnife.bind(this);
        initDatas();


    }

    private void initDatas() {
        String data = Tools.readStrFromAssets("pics.data", mContext);
        pics = new Gson().fromJson(data, new TypeToken<List<String>>() {
        }.getType());

        pics = pics.subList(0, 9);
        mGrllery.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter = new SimpleAdapter(mGrllery);
        mGrllery.setAdapter(mAdapter);
        mAdapter.setData(pics);
    }


    private class SimpleAdapter extends BGARecyclerViewAdapter<String> {

        public SimpleAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.image);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, String model) {
            Glide.with(mContext).load(pics.get(position)).placeholder(R.drawable.a5)
                    .into(helper.getImageView(R.id.image));
        }
    }
}
