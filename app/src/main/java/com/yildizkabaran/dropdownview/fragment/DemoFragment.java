package com.yildizkabaran.dropdownview.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yildizkabaran.dropdownview.ColorChoice;
import com.yildizkabaran.dropdownview.ColorChoiceAdapter;
import com.yildizkabaran.dropdownview.view.DropDownView;
import com.yildizkabaran.dropdownview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class DemoFragment extends Fragment {

  private static final String TAG = DemoFragment.class.getSimpleName();
  private static final List<ColorChoice> COLOR_CHOICES = new ArrayList<ColorChoice>();
  static {
    COLOR_CHOICES.add(new ColorChoice(R.color.black, R.drawable.black_rect, R.string.choose_color));
    COLOR_CHOICES.add(new ColorChoice(R.color.gray, R.drawable.gray_rect, R.string.gray));
    COLOR_CHOICES.add(new ColorChoice(R.color.pink, R.drawable.pink_rect, R.string.pink));
    COLOR_CHOICES.add(new ColorChoice(R.color.red, R.drawable.red_rect, R.string.red));
    COLOR_CHOICES.add(new ColorChoice(R.color.orange, R.drawable.orange_rect, R.string.orange));
    COLOR_CHOICES.add(new ColorChoice(R.color.yellow, R.drawable.yellow_rect, R.string.yellow));
    COLOR_CHOICES.add(new ColorChoice(R.color.green, R.drawable.green_rect, R.string.green));
    COLOR_CHOICES.add(new ColorChoice(R.color.blue, R.drawable.blue_rect, R.string.blue));
  }

  private DropDownView dropDown;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

    dropDown = (DropDownView) rootView.findViewById(R.id.drop_down_view);
    dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rootView.setBackgroundResource(COLOR_CHOICES.get(position).colorId);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    dropDown.setAdapter(new ColorChoiceAdapter(getActivity(), COLOR_CHOICES));

    return rootView;
  }

}
