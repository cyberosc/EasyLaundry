package com.acktos.easylaundry;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Acktos on 26/06/15.
 */
public class ClothesCursorAdapter extends CursorAdapter{

    private LayoutInflater inflater;

    public ClothesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.clhoting_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.clothing_title);
        TextView description = (TextView) view.findViewById(R.id.clothing_subtitle);
        ImageView thumbnail=(ImageView) view.findViewById(R.id.clothing_image);
        TextView price=(TextView) view.findViewById(R.id.clothing_price);

        title.setText(cursor.getString(cursor.getColumnIndex(LaundryContract.ClothesTable.COLUMN_NAME_TITLE)));
        description.setText(cursor.getString(cursor.getColumnIndex(LaundryContract.ClothesTable.COLUMN_NAME_DESCRIPTION)));
        price.setText("$ "+cursor.getString(cursor.getColumnIndex(LaundryContract.ClothesTable.COLUMN_NAME_PRICE)));
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex(LaundryContract.ClothesTable.COLUMN_NAME_THUMBNAIL)))
                .into(thumbnail);
    }
}
