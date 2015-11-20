
package com.example.photogallery.fragment;

import java.util.ArrayList;

import com.example.photogallery.R;
import com.example.photogallery.object.GalleryItem;
import com.example.photogallery.utils.FlickrFetcher;
import com.example.photogallery.utils.ThumbnailDownloader;
import com.example.photogallery.utils.ThumbnailDownloader.Listener;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

public class PhotoGalleryFragment extends Fragment
{
    private static final String TAG = "PhotoGalleryFragment";

    private GridView mGridView;

    private ArrayList< GalleryItem > mItems;

    private ThumbnailDownloader< ImageView > mThumbnailThread;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
        setHasOptionsMenu( true );
        updateItems();

        mThumbnailThread = new ThumbnailDownloader< ImageView >( new Handler() );
        mThumbnailThread.setListener( new Listener< ImageView >()
        {
            @Override
            public void onThumbnailDownload( ImageView token, Bitmap thumbnail )
            {
                if( isVisible() )
                {
                    token.setImageBitmap( thumbnail );
                }
            }
        } );
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i( TAG, "Background thread started." );
    }

    public void updateItems()
    {
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_photo_gallery, container, false );
        mGridView = ( GridView ) v.findViewById( R.id.gridView );
        setupAdapter();
        return v;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i( TAG, "Background thread destroyed." );
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater )
    {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.fragment_photo_gallery, menu );
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
        {
            MenuItem searchItem = menu.findItem( R.id.menu_item_search );
            SearchView searchView = ( SearchView ) searchItem.getActionView();
            SearchManager searchManager = ( SearchManager ) getActivity().getSystemService( Context.SEARCH_SERVICE );
            ComponentName name = getActivity().getComponentName();
            SearchableInfo searchInfo = searchManager.getSearchableInfo( name );
            searchView.setSearchableInfo( searchInfo );
        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences( getActivity() ).edit()
                        .putString( FlickrFetcher.PREF_SEARCH_QUERY, null ).commit();
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    private void setupAdapter()
    {
        if( getActivity() == null || mGridView == null )
        {
            return;
        }
        if( mItems != null )
        {
            mGridView.setAdapter( new GalleryItemAdapter( mItems ) );
        }
        else
        {
            mGridView.setAdapter( null );
        }
    }

    private class FetchItemsTask extends AsyncTask< Void, Void, ArrayList< GalleryItem > >
    {
        @Override
        protected ArrayList< GalleryItem > doInBackground( Void... params )
        {
            Activity activity = getActivity();
            if( activity == null )
            {
                return new ArrayList< GalleryItem >();
            }
            String query = PreferenceManager.getDefaultSharedPreferences( activity )
                    .getString( FlickrFetcher.PREF_SEARCH_QUERY, null );

            FlickrFetcher flickrFetcher = new FlickrFetcher();
            if( query == null )
            {
                return flickrFetcher.fetchItems();
            }
            else
            {
                return flickrFetcher.search( query.trim() );
            }
        }

        @Override
        protected void onPostExecute( ArrayList< GalleryItem > result )
        {
            mItems = result;
            String query = PreferenceManager.getDefaultSharedPreferences( getActivity() )
                    .getString( FlickrFetcher.PREF_SEARCH_QUERY, null );
            setupAdapter();
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter< GalleryItem >
    {
        public GalleryItemAdapter( ArrayList< GalleryItem > items )
        {
            super( getActivity(), 0, items );
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            if( convertView == null )
            {
                convertView = getActivity().getLayoutInflater().inflate( R.layout.gallery_item, parent, false );
            }

            ImageView imageView = ( ImageView ) convertView.findViewById( R.id.gallery_item_imageView );
            imageView.setImageResource( R.drawable.brian_up_close );
            GalleryItem item = getItem( position );
            mThumbnailThread.queueThumbnail( imageView, item.getUrl() );
            new PreloadImagesTask().execute( item );
            return convertView;
        }
    }

    private class PreloadImagesTask extends AsyncTask< GalleryItem, Void, Void >
    {
        @Override
        protected Void doInBackground( GalleryItem... params )
        {
            mThumbnailThread.preLoad( mItems, params[0] );
            return null;
        }
    }
}
