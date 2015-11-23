
package com.example.runtracker.fragment;

import com.example.runtracker.R;
import com.example.runtracker.manager.RunManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RunFragment extends Fragment
{
    private TextView mAltitudeTextView;

    private TextView mDurationTextView;

    private TextView mLatitudeTextView;

    private TextView mLongitudeTextView;

    private Button mStartButton;

    private TextView mStartedTextView;

    private Button mStopButton;

    private RunManager mRunManager;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
        mRunManager = RunManager.get( getActivity() );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_run, container, false );
        mStartedTextView = ( TextView ) v.findViewById( R.id.run_startedTextView );
        mLatitudeTextView = ( TextView ) v.findViewById( R.id.run_latitudeTextView );
        mLongitudeTextView = ( TextView ) v.findViewById( R.id.run_longitudeTextView );
        mAltitudeTextView = ( TextView ) v.findViewById( R.id.run_altitudeTextView );
        mDurationTextView = ( TextView ) v.findViewById( R.id.run_durationTextView );

        mStartButton = ( Button ) v.findViewById( R.id.run_startButton );
        mStopButton = ( Button ) v.findViewById( R.id.run_stopButton );
        return v;
    }
}
