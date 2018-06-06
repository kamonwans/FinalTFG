package com.project.finalyear.thaispellinggame.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.fragment.HomeFragment;
import com.project.finalyear.thaispellinggame.fragment.RankFragment;
import com.project.finalyear.thaispellinggame.fragment.UserOnlineFragment;


public class ViewPagerAdapter extends Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    private static int NUM_ITEMS = 3;
    public int[] tabIcons = {
            R.drawable.icon_home,
            R.drawable.icon_friends,
            R.drawable.icon_badge
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_page, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new sliderAdapter(getChildFragmentManager()));
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

        return view;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private class sliderAdapter extends FragmentPagerAdapter {

        public sliderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    CheckInternet();
                    return new UserOnlineFragment();

                case 2:
                    CheckInternet();
                    return new RankFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    public void CheckInternet() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ตด้วยค่ะ !", Toast.LENGTH_SHORT).show();

        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}

