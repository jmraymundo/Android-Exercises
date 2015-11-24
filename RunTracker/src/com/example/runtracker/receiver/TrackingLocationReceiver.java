
package com.example.runtracker.receiver;

import com.example.runtracker.manager.RunManager;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class TrackingLocationReceiver extends LocationReceiver
{
    private static final String TAG = "TrackingLocationReceiver";

    @Override
    protected void onLocationReceived( Context context, Location loc )
    {
        Log.d( TAG, "location received in receiver: " + loc.toString() );
        RunManager.get( context ).insertLocation( loc );
    }
}
