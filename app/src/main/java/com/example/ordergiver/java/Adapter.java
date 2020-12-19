package com.example.ordergiver.java;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/*
    Adapter Module for control tabs
    @nait-sab
*/

public class Adapter extends FragmentPagerAdapter
{
    // Composants
    private List<Fragment> _tabList;

    // Constructeur
    public Adapter(FragmentManager manager, List<Fragment> tabList)
    {
        super(manager);
        _tabList = tabList;
    }

    @Override
    public Fragment getItem(int position)
    {
        return _tabList.get(position);
    }

    @Override
    public int getCount()
    {
        return _tabList.size();
    }
}
