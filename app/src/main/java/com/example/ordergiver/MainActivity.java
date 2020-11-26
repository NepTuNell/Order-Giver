package com.example.ordergiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.ordergiver.fragments.HomeTab;
import com.example.ordergiver.fragments.OrderTab;
import com.example.ordergiver.java.Adapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewVisible;
    private PagerAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        this.viewVisible = findViewById(R.id.view_visible);
        this.pageAdapter = new Adapter(getSupportFragmentManager(), fragmentList);
        this.viewVisible.setAdapter(this.pageAdapter);
    }

}