package com.example.criminalintent;

import java.util.UUID;

public class Crime
{
    private UUID myId;

    private String myTitle;

    public Crime()
    {
        myId = UUID.randomUUID();
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
