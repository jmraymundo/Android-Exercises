
package com.example.runtracker.loader;

import com.example.runtracker.manager.RunManager;
import com.example.runtracker.object.Run;

import android.content.Context;

public class RunLoader extends DataLoader< Run >
{
    private long mRunId;

    public RunLoader( Context context, long runId )
    {
        super( context );
        mRunId = runId;
    }

    @Override
    public Run loadInBackground()
    {
        return RunManager.get( getContext() ).getRun( mRunId );
    }
}
