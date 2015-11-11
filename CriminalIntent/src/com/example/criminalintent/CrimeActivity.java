package com.example.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class CrimeActivity extends Activity
{

    public final static String CRIMINAL_INTENT = "CriminalIntent";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        Log.d( CRIMINAL_INTENT, "inside onCreate at CrimeActivity" );
        setContentView( R.layout.activity_crime );

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById( R.id.fragmentContainer );

        if( null == fragment )
        {
            fragment = new CrimeFragment();
            fm.beginTransaction().add( R.id.fragmentContainer, fragment ).commit();
        }
    }
}
