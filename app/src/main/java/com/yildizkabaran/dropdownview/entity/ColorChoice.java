package com.yildizkabaran.dropdownview.entity;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class ColorChoice {

  private static final String TAG = ColorChoice.class.getSimpleName();

  public final int colorId;
  public final int drawableId;
  public final int textId;

  public ColorChoice(int colorId, int drawableId, int textId) {
    this.colorId = colorId;
    this.drawableId = drawableId;
    this.textId = textId;
  }
}
