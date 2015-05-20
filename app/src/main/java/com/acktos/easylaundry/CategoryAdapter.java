package com.acktos.easylaundry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by OSCAR ACKTOS on 28/03/2015.
 */
public class CategoryAdapter extends BaseAdapter {

    private ArrayList<Clothing> clothings;
    private LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, ArrayList<Clothing> clothings){

        this.clothings=clothings;
        layoutInflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return clothings.size();
    }

    @Override
    public Object getItem(int position) {
        return clothings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.category_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.clothing_title);
            holder.image = (ImageView) convertView.findViewById(R.id.category_image);
            holder.price = (TextView) convertView.findViewById(R.id.clothing_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Clothing clothing = clothings.get(position);
        holder.title.setText(clothing.name);
        holder.image.setImageResource(clothing.imageResource);

        if(clothing.price!="0"){
            holder.price.setText(clothing.price);
        }


        return convertView;
    }

    static class ViewHolder {

        TextView title;
        TextView price;
        ImageView image;

    }
}
