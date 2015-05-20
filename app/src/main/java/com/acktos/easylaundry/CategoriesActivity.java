package com.acktos.easylaundry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;


public class CategoriesActivity extends ActionBarActivity {


    public static final String SHARED_PREFERENCES ="com.acktos.SHARED_PREFERENCES";
    public static final String SHARED_CLOTHING_QTY ="com.acktos.CLOTHING_QTY";

    public static final String PROPERTY_REG_ID = "easylaundry_registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SENDER_ID = "316092606429 ";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "EasyLaundryDebug";


    TextView mDisplay;
    GoogleCloudMessaging gcm;

    Context context;

    String regid;

    //ANDROID UTILS
    SharedPreferences mPrefs;// Handle to SharedPreferences for this APP
    SharedPreferences.Editor mEditor;// Handle to a SharedPreferences editor

    ActionBar actionBar;
    TextView qtyClothes;
    int items=0;

    private ListView categoriesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        categoriesList=(ListView)findViewById(R.id.list_categories);

        actionBar=getSupportActionBar();

        //get shared preferences
        mPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);// Open Shared Preferences
        mEditor = mPrefs.edit();// Get an editor

        ArrayList<Clothing> clothings=new ArrayList<Clothing>();

        clothings.add(new Clothing(R.drawable.circle_suit,"SUITS","0"));
        clothings.add(new Clothing(R.drawable.circle_dress,"DRESSES","0"));
        clothings.add(new Clothing(R.drawable.circle_jacket,"JACKETS","0"));
        clothings.add(new Clothing(R.drawable.circle_towelling,"TOWELLING","0"));
        clothings.add(new Clothing(R.drawable.circle_shirt,"SHIRTS","0"));
        clothings.add(new Clothing(R.drawable.circle_pants,"PANTS","0"));
        clothings.add(new Clothing(R.drawable.circle_knitwear,"KNITWEAR","0"));
        clothings.add(new Clothing(R.drawable.circle_tablecloth,"TABLECLOTH","0"));


        CategoryAdapter categoryAdapter = new CategoryAdapter(this, clothings);
        categoriesList.setAdapter(categoryAdapter);

        //set click handler to categories list
        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Object item=parent.getItemAtPosition(position);
                Clothing card=(Clothing)item;

                //Toast.makeText(CardListActivity.this, getString(R.string.set_payment_success),Toast.LENGTH_SHORT).show();

                Intent i=new Intent(CategoriesActivity.this,SubCategoryActivity.class);
                startActivity(i);

            }
        });

        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_categories, menu);

        final View shoppingcart = menu.findItem(R.id.action_basket).getActionView();
        qtyClothes = (TextView) shoppingcart.findViewById(R.id.qty_clothes);

        qtyClothes.setText(String.valueOf(items));
        qtyClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoriesActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.basket) {
            Intent i=new Intent(this,OrderActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onPostResume();
        checkPlayServices();
        showItemsNumber();
    }

    private void showItemsNumber(){

        items= mPrefs.getInt(SHARED_CLOTHING_QTY, 0);
        invalidateOptionsMenu();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * If result is empty, the app needs to register.
     * @return registration ID, or empty string if there is no existing registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        //SharedPreferences.Editor editor = prefs.edit();
        //editor.putString(PROPERTY_REG_ID, "");
        //editor.commit();

        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }else{
            Log.i(TAG,"Registration id:"+registrationId);
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(CategoriesActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
                Log.i(TAG,"message:"+msg);
            }

        }.execute(null, null, null);

    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


}
