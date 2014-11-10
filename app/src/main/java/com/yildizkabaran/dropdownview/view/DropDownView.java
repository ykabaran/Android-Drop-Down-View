package com.yildizkabaran.dropdownview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public abstract class DropDownView extends AdapterView<SpinnerAdapter> implements
    View.OnClickListener {

  private static final String TAG = DropDownView.class.getSimpleName();

  public DropDownView(Context context) {
    super(context);
    initialize();
  }

  public DropDownView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DropDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize();
  }

  private ValueAnimator expander, collapser;
  private TimeInterpolator linearInterpolator = new LinearInterpolator();

  private static final int EXPAND_DURATION = 5000;
  private static final int STATE_COLLAPSED = 0;
  private static final int STATE_EXPANDED = 2;
  private static final int STATE_EXPANDING = 1;
  private static final int STATE_COLLAPSING = 3;
  private int state = STATE_COLLAPSED;

  private SpinnerAdapter adapter;

  private View collapsedView, pendingSelectionView;
  private SparseArray<View> dropDownViews = new SparseArray<View>();
  private int selectedIndex = -1, pendingSelection = -1;

  private void initialize(){
    collapser = ValueAnimator.ofFloat(1F, 0F);
    collapser.setInterpolator(linearInterpolator);
    collapser.setDuration(EXPAND_DURATION);
    collapser.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        state = STATE_COLLAPSING;
        int childCount = adapter.getCount();
        for (int i = 0; i < childCount; ++i) {
          final View v = dropDownViews.get(i);
          if (v != null) {
            bringChildToFront(v);
          }
        }
        if(pendingSelectionView != null) {
          bringChildToFront(pendingSelectionView);
        } else {
          bringChildToFront(collapsedView);
        }
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        setSelection(pendingSelection);
        state = STATE_COLLAPSED;
      }
    });
    collapser.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        float currVal = (Float) animation.getAnimatedValue();
        displayDropDown(currVal);
      }
    });

    expander = ValueAnimator.ofFloat(0F, 1F);
    expander.setInterpolator(linearInterpolator);
    expander.setDuration(EXPAND_DURATION);
    expander.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        state = STATE_EXPANDING;
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        state = STATE_EXPANDED;
      }
    });
    expander.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        float currVal = (Float) animation.getAnimatedValue();
        displayDropDown(currVal);
      }
    });
  }

  @Override
  public SpinnerAdapter getAdapter() {
    return adapter;
  }

  @Override
  public void setAdapter(SpinnerAdapter adapter) {
    this.adapter = adapter;
    initializeViewsWithAdapter();
  }

  @Override
  public View getSelectedView() {
    return collapsedView;
  }

  private void animateSelection(int index){
    pendingSelectionView = dropDownViews.get(index);
    dropDownViews.remove(index);
    dropDownViews.put(selectedIndex, collapsedView);
    pendingSelection = index;

    collapse();
  }

  @Override
  public void setSelection(int position) {
    if(selectedIndex == position || pendingSelectionView == null){
      return;
    }
    selectedIndex = position;
    collapsedView = pendingSelectionView;
    pendingSelectionView = null;
    performSelection();
  }

  private void performSelection(){
    OnItemSelectedListener listener = getOnItemSelectedListener();
    if(listener == null){
      return;
    }
    listener.onItemSelected(this, collapsedView, selectedIndex, collapsedView.getId());
  }

  private void initializeViewsWithAdapter() {
    removeAllViewsInLayout();
    dropDownViews.clear();

    if (adapter == null || adapter.getCount() < 1) {
      selectedIndex = -1;
      return;
    }

    final int numItems = adapter.getCount();
    for (int i = 1; i < numItems; ++i) {
      final View itemView = adapter.getView(i, null, this);
      itemView.setOnClickListener(this);
      dropDownViews.put(i, itemView);
    }
    for (int i = numItems - 1; i > 0; --i) {
      final View v = dropDownViews.get(i);
      addViewInLayout(v, -1, v.getLayoutParams());
    }

    selectedIndex = 0;
    collapsedView = adapter.getView(selectedIndex, null, this);
    collapsedView.setOnClickListener(this);
    addViewInLayout(collapsedView, -1, collapsedView.getLayoutParams());
    post(new Runnable() {
      @Override
      public void run() {
        displayDropDown(0F);
      }
    });
    performSelection();
  }

  @Override
  public void onClick(View v) {
    if(state == STATE_EXPANDING || state == STATE_COLLAPSING){
      return;
    }

    if (v == collapsedView) {
      if (state == STATE_COLLAPSED) {
        expand();
      } else if (state == STATE_EXPANDED) {
        collapse();
      }
    } else {
      int arrIndex = dropDownViews.indexOfValue(v);
      int adapterIndex = dropDownViews.keyAt(arrIndex);
      animateSelection(adapterIndex);
    }
  }

  private int width;

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    width = getMeasuredWidth();
    measureChildren(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if(adapter == null || adapter.getCount() < 1){
      return;
    }

    int topEdge = positionChild(collapsedView, 0);
    int childCount = adapter.getCount();
    for (int i=0; i<childCount; ++i) {
      final View v = dropDownViews.get(i);
      if(v != null && v != collapsedView) {
        topEdge = positionChild(v, topEdge + 2);
      } else if(pendingSelectionView != null && i == pendingSelection){
        topEdge += pendingSelectionView.getMeasuredHeight() + 2;
      }
    }
  }

  private int positionChild(View child, int top) {
    int childWidth = child.getMeasuredWidth();
    int childLeft = (width - childWidth) / 2;
    int newTop = top + child.getMeasuredHeight();
    child.layout(childLeft, top, childLeft + childWidth, newTop);
    return newTop;
  }

  private void expand() {
    if (state == STATE_EXPANDING || state == STATE_EXPANDED) {
      return;
    }
    if (collapser != null && collapser.isStarted()) {
      collapser.cancel();
    }

    expander.start();
  }

  private void collapse() {
    if (state == STATE_COLLAPSING || state == STATE_COLLAPSED) {
      return;
    }
    if (expander != null && expander.isStarted()) {
      expander.cancel();
    }

    collapser.start();
  }

  protected View getDropDownView(int index) {
    return dropDownViews.get(index);
  }

  protected View getPendingSelectionView(){
    return pendingSelectionView;
  }

  protected abstract void displayDropDown(float amount);
}
