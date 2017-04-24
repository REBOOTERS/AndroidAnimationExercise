package home.smart.fly.animationdemo.customview.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animationdemo.R;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class RecyclerViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<String> datas;


    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("this is item " + i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MyAdapter());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            View mView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new MyHolder(mView);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.mTextView.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            public MyHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
