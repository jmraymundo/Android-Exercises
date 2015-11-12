package com.example.criminalintent.object;

import java.util.Date;
import java.util.UUID;

public class Crime
{
    private boolean mySolved;

    private Date myDateTime;

    private UUID myId;

    private String myTitle;

    public Crime()
    {
        myId = UUID.randomUUID();
        myDateTime = new Date();
    }

    public Date getDateTime()
    {
        return myDateTime;
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
        return mySolved;
    }

    public void setDateTime( Date newDateTime )
    {
        myDateTime = newDateTime;
    }

    public void setSolved( boolean newSolved )
    {
        mySolved = newSolved;
    }

    public void setTitle( String newTitle )
    {
        myTitle = newTitle;
    }

    @Override
    public String toString()
    {
        return getTitle();
    }
}
