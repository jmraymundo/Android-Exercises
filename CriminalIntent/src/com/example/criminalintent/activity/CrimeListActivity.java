
package com.example.criminalintent.activity;

import com.example.criminalintent.fragment.CrimeListFragment;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}
