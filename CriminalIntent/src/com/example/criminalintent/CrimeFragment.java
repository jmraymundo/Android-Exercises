package com.example.criminalintent;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment
{
    private Crime myCrime;

    private EditText myEditText;

    private Button myDateButton;

    private CheckBox mySolvedCheckBox;

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

        myDateButton = ( Button ) v.findViewById( R.id.crime_date );
        myDateButton.setText( DateFormat.format( "EEEE, MMM dd, yyyy", myCrime.getDate() ) );
        myDateButton.setEnabled( false );

        mySolvedCheckBox = ( CheckBox ) v.findViewById( R.id.crime_solved );
        mySolvedCheckBox.setOnCheckedChangeListener( new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
            {
                myCrime.setSolved( isChecked );
            }
        } );
        return v;
    }
}
