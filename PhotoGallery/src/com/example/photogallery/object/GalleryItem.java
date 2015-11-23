
package com.example.photogallery.object;

public class GalleryItem
{
    private String mCaption;

    private String mId;

    private String mOwner;

    private String mUrl;

    public GalleryItem( String id, String caption, String smallUrl )
    {
        setId( id );
        setCaption( caption );
        setUrl( smallUrl );
    }

    public String getCaption()
    {
        return mCaption;
    }

    public String getId()
    {
        return mId;
    }

    public String getOwner()
    {
        return mOwner;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public void setCaption( String caption )
    {
        mCaption = caption;
    }

    public void setId( String id )
    {
        mId = id;
    }

    public void setOwner( String owner )
    {
        mOwner = owner;
    }

    public void setUrl( String url )
    {
        mUrl = url;
    }

    @Override
    public String toString()
    {
        return mCaption;
    }
}
