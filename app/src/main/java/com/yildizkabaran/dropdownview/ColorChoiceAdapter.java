package com.yildizkabaran.dropdownview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class ColorChoiceAdapter extends ArrayAdapter<ColorChoice> {

  private static final String TAG = ColorChoiceAdapter.class.getSimpleName();
  private static final int LAYOUT_ID = R.layout.category_item;

  public ColorChoiceAdapter(Context context) {
    super(context, LAYOUT_ID);
  }

  public ColorChoiceAdapter(Context context, List<ColorChoice> objects) {
    super(context, LAYOUT_ID, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    CategoryViewHolder viewHolder;
    if(view == null){
      view = LayoutInflater.from(getContext()).inflate(LAYOUT_ID, parent, false);
      CategoryViewHolder.init(view);
    }

    viewHolder = CategoryViewHolder.from(view);
    ColorChoice colorChoice = getItem(position);

    viewHolder.image.setImageResource(colorChoice.drawableId);
    viewHolder.title.setText(colorChoice.textId);

    return view;
  }

  private static class CategoryViewHolder {

    private static final int TAG_KEY = R.id.category_view_holder_tag;

    public ImageView image;
    public TextView title;

    private CategoryViewHolder(View view){
      image = (ImageView) view.findViewById(R.id.image);
      title = (TextView) view.findViewById(R.id.title);
    }

    public static void init(View view){
      CategoryViewHolder viewHolder = new CategoryViewHolder(view);
      view.setTag(TAG_KEY, viewHolder);
    }

    public static CategoryViewHolder from(View view){
      return (CategoryViewHolder) view.getTag(TAG_KEY);
    }
  }

}
