
package com.example.runtracker.object;

import android.annotation.SuppressLint;
import java.util.Date;

public class Run
{
    private Date mStartDate;

    public Run()
    {
        setStartDate( new Date() );
    }

    public Date getStartDate()
    {
        return mStartDate;
    }

    public void setStartDate( Date startDate )
    {
        mStartDate = startDate;
    }

    public int getDurationSeconds( long endMillis )
    {
        return ( int ) ( endMillis - mStartDate.getTime() ) / 1000;
    }

    @SuppressLint( "DefaultLocale" )
    public static String formatDuration( int durationSeconds )
    {
        int seconds = durationSeconds % 60;
        int minutes = ( ( durationSeconds - seconds ) / 60 ) % 60;
        int hours = ( durationSeconds - ( minutes * 60 ) - seconds ) / 3600;
        return String.format( "%02d:%02d:%02d", hours, minutes, seconds );
    }
}
