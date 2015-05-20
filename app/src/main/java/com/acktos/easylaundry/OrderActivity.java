package com.acktos.easylaundry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class OrderActivity extends ActionBarActivity {


    public static final String SHARED_PREFERENCES ="com.acktos.SHARED_PREFERENCES";
    public static final String SHARED_CLOTHING_QTY ="com.acktos.CLOTHING_QTY";
    public static final String SHARED_CLOTHING_PRICE ="com.acktos.CLOTHING_PRICE";

    //ANDROID UTILS
    SharedPreferences mPrefs;// Handle to SharedPreferences for this APP
    SharedPreferences.Editor mEditor;// Handle to a SharedPreferences editor

    int currentQty;
    int currentPrice;

    TextView currentQtyView;
    TextView currentPriceView;
    private static  EditText collectedDate;
    private static EditText deliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        currentPriceView=(TextView) findViewById(R.id.current_price);
        currentQtyView=(TextView) findViewById(R.id.current_qty);

        collectedDate=(EditText) findViewById(R.id.order_collection);
        deliveryDate=(EditText) findViewById(R.id.order_delivery);



        //get shared preferences
        mPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);// Open Shared Preferences
        mEditor = mPrefs.edit();// Get an editor

        currentQty= mPrefs.getInt(SHARED_CLOTHING_QTY, 0);
        currentPrice= mPrefs.getInt(SHARED_CLOTHING_PRICE, 0);

        currentQtyView.setText("Items:"+Integer.toString(currentQty));
        //currentPriceView.setText("$ "+Integer.toString(currentPrice));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.finish) {
            Intent i=new Intent(this,FinishActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showCollectedDate(View v){
        DialogFragment collected=new CollectionDatePickerFragment();
        collected.show(getFragmentManager(),"CollectionDatePickerFragment");
    }

    public void showDeliveryDate(View v){
        DialogFragment delivery=new DeliveryDatePickerFragment();
        delivery.show(getFragmentManager(),"DeliveryDatePickerFragment");
    }

    public static class CollectionDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        final Calendar c= Calendar.getInstance();

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
            collectedDate.setText(sdf.format(c.getTime()));
        }

    }

    public static class DeliveryDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        final Calendar c= Calendar.getInstance();

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
            deliveryDate.setText(sdf.format(c.getTime()));
        }

    }
}
