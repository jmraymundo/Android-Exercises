
package com.example.runtracker.manager;

import com.example.runtracker.database.RunDatabaseHelper;
import com.example.runtracker.object.Run;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

public class RunManager
{
    public static final String ACTION_LOCATION = "com.example.runtracker.manager.ACTION_LOCATION";

    public static final String PREFS_FILE = "runs";

    public static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";

    private static RunManager sRunManager;

    private static final String TAG = "RunManager";

    private Context mAppContext;

    private LocationManager mLocationManager;

    private RunDatabaseHelper mHelper;

    private SharedPreferences mPrefs;

    private long mCurrentRunId;

    private RunManager( Context context )
    {
        mAppContext = context;
        mLocationManager = ( LocationManager ) mAppContext.getSystemService( Context.LOCATION_SERVICE );
        mHelper = new RunDatabaseHelper( mAppContext );
        mPrefs = mAppContext.getSharedPreferences( PREFS_FILE, Context.MODE_PRIVATE );
        mCurrentRunId = mPrefs.getLong( PREF_CURRENT_RUN_ID, -1 );
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

    public void stopLocationUpdates()
    {
        PendingIntent pi = getLocationPendingIntent( false );
        if( pi != null )
        {
            mLocationManager.removeUpdates( pi );
            pi.cancel();
        }
    }

    private void broadcastLocation( Location location )
    {
        Intent broadcast = new Intent( ACTION_LOCATION );
        broadcast.putExtra( LocationManager.KEY_LOCATION_CHANGED, location );
        mAppContext.sendBroadcast( broadcast );
    }

    private PendingIntent getLocationPendingIntent( boolean shouldCreate )
    {
        Intent broadcast = new Intent( ACTION_LOCATION );
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast( mAppContext, 0, broadcast, flags );
    }

    public Run startNewRun()
    {
        Run run = insertRun();
        startTrackingRun( run );
        return run;
    }

    public void startTrackingRun( Run run )
    {
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong( PREF_CURRENT_RUN_ID, mCurrentRunId ).commit();
        startLocationUpdates();
    }

    public void stopRun()
    {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove( PREF_CURRENT_RUN_ID ).commit();
    }

    private Run insertRun()
    {
        Run run = new Run();
        run.setId( mHelper.insertRun( run ) );
        return run;
    }
}
