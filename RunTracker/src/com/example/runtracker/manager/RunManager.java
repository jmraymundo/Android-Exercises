
package com.example.runtracker.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class RunManager
{
    private static final String ACTION_LOCATION = "com.example.runtracker.manager.ACTION_LOCATION";

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
        PendingIntent pi = getLocationPendingIntent( true );
        mLocationManager.requestLocationUpdates( provider, 0, 0, pi );
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
