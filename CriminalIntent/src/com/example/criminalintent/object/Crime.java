
package com.example.criminalintent.object;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime
{

    private static final String JSON_DATE = "date";

    private static final String JSON_ID = "id";

    private static final String JSON_PHOTO = "photo";

    private static final String JSON_SOLVED = "solved";

    private static final String JSON_TITLE = "title";

    private static final String JSON_SUSPECT = "suspect";

    private Date mDate;

    private UUID mId;

    private Photo mPhoto;

    private boolean mSolved;

    private String mTitle;

    private String mSuspect;

    public Crime()
    {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime( JSONObject json ) throws JSONException
    {
        mId = UUID.fromString( json.getString( JSON_ID ) );
        mTitle = json.getString( JSON_TITLE );
        mSolved = json.getBoolean( JSON_SOLVED );
        mDate = new Date( json.getLong( JSON_DATE ) );
        if( json.has( JSON_PHOTO ) )
        {
            mPhoto = new Photo( json.getJSONObject( JSON_PHOTO ) );
        }
        if( json.has( JSON_SUSPECT ) )
        {
            mSuspect = json.getString( JSON_SUSPECT );
        }
    }

    public Date getDate()
    {
        return mDate;
    }

    public UUID getId()
    {
        return mId;
    }

    public Photo getPhoto()
    {
        return mPhoto;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public boolean isSolved()
    {
        return mSolved;
    }

    public void setDate( Date date )
    {
        mDate = date;
    }

    public void setPhoto( Photo photo )
    {
        mPhoto = photo;
    }

    public void setSolved( boolean solved )
    {
        mSolved = solved;
    }

    public void setTitle( String title )
    {
        mTitle = title;
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put( JSON_ID, mId.toString() );
        json.put( JSON_TITLE, mTitle );
        json.put( JSON_DATE, mDate.getTime() );
        json.put( JSON_SOLVED, mSolved );
        if( mPhoto != null )
        {
            json.put( JSON_PHOTO, mPhoto.toJSON() );
        }
        if( mSuspect != null )
        {
            json.put( JSON_SUSPECT, mSuspect );
        }
        return json;
    }

    @Override
    public String toString()
    {
        return mTitle;
    }

    public String getSuspect()
    {
        return mSuspect;
    }

    public void setSuspect( String suspect )
    {
        mSuspect = suspect;
    }

}
