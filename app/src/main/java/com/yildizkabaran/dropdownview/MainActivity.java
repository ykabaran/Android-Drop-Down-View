package com.yildizkabaran.dropdownview;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.yildizkabaran.dropdownview.fragment.DemoFragment;


public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .replace(R.id.container, new DemoFragment())
          .disallowAddToBackStack()
          .commit();
    }

  }
}
