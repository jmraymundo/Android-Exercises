
package com.example.photogallery.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.photogallery.object.GalleryItem;

import android.net.Uri;
import android.util.Log;

public class FlickrFetcher
{
    public static final String PREF_LAST_RESULT_ID = "lastResultId";

    public static final String PREF_SEARCH_QUERY = "searchQuery";

    public static final String TAG = "FlickrFetcher";

    private static final String API_KEY = "96d49464b6ab05385f4b33e1d8ee82bd";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest";

    private static final String EXTRA_SMALL_URL = "url_s";

    private static final String KEYWORD_API_KEY = "api_key";

    private static final String KEYWORD_EXTRA = "extras";

    private static final String KEYWORD_METHOD = "method";

    private static final String KEYWORD_TEXT = "text";

    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";

    private static final String METHOD_SEARCH = "flickr.photos.search";

    private static final String XML_PHOTO = "photo";

    public ArrayList< GalleryItem > fetchItems()
    {
        String url = Uri.parse( ENDPOINT ).buildUpon().appendQueryParameter( KEYWORD_METHOD, METHOD_GET_RECENT )
                .appendQueryParameter( KEYWORD_API_KEY, API_KEY ).appendQueryParameter( KEYWORD_EXTRA, EXTRA_SMALL_URL )
                .build().toString();
        return downloadGalleryItems( url );
    }

    public String getUrl( String urlSpec ) throws IOException
    {
        return new String( getUrlBytes( urlSpec ) );
    }

    public byte[] getUrlBytes( String urlSpec ) throws IOException
    {
        URL url = new URL( urlSpec );
        HttpURLConnection connection = ( HttpURLConnection ) url.openConnection();
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if( connection.getResponseCode() != HttpURLConnection.HTTP_OK )
            {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while( ( bytesRead = in.read( buffer ) ) > 0 )
            {
                out.write( buffer, 0, bytesRead );
            }
            out.close();
            byte[] output = out.toByteArray();
            return output;
        }
        finally

        {
            connection.disconnect();
        }

    }

    public void parseItems( ArrayList< GalleryItem > items, XmlPullParser parser )
            throws XmlPullParserException, IOException
    {
        int eventType = parser.next();
        while( eventType != XmlPullParser.END_DOCUMENT )
        {
            if( eventType == XmlPullParser.START_TAG && XML_PHOTO.equals( parser.getName() ) )
            {
                String id = parser.getAttributeValue( null, "id" );
                String caption = parser.getAttributeValue( null, "title" );
                String smallUrl = parser.getAttributeValue( null, EXTRA_SMALL_URL );

                GalleryItem item = new GalleryItem( id, caption, smallUrl );
                items.add( item );
            }
            eventType = parser.next();
        }
    }

    public ArrayList< GalleryItem > search( String query )
    {
        String url = Uri.parse( ENDPOINT ).buildUpon().appendQueryParameter( KEYWORD_METHOD, METHOD_SEARCH )
                .appendQueryParameter( KEYWORD_API_KEY, API_KEY ).appendQueryParameter( KEYWORD_EXTRA, EXTRA_SMALL_URL )
                .appendQueryParameter( KEYWORD_TEXT, query ).build().toString();
        return downloadGalleryItems( url );
    }

    private ArrayList< GalleryItem > downloadGalleryItems( String url )
    {
        ArrayList< GalleryItem > items = new ArrayList< GalleryItem >();
        try
        {
            String xml = getUrl( url );
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput( new StringReader( xml ) );
            parseItems( items, parser );
        }
        catch( IOException e )
        {
            Log.e( TAG, "Failed to fetch items: " + e );
        }
        catch( XmlPullParserException e )
        {
            Log.e( TAG, "Failed to parse items: " + e );
        }
        return items;
    }
}
