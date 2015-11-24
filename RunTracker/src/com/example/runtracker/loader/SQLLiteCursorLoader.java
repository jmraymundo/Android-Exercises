
package com.example.runtracker.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public abstract class SQLLiteCursorLoader extends AsyncTaskLoader< Cursor >
{
    private Cursor mCursor;

    public SQLLiteCursorLoader( Context context )
    {
        super( context );
    }

    @Override
    public void deliverResult( Cursor data )
    {
        Cursor oldCursor = mCursor;
        mCursor = data;
        if( isStarted() )
        {
            super.deliverResult( data );
        }
        if( oldCursor != null && oldCursor != data && !oldCursor.isClosed() )
        {
            oldCursor.close();
        }
    }

    @Override
    public Cursor loadInBackground()
    {
        Cursor cursor = loadCursor();
        if( cursor != null )
        {
            cursor.getCount();
        }
        return cursor;
    }

    @Override
    public void onCanceled( Cursor data )
    {
        if( data != null && !data.isClosed() )
        {
            data.close();
        }
    }

    protected abstract Cursor loadCursor();

    @Override
    protected void onReset()
    {
        super.onReset();
        onStopLoading();
        if( mCursor != null && !mCursor.isClosed() )
        {
            mCursor.close();
        }
        mCursor = null;
    }

    @Override
    protected void onStartLoading()
    {
        if( mCursor != null )
        {
            deliverResult( mCursor );
        }
        if( takeContentChanged() || mCursor == null )
        {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading()
    {
        cancelLoad();
    }
}
