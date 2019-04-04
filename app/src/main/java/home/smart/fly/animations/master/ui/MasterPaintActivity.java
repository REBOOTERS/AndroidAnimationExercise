package home.smart.fly.animations.master.ui;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import home.smart.fly.animations.R;
import home.smart.fly.animations.master.Tools;
import home.smart.fly.animations.master.adapter.ModeSelectorAdapter;
import home.smart.fly.animations.master.widget.MasterPaintView;

public class MasterPaintActivity extends AppCompatActivity {
    private static final String TAG = "MasterPaintActivity";
    @BindView(R.id.master)
    MasterPaintView master;
    @BindView(R.id.modeList)
    RecyclerView modeList;

    private List<Integer> mIntegers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_paint);
        ButterKnife.bind(this);
        setTitle("MasterPaint");

        ModeSelectorAdapter mModeSelectorAdapter = new ModeSelectorAdapter(modeList);
        modeList.setLayoutManager(new LinearLayoutManager(this));
        modeList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        modeList.setAdapter(mModeSelectorAdapter);
        mModeSelectorAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                PorterDuff.Mode mMode = Tools.intToMode(position);
                master.drawComposeShader(mMode);
                Log.e(TAG, "onRVItemClick: mode==" + mMode);
            }
        });

        for (int i = 0; i < 16; i++) {
            mIntegers.add(i);
        }

        mModeSelectorAdapter.setData(mIntegers);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        modeList.setVisibility(View.INVISIBLE);
        switch (item.getItemId()) {
            case R.id.linearGradient:
                master.drawLinearGradient();
                break;
            case R.id.radialGradient:
                master.drawRadialsGradient();
                break;
            case R.id.sweepGradient:
                master.drawSweepGradient();
                break;
            case R.id.bitmapShader:
                master.drawBitmapShader();
                break;
            case R.id.composeShader:
                modeList.setVisibility(View.VISIBLE);
                master.drawComposeShader(PorterDuff.Mode.SRC);
                break;
            default:
                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master_menu, menu);
        return true;
    }


}
