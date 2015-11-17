
package com.example.photogallery.fragment;

import com.example.photogallery.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class PhotoGalleryFragment extends Fragment
{
    private GridView mGridView;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_photo_gallery, container, false );
        mGridView = ( GridView ) v.findViewById( R.id.gridView );
        return v;
    }
}
