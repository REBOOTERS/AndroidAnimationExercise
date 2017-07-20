package home.smart.fly.animations.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.PlayerBean;
import home.smart.fly.animations.customview.views.BallGameView;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.Tools;

public class FakeFootballActivity extends AppCompatActivity implements BGAOnRVItemClickListener {

    @BindView(R.id.gameView)
    BallGameView mGameView;
    @BindView(R.id.playerLists)
    RecyclerView playerLists;

    private Context mContext;

    private List<PlayerBean> mPlayerBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_football);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.transblue), 0);
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
        adapter.setOnRVItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL, false);
        playerLists.setLayoutManager(manager);
        playerLists.setAdapter(adapter);
        adapter.setData(mPlayerBeanList);
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
        new BitmapTask().execute(position);
    }


    private class BitmapTask extends AsyncTask<Integer, Void, Bitmap> {

        int position=0;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mGameView.updatePlayer(bitmap, mPlayerBeanList.get(position).getName());
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            position = params[0];
            String url = mPlayerBeanList.get(position).getPerson_img();
            Bitmap mBitmap = null;
            try {
                URL iconUrl = new URL(url);
                URLConnection conn = iconUrl.openConnection();
                HttpURLConnection http = (HttpURLConnection) conn;
                int length = http.getContentLength();
                conn.connect();
                // 获得图像的字符流
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, length);
                mBitmap = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();// 关闭流
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mBitmap;
        }
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
