
package com.example.photogallery.fragment;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.example.photogallery.R;
import com.example.photogallery.utils.FlickrFetcher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class PhotoGalleryFragment extends Fragment
{
    private GridView mGridView;

    private static final String TAG = "PhotoGalleryFragment";

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_photo_gallery, container, false );
        mGridView = ( GridView ) v.findViewById( R.id.gridView );
        return v;
    }

    private class FetchItemsTask extends AsyncTask< Void, Void, Void >
    {
        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                String result = new FlickrFetcher().getUrl( "https://www.google.com" );
                Log.i( TAG, "Fetched contents of URL: " + result );
            }
            catch( IOException e )
            {
                Log.e( TAG, "Failed to fetch URL: " + e );
            }
            return null;
        }

    }
}
