package home.smart.fly.animations.tradition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

public class VPAnim2Activity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final String TAG = "VPAnimActivity";
    private List<String> pics;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_vpanim2);
        initDatas();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setPageMargin(20);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new MyTranformer());
    }


    private class MyTranformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float CENTER = 0.5f;

        @SuppressLint("NewApi")
        public void transformPage(View view, float position) {
            int pageWidth = view.getMeasuredWidth();
            int pageHeight = view.getMeasuredHeight();

            Log.e("TAG", view + " , " + position + "");


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
                view.setPivotX(pageWidth);

            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]

                float scaleFactor;

                if (position < 0) {
                    scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 + position);

                } else {
                    scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - position);
                }


                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setPivotX(pageWidth * CENTER * (1 - position));


            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setPivotX(0);
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            }
        }
    }

    private void initDatas() {
        String data = Tools.readStrFromAssets("pics.data", mContext);
        pics = new Gson().fromJson(data, new TypeToken<List<String>>() {
        }.getType());
        pics = pics.subList(0, 20);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         *
         * @param sectionNumber
         */
        public static PlaceholderFragment newInstance(String sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_vpanim, container, false);
            ImageView section_label = (ImageView) rootView.findViewById(R.id.section_label);
            String url = getArguments().getString(ARG_SECTION_NUMBER);
            Glide.with(getContext()).load(url).into(section_label);
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
            String url = pics.get(position);
            return PlaceholderFragment.newInstance(url);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return pics.size();
        }
    }
}
