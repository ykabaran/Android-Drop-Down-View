package com.yildizkabaran.dropdownview.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yildizkabaran.dropdownview.fragment.SpringDemoFragment;
import com.yildizkabaran.dropdownview.fragment.ZebraDemoFragment;

import java.util.ArrayList;

/**
 * Created by yildizkabaran on 13.11.2014.
 */
public class DemoPagerAdapter extends FragmentPagerAdapter {

  private static final String TAG = DemoPagerAdapter.class.getSimpleName();
  private final Context context;

  private static final ArrayList<String> PAGE_NAMES = new ArrayList<String>();

  static {
    PAGE_NAMES.add(SpringDemoFragment.class.getName());
    PAGE_NAMES.add(ZebraDemoFragment.class.getName());
  }

  public DemoPagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;
  }

  @Override
  public int getCount() {
    return PAGE_NAMES.size();
  }

  @Override
  public Fragment getItem(int position) {
    return Fragment.instantiate(context, PAGE_NAMES.get(position), null);
  }

}
