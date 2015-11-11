package com.example.criminalintent.activity;

import java.util.UUID;

import com.example.criminalintent.fragment.CrimeFragment;

import android.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        UUID id = ( UUID ) getIntent().getSerializableExtra( CrimeFragment.EXTRA_CRIME_ID );
        return CrimeFragment.newInstance( id );
    }
}
