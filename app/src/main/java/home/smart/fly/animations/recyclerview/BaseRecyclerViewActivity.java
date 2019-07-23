package home.smart.fly.animations.recyclerview;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.fragments.SimpleRecyclerViewFragment;
import home.smart.fly.animations.recyclerview.fragments.StaggeredGridFragment;
import home.smart.fly.animations.recyclerview.fragments.VegaRecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseRecyclerViewActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private List<Fragment> mFragments;
    private int mLastFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycler_view);
        mFragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragments();

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new SimpleRecyclerViewFragment());
        mFragments.add(new StaggeredGridFragment());
        mFragments.add(new VegaRecyclerViewFragment());

        mLastFragment = 0;
        mFragmentManager.beginTransaction()
                .add(R.id.container, mFragments.get(0))
                .show(mFragments.get(0))
                .commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (mLastFragment != 0) {
                    updateFragment(mLastFragment, 0);
                }
                return true;
            case R.id.navigation_dashboard:
                if (mLastFragment != 1) {
                    updateFragment(mLastFragment, 1);
                }
                return true;
            case R.id.navigation_notifications:
                if (mLastFragment != 2) {
                    updateFragment(mLastFragment, 2);
                }
                return true;
            default:


        }
        return false;
    };

    private void updateFragment(int last, int i) {
        mLastFragment = i;
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.hide(mFragments.get(last));
        mFragmentTransaction.replace(R.id.container, mFragments.get(i));
        mFragmentTransaction.commit();
    }


}
