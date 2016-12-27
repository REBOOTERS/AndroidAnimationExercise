package home.smart.fly.animationdemo.swipeanim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import home.smart.fly.animationdemo.R;

public class FakeWeiBoActivity extends AppCompatActivity {

    private PullToRefreshView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_wei_bo);
        InitView();
    }

    private void InitView() {
        refreshView = (PullToRefreshView) findViewById(R.id.refreshView);
        refreshView.setOnHeaderRefreshListener(new MyHeadListener());
        refreshView.setOnFooterRefreshListener(new MyFooterListener());



    }


    class MyHeadListener implements PullToRefreshView.OnHeaderRefreshListener {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            refreshView.onHeaderRefreshComplete();
        }
    }

    class MyFooterListener implements PullToRefreshView.OnFooterRefreshListener {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            refreshView.onFooterRefreshComplete();
        }
    }
}
