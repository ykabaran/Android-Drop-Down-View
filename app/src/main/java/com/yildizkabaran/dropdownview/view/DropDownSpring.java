package com.yildizkabaran.dropdownview.view;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class DropDownSpring extends DropDownView {

  private static final String TAG = DropDownSpring.class.getSimpleName();

  public DropDownSpring(Context context) {
    super(context);
  }

  public DropDownSpring(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DropDownSpring(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DropDownSpring(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private TimeInterpolator interpolator = new AnticipateInterpolator();

  @Override
  protected void displayDropDown(float amount) {
    float interpolatedAmount = interpolator.getInterpolation(1F - amount);
    final int numItems = getAdapter().getCount();
    for (int i = 0; i < numItems; ++i) {
      final View itemView = getDropDownView(i);
      if(itemView != null) {
        itemView.setTranslationY(-itemView.getTop() * interpolatedAmount);
      }
    }

    View pending = getPendingSelectionView();
    if(pending != null){
      pending.setTranslationY(-pending.getTop() * interpolatedAmount);
    }

  }
}
