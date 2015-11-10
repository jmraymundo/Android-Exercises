package com.example.criminalintent;

import java.util.UUID;

import android.util.Log;

public class Crime
{
    private UUID myId;

    private String myTitle;

    public Crime()
    {
        myId = UUID.randomUUID();
        Log.d( CrimeActivity.CRIMINAL_INTENT, "inside Crime constructor" );
    }

    public String getTitle()
    {
        return myTitle;
    }

    public void setTitle( String title )
    {
        myTitle = title;
    }

    public UUID getId()
    {
        return myId;
    }
}
