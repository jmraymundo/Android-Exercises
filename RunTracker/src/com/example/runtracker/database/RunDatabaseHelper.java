
package com.example.runtracker.database;

import java.util.Date;

import com.example.runtracker.object.Run;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class RunDatabaseHelper extends SQLiteOpenHelper
{
    private static final String CONSTANT_ASC = "asc";

    private static final String CONSTANT_DESC = "desc";

    private static final String CONSTANT_LIMIT_1 = "1";

    private static final String DB_NAME = "runs.sqlite";

    private static final String LOCATION_COLUMN_ALTITUDE = "altitude";

    private static final String LOCATION_COLUMN_LATITUDE = "latitude";

    private static final String LOCATION_COLUMN_LONGITUDE = "longitude";

    private static final String LOCATION_COLUMN_PROVIDER = "provider";

    private static final String LOCATION_COLUMN_RUN_ID = "run_id";

    private static final String LOCATION_COLUMN_TIMESTAMP = "timestamp";

    private static final String RUN_COLUMN_ID = "_id";

    private static final String RUN_COLUMN_START_DATE = "start_date";

    private static final String TABLE_LOCATION = "location";

    private static final String TABLE_RUN = "run";

    private static final String TAG = "RunDatabaseHelper";

    private static final int VERSION = 1;

    public RunDatabaseHelper( Context context )
    {
        super( context, DB_NAME, null, VERSION );
    }

    public long insertLocation( long runId, Location location )
    {
        ContentValues cv = new ContentValues();
        cv.put( LOCATION_COLUMN_LATITUDE, location.getLatitude() );
        cv.put( LOCATION_COLUMN_LONGITUDE, location.getLongitude() );
        cv.put( LOCATION_COLUMN_ALTITUDE, location.getAltitude() );
        cv.put( LOCATION_COLUMN_TIMESTAMP, location.getTime() );
        cv.put( LOCATION_COLUMN_PROVIDER, location.getProvider() );
        cv.put( LOCATION_COLUMN_RUN_ID, runId );
        return getWritableDatabase().insert( TABLE_LOCATION, null, cv );
    }

    public long insertRun( Run run )
    {
        ContentValues cv = new ContentValues();
        cv.put( RUN_COLUMN_START_DATE, run.getStartDate().getTime() );
        return getWritableDatabase().insert( TABLE_RUN, null, cv );
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        db.execSQL( "create table " + TABLE_RUN + " (" + RUN_COLUMN_ID + " integer primary key autoincrement, "
                + RUN_COLUMN_START_DATE + " integer)" );
        db.execSQL( "create table " + TABLE_LOCATION + " ( " + LOCATION_COLUMN_TIMESTAMP + " integer, "
                + LOCATION_COLUMN_LATITUDE + " real, " + LOCATION_COLUMN_LONGITUDE + " real, "
                + LOCATION_COLUMN_ALTITUDE + " real, " + LOCATION_COLUMN_PROVIDER + " varchar(100), "
                + LOCATION_COLUMN_RUN_ID + " integer references " + TABLE_RUN + "(" + RUN_COLUMN_ID + ") )" );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        // implement schema changes and data massage here when upgrading
    }

    public LocationCursor queryLastLocationForRun( long runId )
    {
        Cursor wrapped = getReadableDatabase().query( TABLE_LOCATION, null, LOCATION_COLUMN_RUN_ID + " = " + runId,
                null, null, null, LOCATION_COLUMN_TIMESTAMP + " " + CONSTANT_DESC, CONSTANT_LIMIT_1 );
        return new LocationCursor( wrapped );
    }

    public RunCursor queryRun( long id )
    {
        Cursor wrapped = getReadableDatabase().query( TABLE_RUN, null, RUN_COLUMN_ID + " = " + id, null, null, null,
                null, CONSTANT_LIMIT_1 );
        return new RunCursor( wrapped );
    }

    public RunCursor queryRuns()
    {
        Cursor wrapped = getReadableDatabase().query( TABLE_RUN, null, null, null, null, null,
                RUN_COLUMN_START_DATE + " " + CONSTANT_ASC );
        return new RunCursor( wrapped );
    }

    public static class LocationCursor extends CursorWrapper
    {
        public LocationCursor( Cursor c )
        {
            super( c );
        }

        public Location getLocation()
        {
            if( isBeforeFirst() || isAfterLast() )
            {
                return null;
            }
            String provider = getString( getColumnIndex( LOCATION_COLUMN_PROVIDER ) );
            Location loc = new Location( provider );
            loc.setLongitude( getDouble( getColumnIndex( LOCATION_COLUMN_LONGITUDE ) ) );
            loc.setLatitude( getDouble( getColumnIndex( LOCATION_COLUMN_LATITUDE ) ) );
            loc.setAltitude( getDouble( getColumnIndex( LOCATION_COLUMN_ALTITUDE ) ) );
            loc.setTime( getLong( getColumnIndex( LOCATION_COLUMN_TIMESTAMP ) ) );
            return loc;
        }
    }

    public static class RunCursor extends CursorWrapper
    {
        public RunCursor( Cursor c )
        {
            super( c );
        }

        public Run getRun()
        {
            if( isBeforeFirst() || isAfterLast() )
            {
                return null;
            }
            Run run = new Run();
            long runId = getLong( getColumnIndex( RUN_COLUMN_ID ) );
            long startDate = getLong( getColumnIndex( RUN_COLUMN_START_DATE ) );
            run.setId( runId );
            run.setStartDate( new Date( startDate ) );
            return run;
        }
    }

    public LocationCursor queryLocationsForRun( long runId )
    {
        Cursor wrapped = getReadableDatabase().query( TABLE_LOCATION, null, LOCATION_COLUMN_RUN_ID + " = " + runId,
                null, null, null, LOCATION_COLUMN_TIMESTAMP + " " + CONSTANT_ASC );
        return new LocationCursor( wrapped );
    }
}