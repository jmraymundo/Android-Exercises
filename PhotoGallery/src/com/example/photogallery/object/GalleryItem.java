
package com.example.photogallery.object;

public class GalleryItem
{
    private String mCaption;

    private String mId;

    private String mUrl;

    public GalleryItem( String id, String caption, String smallUrl )
    {
        setId( id );
        setCaption( caption );
        setUrl( smallUrl );
    }

    public String toString()
    {
        return mCaption;
    }

    public String getCaption()
    {
        return mCaption;
    }

    public void setCaption( String caption )
    {
        mCaption = caption;
    }

    public String getId()
    {
        return mId;
    }

    public void setId( String id )
    {
        mId = id;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl( String url )
    {
        mUrl = url;
    }
}
