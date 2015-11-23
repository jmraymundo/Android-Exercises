
package com.example.runtracker.database;

import com.example.runtracker.object.Run;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class RunDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_Name = "runs.sqlite";

    private static final String LOCATION_COLUMN_ALTITUDE = "altitude";

    private static final String LOCATION_COLUMN_LATITUDE = "latitude";

    private static final String LOCATION_COLUMN_LONGITUDE = "longitude";

    private static final String LOCATION_COLUMN_PROVIDER = "provider";

    private static final String LOCATION_COLUMN_RUN_ID = "run_id";

    private static final String LOCATION_COLUMN_TIMESTAMP = "timestamp";

    private static final String RUN_COLUMN_ID = "id";

    private static final String RUN_COLUMN_START_DATE = "start_date";

    private static final String TABLE_LOCATION = "location";

    private static final String TABLE_RUN = "run";

    private static final int VERSION = 1;

    public RunDatabaseHelper( Context context )
    {
        super( context, DB_Name, null, VERSION );
    }

    public long insertRun( Run run )
    {
        ContentValues cv = new ContentValues();
        cv.put( RUN_COLUMN_START_DATE, run.getStartDate().getTime() );
        return getWritableDatabase().insert( TABLE_RUN, null, cv );
    }

    public long insertLocation( long runId, Location location )
    {
        ContentValues cv = new ContentValues();
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        db.execSQL( "create table " + TABLE_RUN + " (" + RUN_COLUMN_ID + " integer primary key autoincrement, "
                + RUN_COLUMN_START_DATE + " integer)" );
        db.execSQL( "create table " + TABLE_LOCATION + " ( " + LOCATION_COLUMN_TIMESTAMP + " integer, "
                + LOCATION_COLUMN_LATITUDE + " real, " + LOCATION_COLUMN_LONGITUDE + " real, "
                + LOCATION_COLUMN_ALTITUDE + " real, " + LOCATION_COLUMN_PROVIDER + " varchar(100), "
                + LOCATION_COLUMN_RUN_ID + " integer referenced " + TABLE_RUN + "(" + RUN_COLUMN_ID + ")" );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        // TODO Auto-generated method stub
    }
}