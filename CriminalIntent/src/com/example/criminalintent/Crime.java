package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.util.Log;

public class Crime
{
    private boolean isSolved;

    private Date myDate;

    private UUID myId;

    private String myTitle;

    public Crime()
    {
        myId = UUID.randomUUID();
        myDate = new Date();
        Log.d( CrimeActivity.CRIMINAL_INTENT, "inside Crime constructor" );
    }

    public Date getDate()
    {
        return myDate;
    }

    public UUID getId()
    {
        return myId;
    }

    public String getTitle()
    {
        return myTitle;
    }

    public boolean isSolved()
    {
        return isSolved;
    }

    public void setDate( Date date )
    {
        myDate = date;
    }

    public void setSolved( boolean isSolved )
    {
        this.isSolved = isSolved;
    }

    public void setTitle( String title )
    {
        myTitle = title;
    }
}
