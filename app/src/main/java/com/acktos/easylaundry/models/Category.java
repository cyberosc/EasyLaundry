package com.acktos.easylaundry.models;

import android.content.ContentValues;

import com.acktos.easylaundry.LaundryContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 19/06/15.
 */
public class Category {

    public String id;
    public String name;
    public String description;
    public String thumbnail;

    public static final String KEY_NAME="name";
    public static final String KEY_DESCRIPTION="description";
    public static final String KEY_IMAGE="thumbnail";
    public static final String KEY_ID="id";


    public Category(String id,String thumbnail,String name,String description){
        this.id=id;
        this.thumbnail=thumbnail;
        this.name=name;
        this.description=description;

    }

    public Category(JSONObject jsonObject){

        try {
            this.id=jsonObject.getString(KEY_ID);
            this.name=jsonObject.getString(KEY_NAME);
            this.description=jsonObject.getString(KEY_DESCRIPTION);
            this.thumbnail=jsonObject.getString(KEY_IMAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ContentValues getValues(){

        ContentValues values=new ContentValues();
        //values.put(LaundryContract.CategoriesTable._ID,this.id);
        values.put(LaundryContract.CategoriesTable.COLUMN_NAME_TITLE,this.name);
        values.put(LaundryContract.CategoriesTable.COLUMN_NAME_DESCRIPTION,this.description);
        values.put(LaundryContract.CategoriesTable.COLUMN_NAME_THUMBNAIL,this.thumbnail);
        return values;
    }
}
