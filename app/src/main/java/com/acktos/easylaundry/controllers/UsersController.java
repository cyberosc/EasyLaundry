package com.acktos.easylaundry.controllers;

import android.util.Log;

import com.acktos.easylaundry.android.HttpRequest;
import com.acktos.easylaundry.models.Order;

/**
 * Created by Acktos on 7/8/15.
 */
public class UsersController {

    public final static String KEY_REGISTER_ID="register_id";
    public final static String URL_SAVE_REGISTER_ID="http://www.blue.acktos.com.co/add_registerid/";

    public boolean saveRegisterId(String registerId) {


        boolean success = true;

        HttpRequest httpPost = new HttpRequest(URL_SAVE_REGISTER_ID);

        httpPost.setParam(KEY_REGISTER_ID, registerId);

        String responseData = httpPost.postRequest();

        Log.i("response register id", responseData);


        return success;

    }
}
