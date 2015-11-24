
package com.example.runtracker.loader;

import com.example.runtracker.manager.RunManager;

import android.content.Context;
import android.database.Cursor;

public class RunListCursorLoader extends SQLLiteCursorLoader
{
    public RunListCursorLoader( Context context )
    {
        super( context );
    }

    @Override
    protected Cursor loadCursor()
    {
        return RunManager.get( getContext() ).queryRuns();
    }

}
