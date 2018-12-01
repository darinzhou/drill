package com.easysoftware.drill.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class CFItemFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<MainBasePresenter> mPresenters;
    private List<CFItemFragment> mFragments;
    private List<String> mTitles;

    public CFItemFragmentPagerAdapter(FragmentManager fm, List<MainBasePresenter> presenters,
                                      List<String> titles) {
        super(fm);
        mPresenters = presenters;
        mTitles = titles;
        mFragments = new ArrayList<>();
        for (MainBasePresenter p : presenters) {
            mFragments.add(CFItemFragment.newInstance(p));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mPresenters.size();
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void filter(String text) {
        for (CFItemFragment fragment : mFragments) {
            fragment.filter(text);
        }
    }

    // fragments were re-created by system, we just need to update their presenters
    public void update(ViewPager viewPager, List<MainBasePresenter> presenters) {
        mFragments.clear();
        for (int i = 0; i < presenters.size(); ++i) {
            CFItemFragment fragment = (CFItemFragment) instantiateItem(viewPager, i);
            fragment.update(presenters.get(i));
            mFragments.add(fragment);
        }
    }
}
