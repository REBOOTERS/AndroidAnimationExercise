package home.smart.fly.animations.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.transformationlayout.TransformationLayout;
import com.skydoves.transformationlayout.TransitionExtensionKt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.PlayerBean;
import home.smart.fly.animations.customview.views.BallGameView;
import home.smart.fly.animations.ui.SimpleBaseActivity;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.Tools;

public class FakeFootballActivity extends SimpleBaseActivity {
    private static final String TAG = "FakeFootballActivity";
    @BindView(R.id.gameView)
    public BallGameView mGameView;
    @BindView(R.id.playerLists)
    RecyclerView playerLists;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.content)
    FrameLayout content;
    private Context mContext;
    private PlayAdapter adapter;


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

    @OnClick({R.id.back, R.id.next})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                mGameView.clearInvalidView();

                mGameView.setDrawingCacheEnabled(true);
                Bitmap mBitmap = mGameView.getDrawingCache();

                if (mBitmap != null) {
                    new SavePicTask().execute(mBitmap);
                } else {
                    Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
                }


                break;
            default:
                break;
        }
    }

    private class SavePicTask extends AsyncTask<Bitmap, Void, String> {


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)) {
                Intent mIntent = new Intent(mContext, GameViewSaveActivity.class);
                mIntent.putExtra("picUrl", s);
                mContext.startActivity(mIntent);
            } else {
                Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap mBitmap = params[0];
            String filePath = "";
            Calendar now = new GregorianCalendar();
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String fileName = simpleDate.format(now.getTime());
            //保存在应用内目录，免去申请读取权限的麻烦

            File mFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName + ".jpg");
            try {
                OutputStream mOutputStream = new FileOutputStream(mFile);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mOutputStream);
                mOutputStream.flush();
                mOutputStream.close();
                filePath = mFile.getAbsolutePath();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return filePath;
        }
    }


    private void initData() {
        String json = Tools.readStrFromAssets("player.json", mContext);
        Gson mGson = new Gson();
        mPlayerBeanList = mGson.fromJson(json, new TypeToken<List<PlayerBean>>() {
        }.getType());

        adapter = new PlayAdapter(mPlayerBeanList);
        GridLayoutManager manager = new GridLayoutManager(mContext,
                2, LinearLayoutManager.HORIZONTAL, false);
        playerLists.setLayoutManager(manager);
        playerLists.setAdapter(adapter);
        adapter.setOnRVItemClickListener(new PlayAdapter.OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                int mBubblePosition = mGameView.getCurrentPos();

                if (mBubblePosition == -1) {
                    Toast.makeText(mContext, "先点击气泡，再添加球员", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (mPlayerBeanList.get(position).isSelected()) {
                    Toast.makeText(mContext, "球员已被选择!", Toast.LENGTH_SHORT).show();
                } else {


                    View avatar = itemView.findViewById(R.id.img);


                    int width = avatar.getWidth();
                    int height = avatar.getHeight();
                    Bitmap bitmap = Tools.View2Bitmap(avatar, width, height);
                    int[] location = new int[2];
                    itemView.getLocationOnScreen(location);
                    if (bitmap != null) {
                        mGameView.updatePlayer(bitmap, mPlayerBeanList.get(position).getName(), location, content);
                        for (int i = 0; i < mPlayerBeanList.size(); i++) {
                            if (mPlayerBeanList.get(i).getPosition() == mBubblePosition) {
                                //同一个位置，先把上次选中的球员，设置为未选中
                                mPlayerBeanList.get(i).setSelected(false);
                            }
                        }
                        //将此次更新的球员设置为气泡上选中的球员
                        mPlayerBeanList.get(position).setSelected(true);
                        mPlayerBeanList.get(position).setPosition(mBubblePosition);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }


    private static class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.MyHolder> {


        public interface OnRVItemClickListener {
            void onRVItemClick(ViewGroup parent, View itemView, int position);
        }

        private OnRVItemClickListener mOnRVItemClickListener;

        public void setOnRVItemClickListener(OnRVItemClickListener onRVItemClickListener) {
            mOnRVItemClickListener = onRVItemClickListener;
        }

        private List<PlayerBean> mPlayerBeanList;
        private Context mContext;

        public PlayAdapter(List<PlayerBean> playerBeanList) {
            mPlayerBeanList = playerBeanList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dqd_player_item, parent, false);
            MyHolder myHolder = new MyHolder(view);
            view.setOnClickListener(v -> {
                if (mOnRVItemClickListener != null) {
                    mOnRVItemClickListener.onRVItemClick(parent, view, myHolder.getAdapterPosition());
                }
            });
            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            PlayerBean model = mPlayerBeanList.get(position);
            holder.mPlayerName.setText(model.getName());
            Glide.with(mContext).load(model.getPerson_img())
//                    .apply(new RequestOptions().error(R.drawable.messi))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                           if(e!=null){
                               Log.e(TAG, "onLoadFailed: "+e.getMessage() );
                               Log.e(TAG, "onLoadFailed: "+e.fillInStackTrace());
                           }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.mPlayerImg);
            if (model.isSelected()) {
                holder.mPlayerSel.setImageResource(R.drawable.battle_player_state_checked);
            } else {
                holder.mPlayerSel.setImageResource(R.drawable.battle_player_state_unchecked);
            }
        }

        @Override
        public int getItemCount() {
            return mPlayerBeanList.size();
        }

        static class MyHolder extends RecyclerView.ViewHolder {
            private TextView mPlayerName;
            private ImageView mPlayerImg;
            private ImageView mPlayerSel;

            public MyHolder(View itemView) {
                super(itemView);
                mPlayerName = itemView.findViewById(R.id.name);
                mPlayerImg = itemView.findViewById(R.id.img);
                mPlayerSel = itemView.findViewById(R.id.isSel);
            }
        }
    }

}
