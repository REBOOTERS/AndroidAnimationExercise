package home.smart.fly.animations.recyclerview;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.fragments.SimpleRecyclerViewFragment;
import home.smart.fly.animations.recyclerview.fragments.StaggeredGridFragment;
import home.smart.fly.animations.recyclerview.fragments.TextListFragment;
import home.smart.fly.animations.recyclerview.fragments.VegaRecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseRecyclerViewActivity extends AppCompatActivity {
    private static final String TAG = "BaseRecyclerViewActivit";
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

        navigation.setSelectedItemId(R.id.navigation_simple);
    }

    // <editor-fold defaultstate="collapsed" desc="initFragments">
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new SimpleRecyclerViewFragment());
        mFragments.add(new StaggeredGridFragment());
        mFragments.add(new VegaRecyclerViewFragment());
        mFragments.add(new TextListFragment());

        mLastFragment = 0;
        mFragmentManager.beginTransaction()
                .add(R.id.container, mFragments.get(mLastFragment))
                .commit();

    }
    // </editor-fold>

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
            case R.id.navigation_simple:
                if (mLastFragment != 3) {
                    updateFragment(mLastFragment, 3);
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
