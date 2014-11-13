package com.yildizkabaran.dropdownview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.yildizkabaran.dropdownview.interpolator.SpringInterpolator;

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

  private static final int EXPAND_ANIM_DUR = 1500;
  private TimeInterpolator expandInterpolator = new SpringInterpolator(12F, 5);
  private static final int COLLAPSE_ANIM_DUR = 1000;
  private TimeInterpolator collapseInterpolator = new OvershootInterpolator();
  private TimeInterpolator selectedInterpolator = new BounceInterpolator();
  private float[] viewStartY;

  @Override
  protected void onExpand() {
    ValueAnimator animator = ValueAnimator.ofFloat(0F, 1F);
    animator.setDuration(EXPAND_ANIM_DUR);
    animator.setInterpolator(new LinearInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        expandDropDown((Float) animation.getAnimatedValue());
      }
    });
    animator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        onExpanded();
      }
    });
    animator.start();
  }

  @Override
  protected void onCollapse() {
    int numViews = getAdapter().getCount();
    viewStartY = new float[numViews];
    for(int i=0; i<numViews; ++i) {
      viewStartY[i] = getViewAtIndex(i).getTranslationY();
    }

    ValueAnimator animator = ValueAnimator.ofFloat(1F, 0F);
    animator.setDuration(COLLAPSE_ANIM_DUR);
    animator.setInterpolator(new LinearInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        collapseDropDown((Float) animation.getAnimatedValue());
      }
    });
    animator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        onCollapsed();
      }
    });
    animator.start();
  }

  private void expandDropDown(float amount) {
    int selectedIndex = getSelectedIndex();
    int height = getSelectedView().getMeasuredHeight() + 2;

    float interpolatedAmount = expandInterpolator.getInterpolation(amount);
    final int numItems = getAdapter().getCount();
    for (int i = 0; i < numItems; ++i) {
      final View itemView = getViewAtIndex(i);
      if(i != selectedIndex) {
        int yPos = i < selectedIndex ? i + 1 : i;
        itemView.setTranslationY(yPos * height * interpolatedAmount);
      }
    }
  }

  private void collapseDropDown(float amount){
    int selectedIndex = getSelectedIndex();

    float interpolatedAmount = collapseInterpolator.getInterpolation(amount);
    final int numItems = getAdapter().getCount();
    for (int i = 0; i < numItems; ++i) {
      final View itemView = getViewAtIndex(i);
      if(i != selectedIndex) {
        itemView.setTranslationY(viewStartY[i] * interpolatedAmount);
      }
    }

    View selected = getSelectedView();
    selected.setTranslationY(viewStartY[selectedIndex] * (1 - selectedInterpolator.getInterpolation(1 - amount)));
  }
}
