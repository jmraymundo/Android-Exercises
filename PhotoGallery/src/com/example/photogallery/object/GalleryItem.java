
package com.example.photogallery.object;

public class GalleryItem
{
    private static final String FORWARD_SLASH = "/";

    private static final String PHOTO_PAGE_URL_ROOT = "https://www.flickr.com/photos/";

    private String mCaption;

    private String mId;

    private String mOwner;

    private String mUrl;

    public GalleryItem( String id, String caption, String smallUrl, String owner )
    {
        this();
        setId( id );
        setCaption( caption );
        setUrl( smallUrl );
        setOwner( owner );
    }

    public GalleryItem()
    {
        mCaption = null;
        mId = null;
        mOwner = null;
        mUrl = null;
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

    public String getPhotoPageUrl()
    {
        return PHOTO_PAGE_URL_ROOT + getOwner() + FORWARD_SLASH + getId();
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
