
package com.example.runtracker.manager;

import com.example.runtracker.database.RunDatabaseHelper;
import com.example.runtracker.database.RunDatabaseHelper.LocationCursor;
import com.example.runtracker.database.RunDatabaseHelper.RunCursor;
import com.example.runtracker.object.Run;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class RunManager
{
    public static final String ACTION_LOCATION = "com.example.runtracker.manager.ACTION_LOCATION";

    public static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";

    public static final String PREFS_FILE = "runs";

    private static RunManager sRunManager;

    private static final String TAG = "RunManager";

    private static final String TEST_PROVIDER = "TEST_PROVIDER";

    private Context mAppContext;

    private long mCurrentRunId;

    private RunDatabaseHelper mHelper;

    private LocationManager mLocationManager;

    private SharedPreferences mPrefs;

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
        Log.d( TAG, "inside get(Context context) method" );
        if( sRunManager == null )
        {
            Log.d( TAG, "sRunManager is null! Creating new instance" );
            sRunManager = new RunManager( context.getApplicationContext() );
        }
        Log.d( TAG, "Is sRunManager null? " + ( sRunManager == null ) );
        return sRunManager;
    }

    public Location getLastLocationForRun( long runId )
    {
        Location location = null;
        LocationCursor cursor = mHelper.queryLastLocationForRun( runId );
        cursor.moveToFirst();
        if( !cursor.isAfterLast() )
        {
            location = cursor.getLocation();
        }
        cursor.close();
        return location;
    }

    public Run getRun( long id )
    {
        Run run = null;
        RunCursor cursor = mHelper.queryRun( id );
        cursor.moveToFirst();
        if( !cursor.isAfterLast() )
        {
            run = cursor.getRun();
        }
        cursor.close();
        return run;
    }

    public void insertLocation( Location loc )
    {
        Log.d( TAG, "location received: " + loc.toString() );
        if( mCurrentRunId != -1 )
        {
            Log.d( TAG, "inserting location to table with id = " + mCurrentRunId );
            mHelper.insertLocation( mCurrentRunId, loc );
        }
        else
        {
            Log.e( TAG, "Location received without tracking run; ignoring." );
        }
    }

    public boolean isLocatorAvailable()
    {
        return mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )
                || ( mLocationManager.getProvider( TEST_PROVIDER ) != null
                        && mLocationManager.isProviderEnabled( TEST_PROVIDER ) );
    }

    public boolean isTrackingRun()
    {
        return getLocationPendingIntent( false ) != null;
    }

    public boolean isTrackingRun( Run run )
    {
        return run != null && run.getId() == mCurrentRunId;
    }

    public LocationCursor queryLocationsForRun( long id )
    {
        return mHelper.queryLocationsForRun( id );
    }

    public RunCursor queryRuns()
    {
        return mHelper.queryRuns();
    }

    public void startLocationUpdates()
    {
        String provider = LocationManager.GPS_PROVIDER;

        if( mLocationManager.getProvider( TEST_PROVIDER ) != null
                && mLocationManager.isProviderEnabled( TEST_PROVIDER ) )
        {
            provider = TEST_PROVIDER;
        }
        Log.d( TAG, "Using provider: " + provider );

        Location lastKnownLocation = mLocationManager.getLastKnownLocation( provider );
        if( lastKnownLocation != null )
        {
            lastKnownLocation.setTime( System.currentTimeMillis() );
            broadcastLocation( lastKnownLocation );
        }

        PendingIntent pi = getLocationPendingIntent( true );
        mLocationManager.requestLocationUpdates( provider, 0, 0, pi );
    }

    public Run startNewRun()
    {
        Run run = insertRun();
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong( PREF_CURRENT_RUN_ID, mCurrentRunId ).commit();
        startLocationUpdates();
        return run;
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

    public void stopRun()
    {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove( PREF_CURRENT_RUN_ID ).commit();
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

    private Run insertRun()
    {
        Run run = new Run();
        run.setId( mHelper.insertRun( run ) );
        return run;
    }
}
