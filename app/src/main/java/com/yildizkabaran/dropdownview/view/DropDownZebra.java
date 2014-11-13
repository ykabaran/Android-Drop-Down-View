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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.yildizkabaran.dropdownview.interpolator.SpringInterpolator;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class DropDownZebra extends DropDownView {

  private static final String TAG = DropDownZebra.class.getSimpleName();

  public DropDownZebra(Context context) {
    super(context);
  }

  public DropDownZebra(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DropDownZebra(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DropDownZebra(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private static final int EXPAND_ANIM_DUR = 1000;
  private static final int COLLAPSE_ANIM_DUR = 500;
  private TimeInterpolator expandInterpolator = new SpringInterpolator(16F, 4);
  private TimeInterpolator collapseInterpolator = new OvershootInterpolator();
  private TimeInterpolator selectedInterpolator = new DecelerateInterpolator();
  private float selectedViewStartY = 0F;
  int width;

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
      public void onAnimationStart(Animator animation) {
        int selectedIndex = getSelectedIndex();
        int height = getSelectedView().getMeasuredHeight() + 2;

        final int numItems = getAdapter().getCount();
        for (int i = 0; i < numItems; ++i) {
          final View itemView = getViewAtIndex(i);
          if(i != selectedIndex) {
            int yPos = i < selectedIndex ? i + 1 : i;
            itemView.setTranslationY(yPos * height);
          }
        }
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        onExpanded();
      }
    });
    animator.start();
  }

  @Override
  protected void onCollapse() {
    selectedViewStartY = getSelectedView().getTranslationY();

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

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    width = getMeasuredWidth();
  }

  private void expandDropDown(float amount) {
    int selectedIndex = getSelectedIndex();

    float interpolatedAmount = 1 - expandInterpolator.getInterpolation(amount);
    final int numItems = getAdapter().getCount();
    for (int i = 0; i < numItems; ++i) {
      final View itemView = getViewAtIndex(i);
      if(i != selectedIndex) {
        if(amount < 0.5F) {
          itemView.setAlpha(amount + 0.5F);
        } else {
          itemView.setAlpha(1F);
        }
        itemView.setTranslationX(width * interpolatedAmount * (i % 2 == 0 ? -1 : 1));
      }
    }
  }

  private void collapseDropDown(float amount){
    int selectedIndex = getSelectedIndex();

    float interpolatedAmount = 1 - collapseInterpolator.getInterpolation(amount);
    final int numItems = getAdapter().getCount();
    for (int i = 0; i < numItems; ++i) {
      final View itemView = getViewAtIndex(i);
      if(i != selectedIndex) {
        if(amount < 0.5F) {
          itemView.setAlpha(amount + 0.5F);
        } else {
          itemView.setAlpha(1F);
        }
        itemView.setTranslationX(width * interpolatedAmount * (i % 2 == 0 ? -1 : 1));
      }
    }

    View selected = getSelectedView();
    selected.setTranslationY(selectedViewStartY * (1 - selectedInterpolator.getInterpolation(1 - amount)));
  }
}
