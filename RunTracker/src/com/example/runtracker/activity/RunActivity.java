
package com.example.runtracker.activity;

import com.example.runtracker.fragment.RunFragment;

import android.support.v4.app.Fragment;

public class RunActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new RunFragment();
    }
}
