
package com.example.runtracker.loader;

import com.example.runtracker.manager.RunManager;
import com.example.runtracker.object.Run;

import android.content.Context;
import android.location.Location;

public class LocationLoader extends DataLoader< Location >
{
    private long mRunId;

    public LocationLoader( Context context, long runId )
    {
        super( context );
        mRunId = runId;
    }

    @Override
    public Location loadInBackground()
    {
        return RunManager.get( getContext() ).getLastLocationForRun( mRunId );
    }
}
