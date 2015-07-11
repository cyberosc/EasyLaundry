package com.acktos.easylaundry;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.acktos.easylaundry.controllers.OrdersController;
import com.acktos.easylaundry.interfaces.OnDateChangeListener;
import com.acktos.easylaundry.interfaces.OnTimeChangeListener;
import com.acktos.easylaundry.models.Order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class OrderActivity extends AppCompatActivity implements View.OnClickListener,OnDateChangeListener, OnTimeChangeListener {


    public static final String SHARED_PREFERENCES ="com.acktos.SHARED_PREFERENCES";
    public static final String SHARED_CLOTHING_QTY ="com.acktos.CLOTHING_QTY";
    public static final String SHARED_CLOTHING_PRICE ="com.acktos.CLOTHING_PRICE";

    //ANDROID UTILS
    SharedPreferences mPrefs;// Handle to SharedPreferences for this APP
    SharedPreferences.Editor mEditor;// Handle to a SharedPreferences editor

    int currentQty;
    int currentPrice;

    private TextView currentQtyView;

    private static EditText addressView;
    private static EditText collectedDate;
    private static EditText collectedTime;
    private static EditText deliveryDate;
    private static EditText deliveryTime;

    private static OnTimeChangeListener timeChangelistener;
    private static OnDateChangeListener dateChangeListener;

    private TextView currentDateView;
    private TextView currentTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_order);


        currentQtyView=(TextView) findViewById(R.id.current_qty);

        addressView=(EditText) findViewById(R.id.order_address);
        collectedDate=(EditText) findViewById(R.id.date_collection);
        collectedTime=(EditText) findViewById(R.id.time_collection);
        deliveryTime=(EditText) findViewById(R.id.time_delivery);
        deliveryDate=(EditText) findViewById(R.id.date_delivery);


        //get shared preferences
        mPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);// Open Shared Preferences
        mEditor = mPrefs.edit();// Get an editor

        currentQty= mPrefs.getInt(SHARED_CLOTHING_QTY, 0);
        currentPrice= mPrefs.getInt(SHARED_CLOTHING_PRICE, 0);

        currentQtyView.setText("Items: "+Integer.toString(currentQty));

        collectedDate.setOnClickListener(this);
        collectedTime.setOnClickListener(this);
        deliveryDate.setOnClickListener(this);
        deliveryTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.date_collection:
                showDatePicker(v);
                break;

            case R.id.time_collection:
                showTimePicker(v);
                break;

            case R.id.date_delivery:
                showDatePicker(v);
                break;

            case R.id.time_delivery:
                showTimePicker(v);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.finish) {

            Order order=new Order();
            order.address=addressView.getText().toString();
            order.deliveryDatetime= deliveryDate.getText().toString()+deliveryTime.getText().toString();
            order.collectionDatetime= collectedDate.getText().toString()+collectedTime.getText().toString();

            (new SaveOrderTask()).execute(order);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePicker(View v){

        currentDateView=(TextView) v;
        DialogFragment datePicker=new DatePickerFragment();
        datePicker.show(getFragmentManager(),"DatePickerDialog");
    }

    public void showTimePicker(View v){

        currentTimeView=(TextView)v;
        DialogFragment timePicker=new TimePickerFragment();
        timePicker.show(getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onDateChange(String date) {
        currentDateView.setText(date);
    }

    @Override
    public void onTimeChange(String time) {
        currentTimeView.setText(time);
    }



    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        final Calendar c= Calendar.getInstance();

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if(activity instanceof OnDateChangeListener){
                dateChangeListener=(OnDateChangeListener) activity;
            }else{
                throw new ClassCastException(activity.toString()+" must implement OnDateChangeListener");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState){

            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),this,year,month,day);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,monthOfYear);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            dateChangeListener.onDateChange(sdf.format(c.getTime()));
        }

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        final Calendar c=Calendar.getInstance();

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if(activity instanceof OnTimeChangeListener){
                timeChangelistener=(OnTimeChangeListener) activity;
            }else{
                throw new ClassCastException(activity.toString()+" must implement OnDataChangeListener");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState){

            int hour=c.get(Calendar.HOUR_OF_DAY);
            int minute=c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,false);
        }


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a",Locale.getDefault());
            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE,minute);

            timeChangelistener.onTimeChange(sdf.format(c.getTime()));
        }

    }


    private class SaveOrderTask extends AsyncTask<Order,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Boolean doInBackground(Order... params) {

            Boolean result;
            OrdersController ordersController=new OrdersController();
            result=ordersController.saveOrder(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            setProgressBarIndeterminateVisibility(false);
            Intent i=new Intent(OrderActivity.this,FinishActivity.class);
            startActivity(i);
            finish();

        }
    }

}
