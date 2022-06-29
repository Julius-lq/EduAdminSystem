package com.zyh.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.zyh.R;
import com.zyh.adapter.BgAdapter;

import java.util.Objects;

public class BackgroundActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ViewPager viewPager;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.root)
//    ViewGroup root;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
//        ButterKnife.bind(this);
        initViews();

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.appbarColor)
                .navigationBarColor(R.color.backgroundColor)
                .autoDarkModeEnable(true)
                .init();

        toolbar.setTitle("主页背景");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewPager.setAdapter(new BgAdapter(getApplicationContext(), getSupportFragmentManager(), 2));
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
    }
}