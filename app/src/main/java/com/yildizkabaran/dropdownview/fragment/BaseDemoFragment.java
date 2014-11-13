package com.yildizkabaran.dropdownview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.yildizkabaran.dropdownview.entity.ColorChoice;
import com.yildizkabaran.dropdownview.adapter.ColorChoiceAdapter;
import com.yildizkabaran.dropdownview.R;
import com.yildizkabaran.dropdownview.view.DropDownView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class BaseDemoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

  private static final String TAG = BaseDemoFragment.class.getSimpleName();
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

  private View rootView;
  private DropDownView dropDown;

  @Override
  public void onDestroyView() {
    dropDown = null;
    rootView = null;

    super.onDestroyView();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    rootView = view;
    dropDown = (DropDownView) view.findViewById(R.id.drop_down_view);
    dropDown.setOnItemSelectedListener(this);
    dropDown.setAdapter(new ColorChoiceAdapter(getActivity(), COLOR_CHOICES));
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    rootView.setBackgroundResource(COLOR_CHOICES.get(position).colorId);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

}
