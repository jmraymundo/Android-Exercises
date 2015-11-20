
package com.example.photogallery.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.photogallery.object.GalleryItem;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ThumbnailDownloader< Token > extends HandlerThread
{
    private static boolean isAlreadyPreloading = false;

    private static final int MESSAGE_DOWNLOAD = 0;

    private static final int PRELOAD_RANGE = 10;

    private static final String TAG = "ThumbnailDownloader";

    private Handler mHandler;

    private LruCache< String, Bitmap > mImageCache = new LruCache< String, Bitmap >( 60 );

    private Listener< Token > mListener;

    private Handler mResponseHandler;

    private Map< Token, String > requestMap = Collections.synchronizedMap( new HashMap< Token, String >() );

    public ThumbnailDownloader( Handler responseHandler )
    {
        super( TAG );
        mResponseHandler = responseHandler;
    }

    public void clearQueue()
    {
        mHandler.removeMessages( MESSAGE_DOWNLOAD );
        requestMap.clear();
        mImageCache.evictAll();
    }

    public synchronized void preLoad( ArrayList< GalleryItem > items, GalleryItem param )
    {
        while( isAlreadyPreloading )
        {
            try
            {
                wait();
            }
            catch( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
        isAlreadyPreloading = true;
        int paramIndex = items.indexOf( param );
        int original;
        for( int current = original = paramIndex - 1; current >= 0 && current > original - 10; current-- )
        {
            executePreload( items, param, current );
        }
        for( int current = original = paramIndex + 1; current < items.size()
                && current < original + PRELOAD_RANGE; current++ )
        {
            executePreload( items, param, current );
        }
        isAlreadyPreloading = false;
        notify();
    }

    public void queueThumbnail( Token token, String url )
    {
        requestMap.put( token, url );
        mHandler.obtainMessage( MESSAGE_DOWNLOAD, token ).sendToTarget();
    }

    public void setListener( Listener< Token > listener )
    {
        mListener = listener;
    }

    private void executePreload( ArrayList< GalleryItem > items, GalleryItem currentItem, int relatedItemIndex )
    {
        GalleryItem item = items.get( relatedItemIndex );
        preLoadImage( item );
    }

    private Bitmap getImage( String url )
    {
        try
        {
            byte[] bitmapBytes = new FlickrFetcher().getUrlBytes( url );
            Bitmap image = BitmapFactory.decodeByteArray( bitmapBytes, 0, bitmapBytes.length );
            mImageCache.put( url, image );
            return image;
        }
        catch( IOException e )
        {
            Log.e( TAG, "Error downloading image", e );
        }
        return null;
    }

    private void handleRequest( final Token token )
    {
        final String url = requestMap.get( token );
        if( url == null )
        {
            return;
        }
        final Bitmap bitmap;
        Bitmap tempBitmap = mImageCache.get( url );
        if( null == tempBitmap )
        {
            bitmap = getImage( url );
        }
        else
        {
            bitmap = tempBitmap;
        }

        mResponseHandler.post( new Runnable()
        {
            @Override
            public void run()
            {
                if( requestMap.get( token ) != url )
                {
                    return;
                }
                requestMap.remove( token );
                mListener.onThumbnailDownload( token, bitmap );
            }
        } );
    }

    private void preLoadImage( GalleryItem item )
    {
        String url = item.getUrl();
        try
        {
            if( null == mImageCache.get( url ) )
            {
                Bitmap bitmap = getImage( url );
                mImageCache.put( url, bitmap );
            }
        }
        catch( NullPointerException e )
        {
            Log.e( TAG, "Url missing", e );
        }
    }

    @SuppressLint( "HandlerLeak" )
    @Override
    protected void onLooperPrepared()
    {
        mHandler = new Handler()
        {
            @SuppressWarnings( "unchecked" )
            @Override
            public void handleMessage( Message msg )
            {
                if( msg.what == MESSAGE_DOWNLOAD )
                {
                    Token token = ( Token ) msg.obj;
                    handleRequest( token );
                }
            }
        };
    }

    public interface Listener< Token >
    {
        void onThumbnailDownload( Token token, Bitmap bitmap );
    }
}
