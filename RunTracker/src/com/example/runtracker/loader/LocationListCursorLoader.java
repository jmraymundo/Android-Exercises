
package com.example.runtracker.loader;

import com.example.runtracker.manager.RunManager;

import android.content.Context;
import android.database.Cursor;

public class LocationListCursorLoader extends SQLLiteCursorLoader
{
    private long mRunId;

    public LocationListCursorLoader( Context c, long runId )
    {
        super( c );
        mRunId = runId;
    }

    @Override
    protected Cursor loadCursor()
    {
        return RunManager.get( getContext() ).queryLocationsForRun( mRunId );
    }
}
