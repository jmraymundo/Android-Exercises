package com.example.criminalintent.fragment;

import java.util.ArrayList;

import com.example.criminalintent.R;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;

import android.app.ListFragment;
import android.os.Bundle;

public class CrimeListFragment extends ListFragment
{
    private ArrayList< Crime > myCrimes;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        getActivity().setTitle( R.string.crimes_title );

        myCrimes = CrimeLab.get( getActivity() ).getCrimes();
    }
}
