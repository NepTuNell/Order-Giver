package com.example.ordergiver.java;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


public class Adapter extends FragmentPagerAdapter
{
    //****************************
    // Attributes
    //****************************

    private List<Fragment> mTabList;

    // Constructor
    public Adapter(FragmentManager manager, List<Fragment> tabList)
    {
        super(manager);
        mTabList = tabList;
    }

    //****************************
    // Methods
    //****************************

    @Override
    public Fragment getItem(int position)
    {
        return mTabList.get(position);
    }

    @Override
    public int getCount()
    {
        return mTabList.size();
    }
}
