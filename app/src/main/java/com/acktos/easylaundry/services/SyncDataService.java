package com.acktos.easylaundry.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.acktos.easylaundry.LaundryContract;
import com.acktos.easylaundry.controllers.CategoriesController;
import com.acktos.easylaundry.controllers.ClothesController;
import com.acktos.easylaundry.models.Category;
import com.acktos.easylaundry.models.Clothing;
import com.acktos.easylaundry.providers.EasyLaundryProvider;

import java.util.ArrayList;


public class SyncDataService extends IntentService {


    private CategoriesController categoriesController;
    private ClothesController clothesController;
    private Uri uri;

    public SyncDataService() {

        super("SyncDataService");
        categoriesController=new CategoriesController(this);
        clothesController=new ClothesController(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            updateCategories();
            updateClothes();
        }
    }

    /**
     * Update Categories trough content provider
     */
    private void updateCategories() {

        ArrayList<Category> categories=categoriesController.getAllCategories();

        if(categories!=null){

            if(categories.size()>0){

                deleteAllCategories(); // delete table info if there is new data

                for(int i=0; i<categories.size();i++){

                    uri=getContentResolver().insert(EasyLaundryProvider.URI_CATEGORIES,categories.get(i).getValues());
                    Log.i("insert row:",uri.toString());
                }
            }
        }

    }

    private int deleteAllCategories(){

        int rowsDeleted;

        rowsDeleted=getContentResolver().delete(EasyLaundryProvider.URI_CATEGORIES,null,null);

        return rowsDeleted;
    }

    /**
     * Get All clothes data from server
     * @return Clothes {@code ArrayList<Clothing>}.
     */
    private void updateClothes(){

        ArrayList<Clothing> clothes=clothesController.getAllClothes();

        if(clothes!=null){

            if(clothes.size()>0){

                deleteAllClothes(); // delete table info if there is new data

                for(int i=0; i<clothes.size();i++){

                    uri=getContentResolver().insert(EasyLaundryProvider.URI_CLOTHES,clothes.get(i).getValues());
                    Log.i("insert clothing row:",uri.toString());
                }
            }
        }

    }

    /**
     * Delete all clothes from local database
     * @return rows deleted {@code int}.
     */
    private int deleteAllClothes(){

        int rowsDeleted;

        rowsDeleted=getContentResolver().delete(EasyLaundryProvider.URI_CLOTHES,null,null);

        return rowsDeleted;
    }


}
