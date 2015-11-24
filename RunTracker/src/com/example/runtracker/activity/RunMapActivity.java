
package com.example.runtracker.activity;

import com.example.runtracker.fragment.RunMapFragment;

import android.support.v4.app.Fragment;

public class RunMapActivity extends SingleFragmentActivity
{
    public static final String EXTRA_RUN_ID = "com.example.runtracker.run_id";

    @Override
    protected Fragment createFragment()
    {
        long id = getIntent().getLongExtra( EXTRA_RUN_ID, -1 );
        if( id != -1 )
        {
            return RunMapFragment.newInstance( id );
        }
        return new RunMapFragment();
    }

}
