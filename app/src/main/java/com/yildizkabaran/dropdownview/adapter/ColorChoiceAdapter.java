package com.yildizkabaran.dropdownview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yildizkabaran.dropdownview.R;
import com.yildizkabaran.dropdownview.entity.ColorChoice;

import java.util.List;

/**
 * Created by yildizkabaran on 10.11.2014.
 */
public class ColorChoiceAdapter extends ArrayAdapter<com.yildizkabaran.dropdownview.entity.ColorChoice> {

  private static final String TAG = ColorChoiceAdapter.class.getSimpleName();
  private static final int LAYOUT_ID = R.layout.category_item;

  public ColorChoiceAdapter(Context context) {
    super(context, LAYOUT_ID);
  }

  public ColorChoiceAdapter(Context context, List<com.yildizkabaran.dropdownview.entity.ColorChoice> objects) {
    super(context, LAYOUT_ID, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    ColorChoiceViewHolder viewHolder;
    if(view == null){
      view = LayoutInflater.from(getContext()).inflate(LAYOUT_ID, parent, false);
      ColorChoiceViewHolder.init(view);
    }

    viewHolder = ColorChoiceViewHolder.from(view);
    ColorChoice colorChoice = getItem(position);

    viewHolder.image.setImageResource(colorChoice.drawableId);
    viewHolder.title.setText(colorChoice.textId);

    return view;
  }

  private static class ColorChoiceViewHolder {

    private static final int TAG_KEY = R.id.category_view_holder_tag;

    public ImageView image;
    public TextView title;

    private ColorChoiceViewHolder(View view){
      image = (ImageView) view.findViewById(R.id.image);
      title = (TextView) view.findViewById(R.id.title);
    }

    public static void init(View view){
      ColorChoiceViewHolder viewHolder = new ColorChoiceViewHolder(view);
      view.setTag(TAG_KEY, viewHolder);
    }

    public static ColorChoiceViewHolder from(View view){
      return (ColorChoiceViewHolder) view.getTag(TAG_KEY);
    }
  }

}
