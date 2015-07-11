package com.acktos.easylaundry.controllers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.acktos.easylaundry.android.HttpRequest;
import com.acktos.easylaundry.models.Category;
import com.acktos.easylaundry.models.Clothing;
import com.acktos.easylaundry.providers.EasyLaundryProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Acktos on 11/06/15.
 */
public class CategoriesController {

    private Context context;
    private String URL_GET_ALL_CATEGORIES="http://www.asdrubalgutty.com/dermas/getAllCategories";


    public CategoriesController(Context context){

        this.context=context;
    }

    public ArrayList<Category> getAllCategories(){

        ArrayList<Category> categories=null;


        HttpRequest httpRequest=new HttpRequest(URL_GET_ALL_CATEGORIES);
        String responseData=httpRequest.postRequest();


        if(responseData!=null){


            Log.i("debug response request", responseData);
            try {

                JSONArray jsonArray=new JSONArray(responseData);
                categories=new ArrayList<>();

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject itemObject=jsonArray.getJSONObject(i);
                    categories.add(new Category(itemObject));
                }

            } catch (JSONException e) {
                e.getMessage();
            }
        }

        return categories;
    }

    public Cursor getAllCategoriesFromDB(){

        Cursor categoriesCursor=context.getContentResolver().query(EasyLaundryProvider.URI_CATEGORIES,null,null,null,null);
        Log.i("debug cursor size:",categoriesCursor.getCount()+"");

        return categoriesCursor;
    }

}
