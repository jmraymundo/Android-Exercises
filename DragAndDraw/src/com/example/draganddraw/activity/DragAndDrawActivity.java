
package com.example.draganddraw.activity;

import com.example.draganddraw.fragment.DragAndDrawFragment;

import android.support.v4.app.Fragment;

public class DragAndDrawActivity extends SingleFragmentActivity
{

    @Override
    public Fragment createFragment()
    {
        return new DragAndDrawFragment();
    }

}
