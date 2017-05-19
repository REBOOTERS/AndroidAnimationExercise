package home.smart.fly.animations.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.MyAdapter;
import home.smart.fly.animations.adapter.SchoolBeanShell;

public class CollegeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Context mContext;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private List<SchoolBeanShell> mBeanShells;
    private List<String> locations = new ArrayList<>();


    private ImageView headImage;
    private TextView schoolCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);
        mContext = this;

        //
        String json = getStringFromAssets("school.json");
        Gson mGson = new Gson();
        mBeanShells = mGson.fromJson(json, new TypeToken<ArrayList<SchoolBeanShell>>() {
        }.getType());

        for (int i = 0; i < mBeanShells.size(); i++) {
            locations.add(mBeanShells.get(i).getName());
        }

        final int[] pics = new int[]{R.drawable.a5, R.drawable.a6, R.drawable.cat};

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        headImage = (ImageView) findViewById(R.id.headImg);
        schoolCount = (TextView) findViewById(R.id.schoolCount);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                schoolCount.setText(String.valueOf(mBeanShells.get(position).getSchool().size()));
            }

            @Override
            public void onPageSelected(int position) {
                Glide.with(mContext).load(pics[position % pics.length]).placeholder(R.drawable.a6).into(headImage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    private String getStringFromAssets(String filename) {
        try {
            InputStream mInputStream = getAssets().open(filename);
            StringBuilder sb = new StringBuilder();
            int len = -1;
            byte[] buffer = new byte[mInputStream.available()];

            while ((len = mInputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len, "utf-8"));
            }
            mInputStream.close();
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_college, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_DATAS = "section_datas";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         *
         * @param sectionNumber
         */
        public static PlaceholderFragment newInstance(ArrayList<String> sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putStringArrayList(ARG_SECTION_DATAS, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_college, container, false);
            RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
//            NestedScrollView mNestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);

            List<String> datas = getArguments().getStringArrayList(ARG_SECTION_DATAS);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
            mLinearLayoutManager.setSmoothScrollbarEnabled(true);
            mLinearLayoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setNestedScrollingEnabled(false);
            MyAdapter mMyAdapter = new MyAdapter(datas);
            mRecyclerView.setAdapter(mMyAdapter);
            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            ArrayList<String> school = new ArrayList<>();
            for (int i = 0; i < mBeanShells.get(position).getSchool().size(); i++) {
                school.add(mBeanShells.get(position).getSchool().get(i).getName());
            }
            return PlaceholderFragment.newInstance(school);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return locations.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return locations.get(position);
        }
    }
}
