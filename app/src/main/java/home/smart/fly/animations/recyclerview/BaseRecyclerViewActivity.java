package home.smart.fly.animations.recyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import home.smart.fly.animations.R;

public class BaseRecyclerViewActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private StaggeredGridFragment mGridViewFragment;
    private SimpleRecyclerViewFragment mSimpleRecyclerViewFragment;
    private VegaRecyclerViewFragment mVegaRecyclerViewFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mSimpleRecyclerViewFragment == null) {
                        mSimpleRecyclerViewFragment = new SimpleRecyclerViewFragment();
                    }
                    mFragmentTransaction.replace(R.id.content, mSimpleRecyclerViewFragment);
                    mFragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    if (mGridViewFragment == null) {
                        mGridViewFragment = new StaggeredGridFragment();
                    }
                    mFragmentTransaction.replace(R.id.content, mGridViewFragment);
                    mFragmentTransaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    if (mVegaRecyclerViewFragment == null) {
                        mVegaRecyclerViewFragment = new VegaRecyclerViewFragment();
                    }
                    mFragmentTransaction.replace(R.id.container, mVegaRecyclerViewFragment);
                    mFragmentTransaction.commit();
                    return true;


            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycler_view);
        mFragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }


}
