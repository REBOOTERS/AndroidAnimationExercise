package home.smart.fly.animations.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import home.smart.fly.animations.adapter.PlayerBean;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.Tools;

public class FakeFootballActivity extends AppCompatActivity {

    private Context mContext;

    private List<PlayerBean> mPlayerBeanList = new ArrayList<>();

    @BindView(R.id.playerLists)
    RecyclerView playerLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_football);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.transblue),0);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    private void initData() {
        String json = Tools.readStrFromAssets("player.json", mContext);
        Gson mGson = new Gson();
        mPlayerBeanList = mGson.fromJson(json, new TypeToken<List<PlayerBean>>() {
        }.getType());

        MyAdapter adapter = new MyAdapter(playerLists);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL, false);
        playerLists.setLayoutManager(manager);
        playerLists.setAdapter(adapter);


        adapter.setData(mPlayerBeanList);
    }

    private class MyAdapter extends BGARecyclerViewAdapter<PlayerBean> {


        public MyAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.dqd_player_item);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, PlayerBean model) {
            helper.setText(R.id.name, model.getName());
            Glide.with(mContext).load(model.getPerson_img()).into(helper.getImageView(R.id.img));
        }
    }
}
