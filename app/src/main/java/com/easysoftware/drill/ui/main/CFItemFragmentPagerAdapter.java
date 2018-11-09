package com.easysoftware.drill.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.easysoftware.drill.R;

import java.util.List;

public class CFItemFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public CFItemFragmentPagerAdapter(List<Fragment> fragments, FragmentManager fm) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = mFragments.get(position);
        if (fragment instanceof CFItemFragment) {
            return ((CFItemFragment) fragment).getTitle();
        }
        return null;
    }
}
