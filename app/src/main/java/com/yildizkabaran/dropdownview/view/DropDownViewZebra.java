package com.yildizkabaran.dropdownview.view;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.yildizkabaran.dropdownview.BuildConfig;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class DropDownViewZebra extends DropDownView {

  private static final String TAG = DropDownViewZebra.class.getSimpleName();

  public DropDownViewZebra(Context context) {
    super(context);
  }

  public DropDownViewZebra(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DropDownViewZebra(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DropDownViewZebra(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private TimeInterpolator interpolator = new AnticipateInterpolator();

  @Override
  protected void displayDropDown(float amount) {
    int halfWidth = getMeasuredWidth() / 2;

    float interpolatedAmount = interpolator.getInterpolation(1F - amount);
    final int numItems = getAdapter().getCount();
    for (int i = 0; i < numItems; ++i) {
      final View itemView = getDropDownView(i);
      if(itemView != null) {
        itemView.setAlpha(amount);
        itemView.setTranslationX(halfWidth * interpolatedAmount * (i % 2 == 0 ? -1 : 1));
      }
    }

    View pending = getPendingSelectionView();
    if(pending != null){
      pending.setTranslationY(-pending.getTop() * (1F - amount));
    }
  }
}
