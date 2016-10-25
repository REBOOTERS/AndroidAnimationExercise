package home.smart.fly.animationdemo.property.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import home.smart.fly.animationdemo.R;


/**
 */
public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.MyViewHolder> {
    private ArrayList<GoodItem> mData;
    private LayoutInflater mLayoutInflater;
    private CallBackListener mCallBackListener;

    public GoodsListAdapter(Context context, ArrayList<GoodItem> mData) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mData = mData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_shopping_cart_item, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mShoppingCartItemIv.setImageBitmap(mData.get(position).getmGoodsBitmap());
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mShoppingCartItemIv;


        public MyViewHolder(View view) {
            super(view);
            mShoppingCartItemIv = (ImageView) view.findViewById(R.id.iv_shopping_cart_item);
            view.findViewById(R.id.tv_shopping_cart_item).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mShoppingCartItemIv != null && mCallBackListener != null)
                                mCallBackListener.callBackImg(mShoppingCartItemIv);
                        }
                    });
        }


    }


    public void setCallBackListener(CallBackListener mCallBackListener) {
        this.mCallBackListener = mCallBackListener;
    }

    public interface CallBackListener {
        void callBackImg(ImageView goodsImg);
    }
}
