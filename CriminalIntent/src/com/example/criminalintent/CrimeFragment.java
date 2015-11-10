package com.example.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CrimeFragment extends Fragment
{
    private Crime myCrime;

    private EditText myEditText;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        Log.d( CrimeActivity.CRIMINAL_INTENT, "inside onCreate at CrimeFragment" );
        super.onCreate( savedInstanceState );
        myCrime = new Crime();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        Log.d( CrimeActivity.CRIMINAL_INTENT, "inside onCreateView at CrimeFragment" );
        View v = inflater.inflate( R.layout.fragment_crime, container, false );
        myEditText = ( EditText ) v.findViewById( R.id.crime_title );
        myEditText.addTextChangedListener( new TextWatcher()
        {
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                myCrime.setTitle( s.toString() );
            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged( Editable s )
            {
                // TODO Auto-generated method stub

            }
        } );
        return v;
    }
}
