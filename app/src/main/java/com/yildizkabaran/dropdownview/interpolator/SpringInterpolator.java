package com.yildizkabaran.dropdownview.interpolator;

import android.animation.TimeInterpolator;

/**
 * Created by yildizkabaran on 13.11.2014.
 */
public class SpringInterpolator implements TimeInterpolator {

  private static final String TAG = SpringInterpolator.class.getSimpleName();

  private static final float DEF_BOUNCE = 16F;
  private static final int DEF_SMOOTH = 4;

  private final float bounciness;
  private final int smoothness;

  public SpringInterpolator(){
    bounciness = DEF_BOUNCE;
    smoothness = DEF_SMOOTH;
  }

  public SpringInterpolator(float bounciness, int smoothness){
    this.bounciness = bounciness;
    this.smoothness = smoothness;
  }

  @Override
  public float getInterpolation(float input) {
    return (float) (1F - Math.pow(1F - input, smoothness) * Math.cos(bounciness * input));
  }
}
