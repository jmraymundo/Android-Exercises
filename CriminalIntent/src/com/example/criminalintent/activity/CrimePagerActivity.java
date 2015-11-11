
package com.example.criminalintent.activity;

import java.util.ArrayList;

import com.example.criminalintent.R;
import com.example.criminalintent.fragment.CrimeFragment;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity
{
    private ViewPager myViewPager;

    private ArrayList< Crime > myCrimes;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        myViewPager = new ViewPager( this );
        myViewPager.setId( R.id.viewPager );
        setContentView( myViewPager );
        myCrimes = CrimeLab.get( this ).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        myViewPager.setAdapter( new FragmentStatePagerAdapter( fm)
        {

            @Override
            public int getCount()
            {
                return myCrimes.size();
            }

            @Override
            public Fragment getItem( int arg0 )
            {
                Crime crime = myCrimes.get( arg0 );
                return CrimeFragment.newInstance( crime.getId() );
            }
        } );
    }

}
