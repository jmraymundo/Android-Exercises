
package com.example.photogallery.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver
{
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Log.i( TAG, "Received result: " + getResultCode() );
        if( Activity.RESULT_OK != getResultCode() )
        {
            return;
        }

        int requestCode = intent.getIntExtra( PollService.EXTRA_REQUEST_CODE, 0 );
        Notification notif = ( Notification ) intent.getParcelableExtra( PollService.EXTRA_NOTIFICATION );

        NotificationManager notifManager = ( NotificationManager ) context
                .getSystemService( Context.NOTIFICATION_SERVICE );
        notifManager.notify( requestCode, notif );
    }

}
