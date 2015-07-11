package com.acktos.easylaundry.models;

/**
 * Created by Acktos on 7/8/15.
 */
public class Order {

    public String address;
    public String collectionDatetime;
    public String deliveryDatetime;


    public static String KEY_ADRESS="adress";
    public static String KEY_COLLECTION_DATETIME="collection_datetime";
    public static String KEY_DELIVERY_DATETIME="delivery_datetime";

    public static String URL_SAVE_ORDER="http://www.blue.acktos.com.co/add_registerid/";

}
