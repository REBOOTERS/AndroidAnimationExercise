package home.smart.fly.animationdemo.customview.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import home.smart.fly.animationdemo.R;

/**
 * Created by cjj on 2016/2/29.
 */
public class BottomSheetDialogActivity extends AppCompatActivity {
    private static final String shareStr[] = {
            "微信","QQ","空间","微博","GitHub","CJJ测试\nRecyclerView自适应","微信朋友圈","短信","推特","遇见"
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBSDialog();
            }
        });
    }

    private void showBSDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bs_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleStringRecyclerViewAdapter adapter = new SimpleStringRecyclerViewAdapter(this);
        adapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                dialog.dismiss();
                Toast.makeText(BottomSheetDialogActivity.this, "pos--->" + pos, Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.setContentView(view);
        dialog.show();
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

            holder.mTextView.setText(shareStr[position]);
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return shareStr.length;
        }
    }
}
