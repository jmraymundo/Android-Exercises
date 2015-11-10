package com.example.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class CrimeActivity extends FragmentActivity
{

    public final static String CRIMINAL_INTENT = "CriminalIntent";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        Log.d( CRIMINAL_INTENT, "inside onCreate at CrimeActivity" );
        setContentView( R.layout.activity_crime );

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById( R.id.fragmentContainer );

        if( null == fragment )
        {
            fragment = new CrimeFragment();
            fm.beginTransaction().add( R.id.fragmentContainer, fragment ).commit();
        }
    }
}
