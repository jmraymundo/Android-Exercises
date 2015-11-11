package com.example.criminalintent.fragment;

import java.util.UUID;

import com.example.criminalintent.R;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.fragment.crimeId";

    public static final String EXTRA_CRIME = "com.example.criminalintent.fragment.crime";

    private Crime myCrime;

    private Button myDateButton;

    private EditText myEditText;

    private CheckBox mySolvedCheckBox;

    public static CrimeFragment newInstance( UUID id )
    {
        Bundle args = new Bundle();
        args.putSerializable( EXTRA_CRIME_ID, id );
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        UUID id = ( UUID ) getArguments().getSerializable( EXTRA_CRIME_ID );
        myCrime = CrimeLab.get( getActivity() ).getCrime( id );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_crime, container, false );
        myEditText = ( EditText ) v.findViewById( R.id.crime_title );
        myEditText.setText( myCrime.getTitle() );
        myEditText.addTextChangedListener( new TextWatcher()
        {
            @Override
            public void afterTextChanged( Editable s )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                myCrime.setTitle( s.toString() );
            }
        } );

        myDateButton = ( Button ) v.findViewById( R.id.crime_date );
        myDateButton.setText( myCrime.getDate().toString() );
        myDateButton.setEnabled( false );

        mySolvedCheckBox = ( CheckBox ) v.findViewById( R.id.crime_solved );
        mySolvedCheckBox.setChecked( myCrime.isSolved() );
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

    public void returnResult()
    {
        getActivity().setResult( Activity.RESULT_OK, null );
    }
}
