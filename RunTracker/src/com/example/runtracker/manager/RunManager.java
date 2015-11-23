
package com.example.runtracker.manager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class RunManager
{
    public static final String ACTION_LOCATION = "com.example.runtracker.manager.ACTION_LOCATION";

    private static RunManager sRunManager;

    private static final String TAG = "RunManager";

    private Context mAppContext;

    private LocationManager mLocationManager;

    private RunManager( Context context )
    {
        mAppContext = context;
        mLocationManager = ( LocationManager ) mAppContext.getSystemService( Context.LOCATION_SERVICE );
    }

    public static RunManager get( Context context )
    {
        if( sRunManager == null )
        {
            sRunManager = new RunManager( context.getApplicationContext() );
        }
        return sRunManager;
    }

    public boolean isTrackingRun()
    {
        return getLocationPendingIntent( false ) != null;
    }

    public void startLocationUpdates()
    {
        String provider = LocationManager.GPS_PROVIDER;

        Location lastKnownLocation = mLocationManager.getLastKnownLocation( provider );
        if( lastKnownLocation != null )
        {
            lastKnownLocation.setTime( System.currentTimeMillis() );
            broadcastLocation( lastKnownLocation );
        }
        PendingIntent pi = getLocationPendingIntent( true );
        mLocationManager.requestLocationUpdates( provider, 0, 0, pi );
    }

    private void broadcastLocation( Location location )
    {
        Intent broadcast = new Intent( ACTION_LOCATION );
        broadcast.putExtra( LocationManager.KEY_LOCATION_CHANGED, location );
        mAppContext.sendBroadcast( broadcast );
    }

    public void stopLocationUpdates()
    {
        PendingIntent pi = getLocationPendingIntent( false );
        if( pi != null )
        {
            mLocationManager.removeUpdates( pi );
            pi.cancel();
        }
    }

    private PendingIntent getLocationPendingIntent( boolean shouldCreate )
    {
        Intent broadcast = new Intent( ACTION_LOCATION );
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast( mAppContext, 0, broadcast, flags );
    }
}
