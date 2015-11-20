
package com.example.photogallery.services;

import java.util.ArrayList;

import com.example.photogallery.object.GalleryItem;
import com.example.photogallery.utils.FlickrFetcher;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class PollService extends IntentService
{
    private static final String TAG = "PollService";

    private static final int POLL_INTERVAL = 1000 * 15;

    public PollService()
    {
        super( TAG );
    }

    @Override
    protected void onHandleIntent( Intent intent )
    {
        ConnectivityManager cm = ( ConnectivityManager ) getSystemService( Context.CONNECTIVITY_SERVICE );
        @SuppressWarnings( "deprecation" )
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;

        if( !isNetworkAvailable )
        {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
        String query = prefs.getString( FlickrFetcher.PREF_SEARCH_QUERY, null );
        String lastResultId = prefs.getString( FlickrFetcher.PREF_LAST_RESULT_ID, null );

        ArrayList< GalleryItem > items;
        if( query != null )
        {
            items = new FlickrFetcher().search( query );
        }
        else
        {
            items = new FlickrFetcher().fetchItems();
        }
        if( items.size() == 0 )
        {
            return;
        }
        String resultId = items.get( 0 ).getId();
        if( !resultId.equals( lastResultId ) )
        {
            Log.i( TAG, "Got a new result: " + resultId );
        }
        else
        {
            Log.i( TAG, "Got an old result: " + resultId );
        }

        prefs.edit().putString( FlickrFetcher.PREF_LAST_RESULT_ID, resultId ).commit();
    }

    public static void setAlarmService( Context context, boolean isOn )
    {
        Intent i = new Intent( context, PollService.class );
        PendingIntent pi = PendingIntent.getService( context, 0, i, 0 );
        AlarmManager alarmManager = ( AlarmManager ) context.getSystemService( Context.ALARM_SERVICE );
        if( isOn )
        {
            alarmManager.setRepeating( AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi );
        }
        else
        {
            alarmManager.cancel( pi );
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn( Context context )
    {
        Intent i = new Intent( context, PollService.class );
        PendingIntent pi = PendingIntent.getService( context, 0, i, PendingIntent.FLAG_NO_CREATE );
        return pi != null;
    }
}
