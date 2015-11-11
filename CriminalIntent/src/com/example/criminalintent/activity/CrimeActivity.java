package com.example.criminalintent.activity;

import com.example.criminalintent.fragment.CrimeFragment;

import android.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeFragment();
    }
}
