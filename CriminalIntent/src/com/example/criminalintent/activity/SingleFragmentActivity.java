package com.example.criminalintent.activity;

import com.example.criminalintent.R;
import com.example.criminalintent.fragment.CrimeFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public abstract class SingleFragmentActivity extends Activity
{
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fragment );

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById( R.id.fragmentContainer );

        if( null == fragment )
        {
            fragment = new CrimeFragment();
            fm.beginTransaction().add( R.id.fragmentContainer, fragment ).commit();
        }
    }
}
