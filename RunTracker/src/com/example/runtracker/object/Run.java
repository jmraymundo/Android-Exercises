
package com.example.runtracker.object;

import java.util.Date;

import android.annotation.SuppressLint;

public class Run
{
    private long mId;

    private Date mStartDate;

    public Run()
    {
        setId( -1 );
        setStartDate( new Date() );
    }

    @SuppressLint( "DefaultLocale" )
    public static String formatDuration( int durationSeconds )
    {
        int seconds = durationSeconds % 60;
        int minutes = ( ( durationSeconds - seconds ) / 60 ) % 60;
        int hours = ( durationSeconds - ( minutes * 60 ) - seconds ) / 3600;
        return String.format( "%02d:%02d:%02d", hours, minutes, seconds );
    }

    public int getDurationSeconds( long endMillis )
    {
        return ( int ) ( endMillis - mStartDate.getTime() ) / 1000;
    }

    public long getId()
    {
        return mId;
    }

    public Date getStartDate()
    {
        return mStartDate;
    }

    public void setId( long id )
    {
        mId = id;
    }

    public void setStartDate( Date startDate )
    {
        mStartDate = startDate;
    }
}
