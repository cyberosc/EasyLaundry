package com.acktos.easylaundry;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.acktos.easylaundry.services.GcmIntentService;
import com.acktos.easylaundry.services.SyncDataService;


/**
 * Created by OSCAR ACKTOS on 06/03/2015.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i(getClass().getSimpleName(),"Entry to broadCastReceiver");
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {

            Log.i(getClass().getSimpleName(),extras.getString("Category"));
            // Explicitly specify that GcmIntentService will handle the intent.

            ComponentName comp = new ComponentName(context.getPackageName(),SyncDataService.class.getName());
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);

        }



    }


}
