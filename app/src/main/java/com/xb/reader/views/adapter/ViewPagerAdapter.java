package com.xb.reader.views.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by asus on 2017/7/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fragmentManager,ArrayList<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments == null?null:fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null?0:fragments.size();
    }
}
