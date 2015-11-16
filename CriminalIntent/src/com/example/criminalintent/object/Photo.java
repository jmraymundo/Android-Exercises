
package com.example.criminalintent.object;

import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Photo
{

    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    public Photo( JSONObject json ) throws JSONException
    {
        mFilename = json.getString( JSON_FILENAME );
    }

    public Photo( String filename )
    {
        mFilename = filename;
    }

    public String getFilename()
    {
        return mFilename;
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put( JSON_FILENAME, mFilename );
        return json;
    }
}
