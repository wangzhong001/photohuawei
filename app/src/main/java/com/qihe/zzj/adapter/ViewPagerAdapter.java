package com.qihe.zzj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mListFragment;
    private List<String> tableTiles;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> mListFragment, List<String> tableTiles) {
        super(fm);
        this.mListFragment = mListFragment;
        this.tableTiles = tableTiles;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tableTiles.get(position);
    }
}
