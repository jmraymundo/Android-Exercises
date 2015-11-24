
package com.example.runtracker.activity;

import com.example.runtracker.fragment.RunListFragment;

import android.support.v4.app.Fragment;

public class RunListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new RunListFragment();
    }
}
