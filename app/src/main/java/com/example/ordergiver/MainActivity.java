package com.example.ordergiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.ordergiver.fragments.*;
import com.example.ordergiver.java.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //****************************
    // Attributes
    //****************************
    private ViewPager mViewVisible;
    private PagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initView();
    }

    // View configuration ----------------------
    private void initView()
    {
        // Add Tabs
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeTab());
        fragmentList.add(new OrderTab());

        // Config
        mViewVisible = findViewById(R.id.view_visible);
        mPageAdapter = new Adapter(getSupportFragmentManager(), fragmentList);
        mViewVisible.setAdapter(mPageAdapter);
    }

}