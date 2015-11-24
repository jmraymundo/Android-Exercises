
package com.example.runtracker.fragment;

import com.example.runtracker.database.RunDatabaseHelper.LocationCursor;
import com.example.runtracker.loader.LocationListCursorLoader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.PolylineOptions;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RunMapFragment extends SupportMapFragment implements LoaderCallbacks< Cursor >
{
    private static final String ARGS_RUN_ID = "RUN_ID";

    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap mGoogleMap;

    private LocationCursor mLocationCursor;

    public static RunMapFragment newInstance( long id )
    {
        Bundle args = new Bundle();
        args.putLong( ARGS_RUN_ID, id );
        RunMapFragment rf = new RunMapFragment();
        rf.setArguments( args );
        return rf;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        Bundle args = getArguments();
        if( args != null )
        {
            long runId = args.getLong( ARGS_RUN_ID );
            if( runId != -1 )
            {
                LoaderManager lm = getLoaderManager();
                lm.initLoader( LOAD_LOCATIONS, args, this );
            }
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = super.onCreateView( inflater, container, savedInstanceState );
        mGoogleMap = getMap();
        mGoogleMap.setMyLocationEnabled( true );
        return v;
    }

    @Override
    public Loader< Cursor > onCreateLoader( int id, Bundle bundle )
    {
        long runId = bundle.getLong( ARGS_RUN_ID );
        return new LocationListCursorLoader( getActivity(), runId );
    }

    @Override
    public void onLoadFinished( Loader< Cursor > loader, Cursor cursor )
    {
        mLocationCursor = ( LocationCursor ) cursor;
    }

    @Override
    public void onLoaderReset( Loader< Cursor > cursor )
    {
        mLocationCursor.close();
        mLocationCursor = null;
    }

    private void updateUI()
    {
        if( mGoogleMap == null || mLocationCursor == null )
        {
            return;
        }

        PolylineOptions line = new PolylineOptions();
        Builder latLngBuilder = new Builder();

        mLocationCursor.moveToFirst();
        while( !mLocationCursor.isAfterLast() )
        {
            Location loc = mLocationCursor.getLocation();
        }
    }
}
