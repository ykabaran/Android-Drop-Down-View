package com.yildizkabaran.dropdownview.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yildizkabaran.dropdownview.R;

/**
 * Created by yildizkabaran on 13.11.2014.
 */
public class ZebraDemoFragment extends BaseDemoFragment {

  private static final String TAG = ZebraDemoFragment.class.getSimpleName();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_zebra, container, false);
  }
}
