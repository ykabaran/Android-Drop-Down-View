package com.yildizkabaran.dropdownview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public abstract class DropDownView extends AdapterView<SpinnerAdapter> implements
    View.OnClickListener {

  private static final String TAG = DropDownView.class.getSimpleName();

  public DropDownView(Context context) {
    super(context);
  }

  public DropDownView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DropDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private static final int STATE_COLLAPSED = 0;
  private static final int STATE_EXPANDING = 1;
  private static final int STATE_EXPANDED = 2;
  private static final int STATE_COLLAPSING = 3;
  private int state = STATE_COLLAPSED;

  private SpinnerAdapter adapter;

  private final ArrayList<View> views = new ArrayList<View>();
  private int selectedIndex = -1;
  private int width;

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
    return views.get(selectedIndex);
  }

  @Override
  public void setSelection(int position) {
    if(selectedIndex == position){
      return;
    }
    selectedIndex = position;
    performSelection();
  }

  private void performSelection(){
    OnItemSelectedListener listener = getOnItemSelectedListener();
    if(listener == null){
      return;
    }
    View selectedView = getSelectedView();
    listener.onItemSelected(this, getSelectedView(), selectedIndex, selectedView.getId());
  }

  private void initializeViewsWithAdapter() {
    removeAllViewsInLayout();
    views.clear();

    final int numItems = (adapter == null) ? 0 : adapter.getCount();
    if (numItems < 1) {
      selectedIndex = -1;
      return;
    }

    for (int i = 0; i < numItems; ++i) {
      final View itemView = adapter.getView(i, null, this);
      itemView.setOnClickListener(this);
      views.add(itemView);
      addViewInLayout(itemView, -1, itemView.getLayoutParams());
    }
    selectedIndex = 0;
    zOrderChildren();
    performSelection();
  }

  private void zOrderChildren(){
    final int numItems = adapter.getCount();
    for (int i = numItems - 1; i >= 0; --i) {
      if(i != selectedIndex) {
        views.get(i).bringToFront();
      }
    }
    getSelectedView().bringToFront();
  }

  @Override
  public void onClick(View v) {
    if(state == STATE_EXPANDING || state == STATE_COLLAPSING){
      return;
    }

    int adapterIndex = views.indexOf(v);
    if(adapterIndex == selectedIndex){
      if(state == STATE_COLLAPSED){
        expand();
      } else {
        collapse();
      }
      return;
    }

    setSelection(adapterIndex);
    collapse();
  }

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

    int childCount = adapter.getCount();
    for (int i=0; i<childCount; ++i) {
      final View v = views.get(i);
      int childWidth = v.getMeasuredWidth();
      int childLeft = (width - childWidth) / 2;
      v.layout(childLeft, 0, childLeft + childWidth, v.getMeasuredHeight());
    }
  }

  private void expand() {
    if (state != STATE_COLLAPSED) {
      return;
    }

    state = STATE_EXPANDING;
    onExpand();
  }

  protected void onExpanded(){
    state = STATE_EXPANDED;
  }

  protected void onCollapsed(){
    state = STATE_COLLAPSED;
  }

  private void collapse() {
    if (state != STATE_EXPANDED) {
      return;
    }

    state = STATE_COLLAPSING;
    zOrderChildren();
    onCollapse();
  }

  protected int getSelectedIndex(){
    return selectedIndex;
  }

  protected View getViewAtIndex(int index) {
    return views.get(index);
  }

  protected abstract void onExpand();
  protected abstract void onCollapse();
}
