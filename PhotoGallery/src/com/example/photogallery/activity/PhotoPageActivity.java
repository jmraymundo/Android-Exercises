
package com.example.photogallery.activity;

import com.example.photogallery.fragment.PhotoPageFragment;

import android.support.v4.app.Fragment;

public class PhotoPageActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new PhotoPageFragment();
    }
}
