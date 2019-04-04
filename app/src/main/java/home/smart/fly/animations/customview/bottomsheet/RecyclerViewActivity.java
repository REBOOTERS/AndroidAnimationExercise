package home.smart.fly.animations.customview.bottomsheet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import home.smart.fly.animations.R;

/**
 * Created by cjj on 2016/2/24.
 */
public class RecyclerViewActivity extends AppCompatActivity {

    public BottomSheetBehavior behavior;
    public RecyclerView recyclerView;
    public View blackView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_rv);
        // The View with the BottomSheetBehavior
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        SimpleStringRecyclerViewAdapter simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(this);

        simpleStringRecyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Toast.makeText(RecyclerViewActivity.this, "pos--->" + pos, Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(simpleStringRecyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        behavior = BottomSheetBehavior.from(recyclerView);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED||newState == BottomSheetBehavior.STATE_HIDDEN){
//                    blackView.setBackgroundColor(Color.TRANSPARENT);
                    blackView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
                blackView.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(blackView,slideOffset);
            }
        });

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        blackView = findViewById(R.id.blackview);
        blackView.setBackgroundColor(Color.parseColor("#60000000"));
        blackView.setVisibility(View.GONE);
        blackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
                    .inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            if (position == 0) {
//                holder.mImageView.setImageResource(R.drawable.a6);
//            } else if (position == 1) {
//                holder.mImageView.setImageResource(R.drawable.a5);
//            }
//
//            holder.mImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(mContext, "pos --->" + position, Toast.LENGTH_SHORT).show();
//                    mItemClickListener.onItemClick(position);
//                }
//            });

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
            return 10;
        }
    }

}
