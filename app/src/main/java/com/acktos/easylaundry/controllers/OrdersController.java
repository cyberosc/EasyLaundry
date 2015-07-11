package com.acktos.easylaundry.controllers;

import android.util.Log;

import com.acktos.easylaundry.android.HttpRequest;
import com.acktos.easylaundry.models.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Acktos on 7/8/15.
 */
public class OrdersController {



    public boolean saveOrder(Order order){


        boolean success=true;


        HttpRequest httpPost=new HttpRequest(Order.URL_SAVE_ORDER);

        httpPost.setParam(Order.KEY_ADRESS, order.address);
        httpPost.setParam(Order.KEY_COLLECTION_DATETIME, order.collectionDatetime);
        httpPost.setParam(Order.KEY_DELIVERY_DATETIME, order.deliveryDatetime);


        String responseData=httpPost.postRequest();

        Log.i("response save order", responseData);

        return success;
    }
}
