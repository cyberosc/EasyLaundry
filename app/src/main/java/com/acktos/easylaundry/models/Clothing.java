package com.acktos.easylaundry.models;

import android.content.ContentValues;

import com.acktos.easylaundry.LaundryContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by OSCAR ACKTOS on 28/03/2015.
 */
public class Clothing {

    public String id;
    public String categoryId;
    public String name;
    public String description;
    public String thumbnail;
    public String price;

    public static String KEY_ID="id";
    public static String KEY_CATEGORY_ID="category_id";
    public static String KEY_NAME="name";
    public static String KEY_PRICE="price";
    public static String KEY_DESCRIPTION="description";
    public static String KEY_THUMBNAIL="thumbnail";



    public Clothing(String id,String CategoryId,String name,String price,String thumbnail,String description){

        this.id=id;
        this.categoryId=categoryId;
        this.name=name;
        this.price=price;
        this.thumbnail=thumbnail;
        this.description=description;

    }

    public Clothing(JSONObject jsonObject){

        try {

            this.id=jsonObject.getString(KEY_ID);
            this.categoryId=jsonObject.getString(KEY_CATEGORY_ID);
            this.name=jsonObject.getString(KEY_NAME);
            this.price=jsonObject.getString(KEY_PRICE);
            this.description=jsonObject.getString(KEY_DESCRIPTION);
            this.thumbnail=jsonObject.getString(KEY_THUMBNAIL);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ContentValues getValues(){

        ContentValues values=new ContentValues();

        values.put(LaundryContract.ClothesTable.COLUMN_NAME_TITLE,this.name);
        values.put(LaundryContract.ClothesTable.COLUMN_NAME_CATEGORY_ID,this.categoryId);
        values.put(LaundryContract.ClothesTable.COLUMN_NAME_PRICE,this.price);
        values.put(LaundryContract.ClothesTable.COLUMN_NAME_DESCRIPTION,this.description);
        values.put(LaundryContract.ClothesTable.COLUMN_NAME_THUMBNAIL,this.thumbnail);
        return values;
    }

    @Override
    public String toString(){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(KEY_ID,this.id);
            jsonObject.put(KEY_CATEGORY_ID,this.categoryId);
            jsonObject.put(KEY_NAME,this.name);
            jsonObject.put(KEY_PRICE,this.price);
            jsonObject.put(KEY_DESCRIPTION,this.description);
            jsonObject.put(KEY_THUMBNAIL,this.thumbnail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
