package com.acktos.easylaundry.controllers;

import android.content.Context;
import android.util.Log;

import com.acktos.easylaundry.android.HttpRequest;
import com.acktos.easylaundry.models.Category;
import com.acktos.easylaundry.models.Clothing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Acktos on 26/06/15.
 */
public class ClothesController {

    private Context context;
    private String URL_GET_ALL_CLOTHES="http://www.asdrubalgutty.com/dermas/getAllClothes";

    public ClothesController(Context context){

        this.context=context;
    }

    public ArrayList<Clothing> getAllClothes(){

        ArrayList<Clothing> clothes=null;


        HttpRequest httpRequest=new HttpRequest(URL_GET_ALL_CLOTHES);
        String responseData=httpRequest.postRequest();

        if(responseData!=null){


            Log.i("debug getAllClothes:", responseData);
            try {

                JSONArray jsonArray=new JSONArray(responseData);
                clothes=new ArrayList<>();

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject itemObject=jsonArray.getJSONObject(i);
                    clothes.add(new Clothing(itemObject));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return clothes;
    }
}
