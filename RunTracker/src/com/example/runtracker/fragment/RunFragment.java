
package com.example.runtracker.fragment;

import com.example.runtracker.R;
import com.example.runtracker.activity.RunMapActivity;
import com.example.runtracker.loader.LocationLoader;
import com.example.runtracker.loader.RunLoader;
import com.example.runtracker.manager.RunManager;
import com.example.runtracker.object.Run;
import com.example.runtracker.receiver.LocationReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private static final String ARGS_RUN_ID = "run_id";

    private static final int LOAD_LOCATION = 1;

    private static final int LOAD_RUN = 0;

    private static final String TAG = "RunFragment";

    private TextView mAltitudeTextView;

    private TextView mDurationTextView;

    private boolean mIsStarted;

    private Location mLastLocation;

    private TextView mLatitudeTextView;

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
                updateUI();
            }
        }

        @Override
        protected void onProviderEnabledChanged( boolean enabled )
        {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText( getActivity(), toastText, Toast.LENGTH_LONG ).show();
        }
    };

    private TextView mLongitudeTextView;

    private Button mMapButton;

    private Run mRun;

    private RunManager mRunManager;

    private Button mStartButton;

    private TextView mStartedTextView;

    private Button mStopButton;

    public static RunFragment newInstance( long runId )
    {
        Log.i( TAG, "Run " + runId + " selected!" );
        Bundle args = new Bundle();
        args.putLong( ARGS_RUN_ID, runId );
        RunFragment rf = new RunFragment();
        rf.setArguments( args );
        return rf;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
        mRunManager = RunManager.get( getActivity() );
        Bundle args = getArguments();
        if( args != null )
        {
            long runId = args.getLong( ARGS_RUN_ID, -1 );
            if( runId != -1 )
            {
                LoaderManager lm = getLoaderManager();
                lm.initLoader( LOAD_RUN, args, new RunLoaderOnCallbacks() );
                lm.initLoader( LOAD_LOCATION, args, new LocationLoaderOnCallbacks() );
            }
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        Log.i( TAG, "RunFragment view created!" );
        View view = inflater.inflate( R.layout.fragment_run, container, false );

        mStartedTextView = ( TextView ) view.findViewById( R.id.run_startedTextView );
        mLatitudeTextView = ( TextView ) view.findViewById( R.id.run_latitudeTextView );
        mLongitudeTextView = ( TextView ) view.findViewById( R.id.run_longitudeTextView );
        mAltitudeTextView = ( TextView ) view.findViewById( R.id.run_altitudeTextView );
        mDurationTextView = ( TextView ) view.findViewById( R.id.run_durationTextView );

        mStartButton = ( Button ) view.findViewById( R.id.run_startButton );
        mStartButton.setOnClickListener( new StartOnClickListener() );
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
        getActivity().registerReceiver( mLocationReceiver, new IntentFilter( RunManager.ACTION_LOCATION ) );
    }

    @Override
    public void onStop()
    {
        getActivity().unregisterReceiver( mLocationReceiver );
        super.onStop();
    }

    private void updateUI()
    {
        mIsStarted = mRunManager.isTrackingRun();
        boolean trackingThisRun = mRunManager.isTrackingRun( mRun );

        if( mRun != null )
        {
            mStartedTextView.setText( mRun.getStartDate().toString() );
        }

        int durationSeconds = 0;
        if( mRun != null && mLastLocation != null )
        {
            durationSeconds = mRun.getDurationSeconds( mLastLocation.getTime() );
            mLatitudeTextView.setText( Double.toString( mLastLocation.getLatitude() ) );
            mLongitudeTextView.setText( Double.toString( mLastLocation.getLongitude() ) );
            mAltitudeTextView.setText( Double.toString( mLastLocation.getAltitude() ) );
            mMapButton.setEnabled( true );
        }
        else
        {
            mMapButton.setEnabled( false );
        }
        mDurationTextView.setText( Run.formatDuration( durationSeconds ) );
        mStartButton.setEnabled( !mIsStarted );
        mStopButton.setEnabled( mIsStarted && trackingThisRun );
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

    private class RunLoaderOnCallbacks implements LoaderCallbacks< Run >
    {
        @Override
        public Loader< Run > onCreateLoader( int id, Bundle args )
        {
            return new RunLoader( getActivity(), args.getLong( ARGS_RUN_ID ) );
        }

        @Override
        public void onLoaderReset( Loader< Run > loader )
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLoadFinished( Loader< Run > loader, Run run )
        {
            mRun = run;
            updateUI();
        }
    }

    private class StartOnClickListener implements OnClickListener
    {
        @Override
        public void onClick( View v )
        {
            if( mRun == null )
            {
                mRun = mRunManager.startNewRun();
            }
            else
            {
                mRunManager.startTrackingRun( mRun );
            }
            updateUI();
        }
    }

    private class StopOnClickListener implements OnClickListener
    {
        @Override
        public void onClick( View v )
        {
            mRunManager.stopRun();
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
            startActivity( i );
        }
    }
}
