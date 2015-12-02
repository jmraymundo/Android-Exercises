
package com.example.runtracker.fragment;

import java.text.DecimalFormat;

import com.example.runtracker.R;
import com.example.runtracker.activity.RunMapActivity;
import com.example.runtracker.database.RunDatabaseHelper.LocationCursor;
import com.example.runtracker.loader.LocationListCursorLoader;
import com.example.runtracker.loader.LocationLoader;
import com.example.runtracker.loader.RunLoader;
import com.example.runtracker.manager.RunManager;
import com.example.runtracker.object.Run;
import com.example.runtracker.receiver.LocationReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunFragment extends Fragment
{
    public static final String ARGS_RUN_ID = "run_id";

    public static final int SHOW_MAP = 2;

    private static final int LOAD_LOCATION = 1;

    private static final int LOAD_RUN = 0;

    private static final int LOAD_RUN_LOCATIONS = 3;

    private static final String TAG = "RunTrackerV2.0";

    public boolean mIsLocationReceiverRegistered;

    public LocationCursor mLocationCursor;

    public float mTotalDistance = 0;

    private TextView mAltitudeTextView;

    private TextView mDistanceTextView;

    private TextView mDurationTextView;

    private Location mLastLocation;

    private TextView mLatitudeTextView;

    private LocationListLoaderOnCallbacks mLocationListLoaderOnCallbacks;

    private BroadcastReceiver mLocationReceiver = new LocationReceiver()
    {
        @Override
        protected void onLocationReceived( Context context, Location loc )
        {
            if( !mRunManager.isTrackingRun( mRun ) )
            {
                return;
            }
            mLastLocation = loc;
            if( isVisible() )
            {
                restartLoader();
                updateUI();
            }
        }

        @Override
        protected void onProviderEnabledChanged( boolean enabled )
        {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText( getActivity(), toastText, Toast.LENGTH_LONG ).show();
            updateUI();
            if( !enabled )
            {
                getActivity().finish();
            }
        }
    };

    private TextView mLongitudeTextView;

    private Button mMapButton;

    private Run mRun;

    private RunManager mRunManager;

    private TextView mStartedTextView;

    private Button mStopButton;

    public static RunFragment newInstance( long runId )
    {
        Log.i( TAG, "RunFragment - Run " + runId + " selected!" );
        Bundle args = new Bundle();
        args.putLong( ARGS_RUN_ID, runId );
        RunFragment rf = new RunFragment();
        rf.setArguments( args );
        return rf;
    }

    private void restartLoader()
    {
        LoaderManager lm = getLoaderManager();
        Bundle args = new Bundle();
        args.putLong( ARGS_RUN_ID, mRun.getId() );
        lm.restartLoader( LOAD_RUN_LOCATIONS, args, mLocationListLoaderOnCallbacks );
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch( requestCode )
        {
            case SHOW_MAP:
                updateUI();
                break;
            default:
                super.onActivityResult( requestCode, resultCode, data );
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
        mRunManager = RunManager.get( getActivity() );
        Bundle args = getArguments();
        Log.i( TAG, "RunFragment - Running RunFragment!" );
        boolean newRun = true;
        mLocationListLoaderOnCallbacks = new LocationListLoaderOnCallbacks();
        if( args != null )
        {
            Log.i( TAG, "RunFragment - Argument found!" );
            long runId = args.getLong( ARGS_RUN_ID, 0 );
            if( runId != 0 )
            {
                Log.i( TAG, "RunFragment - Valid runId argument. Setting id to " + runId );
                LoaderManager lm = getLoaderManager();
                lm.initLoader( LOAD_RUN, args, new RunLoaderOnCallbacks() );
                lm.initLoader( LOAD_LOCATION, args, new LocationLoaderOnCallbacks() );
                lm.initLoader( LOAD_RUN_LOCATIONS, args, mLocationListLoaderOnCallbacks );
                newRun = false;
            }
        }
        if( newRun )
        {
            mRun = mRunManager.startNewRun();
            Log.i( TAG, "RunFragment - No valid argument found. Starting new run with id " + mRun.getId() );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        Log.i( TAG, "RunFragment - RunFragment view created!" );
        View view = inflater.inflate( R.layout.fragment_run, container, false );

        mStartedTextView = ( TextView ) view.findViewById( R.id.run_startedTextView );
        mLatitudeTextView = ( TextView ) view.findViewById( R.id.run_latitudeTextView );
        mLongitudeTextView = ( TextView ) view.findViewById( R.id.run_longitudeTextView );
        mAltitudeTextView = ( TextView ) view.findViewById( R.id.run_altitudeTextView );
        mDistanceTextView = ( TextView ) view.findViewById( R.id.run_distanceTextView );
        mDurationTextView = ( TextView ) view.findViewById( R.id.run_durationTextView );

        mStopButton = ( Button ) view.findViewById( R.id.run_stopButton );
        mStopButton.setOnClickListener( new StopOnClickListener() );
        mMapButton = ( Button ) view.findViewById( R.id.run_mapButton );
        mMapButton.setOnClickListener( new MapOnClickListener() );

        updateUI();
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if( isRunOngoing() )
        {
            getActivity().registerReceiver( mLocationReceiver, new IntentFilter( RunManager.ACTION_LOCATION ) );
            mIsLocationReceiverRegistered = true;
        }
    }

    public void updateDistance()
    {
        if( mLocationCursor == null )
        {
            return;
        }
        mTotalDistance = 0;
        mLocationCursor.moveToFirst();
        while( !mLocationCursor.isAfterLast() )
        {
            Location previous = mLocationCursor.getLocation();
            mLocationCursor.moveToNext();
            Location current = mLocationCursor.getLocation();
            if( current == null )
            {
                return;
            }
            mTotalDistance = mTotalDistance + previous.distanceTo( current );
        }
    }

    private boolean isRunOngoing()
    {
        boolean isTrackingStarted = mRunManager.isTrackingRun();
        boolean isTrackingThisRun = mRunManager.isTrackingRun( mRun );
        boolean isLocatorAvailable = mRunManager.isLocatorAvailable();
        return isTrackingStarted && isTrackingThisRun && isLocatorAvailable;
    }

    private String roundUp( float totalDistance )
    {
        DecimalFormat format = new DecimalFormat( "#.##" );
        return format.format( totalDistance );
    }

    private void updateResult()
    {
        Intent i = new Intent();
        if( isRunOngoing() )
        {
            i.putExtra( ARGS_RUN_ID, mRun.getId() );
            Log.i( TAG, "RunFragment - Run " + mRun.getId() + " still ongoing." );
        }
        else
        {
            i.putExtra( ARGS_RUN_ID, 0 );
            Log.i( TAG, "RunFragment - Run " + mRun.getId() + " already stopped." );
        }
        getActivity().setResult( Activity.RESULT_OK, i );
    }

    private void updateUI()
    {
        if( mRun != null )
        {
            mStartedTextView.setText( mRun.getStartDate().toString() );
            updateResult();
        }

        if( mLocationCursor != null )
        {
            updateDistance();
        }

        int durationSeconds = 0;
        if( mRun != null && mLastLocation != null )
        {
            durationSeconds = mRun.getDurationSeconds( mLastLocation.getTime() );
            mLatitudeTextView.setText( Double.toString( mLastLocation.getLatitude() ) );
            mLongitudeTextView.setText( Double.toString( mLastLocation.getLongitude() ) );
            mAltitudeTextView.setText( Double.toString( mLastLocation.getAltitude() ) );
            mDistanceTextView.setText( roundUp( mTotalDistance ) + "m" );
            mMapButton.setEnabled( true );
        }
        else
        {
            mMapButton.setEnabled( false );
        }
        mDurationTextView.setText( Run.formatDuration( durationSeconds ) );
        mStopButton.setEnabled( isRunOngoing() );
    }

    private class LocationListLoaderOnCallbacks implements LoaderCallbacks< Cursor >
    {
        @Override
        public Loader< Cursor > onCreateLoader( int id, Bundle args )
        {
            return new LocationListCursorLoader( getActivity(), args.getLong( ARGS_RUN_ID ) );
        }

        @Override
        public void onLoaderReset( Loader< Cursor > cursor )
        {
            mLocationCursor.close();
            mLocationCursor = null;
        }

        @Override
        public void onLoadFinished( Loader< Cursor > loader, Cursor cursor )
        {
            mLocationCursor = ( LocationCursor ) cursor;
            updateUI();
        }
    }

    private class LocationLoaderOnCallbacks implements LoaderCallbacks< Location >
    {
        @Override
        public Loader< Location > onCreateLoader( int id, Bundle args )
        {
            return new LocationLoader( getActivity(), args.getLong( ARGS_RUN_ID ) );
        }

        @Override
        public void onLoaderReset( Loader< Location > loader )
        {

        }

        @Override
        public void onLoadFinished( Loader< Location > loader, Location location )
        {
            mLastLocation = location;
            updateUI();
        }
    }

    private class MapOnClickListener implements OnClickListener
    {
        @Override
        public void onClick( View v )
        {
            Intent i = new Intent( getActivity(), RunMapActivity.class );
            i.putExtra( RunMapActivity.EXTRA_RUN_ID, mRun.getId() );
            startActivityForResult( i, SHOW_MAP );
        }
    }

    private class RunLoaderOnCallbacks implements LoaderCallbacks< Run >
    {
        @Override
        public Loader< Run > onCreateLoader( int id, Bundle args )
        {
            Log.i( TAG, "RunFragment - Run loader created!" );
            return new RunLoader( getActivity(), args.getLong( ARGS_RUN_ID ) );
        }

        @Override
        public void onLoaderReset( Loader< Run > loader )
        {
            Log.i( TAG, "RunFragment - Run loader reset!" );
        }

        @Override
        public void onLoadFinished( Loader< Run > loader, Run run )
        {
            Log.i( TAG, "RunFragment - Loader finished loading. Setting run id to " + run.getId() );
            mRun = run;
            updateUI();
        }
    }

    private class StopOnClickListener implements OnClickListener
    {
        @Override
        public void onClick( View v )
        {
            mRunManager.stopRun();
            getActivity().unregisterReceiver( mLocationReceiver );
            mIsLocationReceiverRegistered = false;
            updateUI();
        }
    }
}
