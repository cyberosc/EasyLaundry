package com.acktos.easylaundry;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class FinishActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    @Override
    protected void onStart() {
        super.onStart();


        //(new NotificationTask()).execute();
    }


    public class NotificationTask extends AsyncTask<Void, Void, Boolean> {




        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(3000);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(FinishActivity.this)
                                .setSmallIcon(R.drawable.ic_order)
                                .setContentTitle("Your Request was accepted")
                                .setContentText("Your request will be handled by \"Clean Laundry\"!");
// Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(FinishActivity.this, OrderStatusActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(FinishActivity.this);
// Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(OrderStatusActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(2, mBuilder.build());
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }


    }
}
