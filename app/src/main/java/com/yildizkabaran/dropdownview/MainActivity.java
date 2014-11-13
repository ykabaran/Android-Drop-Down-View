package com.yildizkabaran.dropdownview;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.yildizkabaran.dropdownview.adapter.DemoPagerAdapter;

public class MainActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new DemoPagerAdapter(getSupportFragmentManager(), this));
  }
}
