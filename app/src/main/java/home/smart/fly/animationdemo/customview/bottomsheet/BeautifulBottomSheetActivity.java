package home.smart.fly.animationdemo.customview.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import home.smart.fly.animationdemo.R;


/**
 * Created by cjj on 2016/2/29.
 */
public class BeautifulBottomSheetActivity extends AppCompatActivity {
    public BottomSheetBehavior behavior;
    public RecyclerView recyclerView;
    public View ns_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beautiful);
        // The View with the BottomSheetBehavior
        ns_view = findViewById(R.id.ns);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SimpleStringRecyclerViewAdapter simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(this);

        simpleStringRecyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Toast.makeText(BeautifulBottomSheetActivity.this, "pos--->" + pos, Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(simpleStringRecyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        behavior = BottomSheetBehavior.from(ns_view);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED||newState == BottomSheetBehavior.STATE_HIDDEN){
//                    blackView.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        public ItemClickListener mItemClickListener;

        public void setItemClickListener(ItemClickListener listener) {
            mItemClickListener = listener;
        }

        public interface ItemClickListener {
            public void onItemClick(int pos);
        }

        private Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(R.id.tv);
            }


        }

        public SimpleStringRecyclerViewAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_beau, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "pos --->" + position, Toast.LENGTH_SHORT).show();
                    mItemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

}
