
package com.example.criminalintent.fragment;

import java.util.Date;
import java.util.UUID;

import com.example.criminalintent.R;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment
{
    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.fragment.crimeId";

    public static final String DIALOG_DATETIME = "com.example.criminalintent.fragment.datetime";

    public static final int REQUEST_DATE = 0;

    public static final int REQUEST_TIME = 1;

    private Crime myCrime;

    private Button myDateTimeButton;

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
        setHasOptionsMenu( true );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case ( android.R.id.home ):
                if( NavUtils.getParentActivityName( getActivity() ) != null )
                {
                    NavUtils.navigateUpFromSameTask( getActivity() );
                }
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @TargetApi( Build.VERSION_CODES.HONEYCOMB )
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_crime, container, false );

        if( NavUtils.getParentActivityName( getActivity() ) != null
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
        {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled( true );
        }
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

        myDateTimeButton = ( Button ) v.findViewById( R.id.crime_date );
        updateDate();
        Log.d( "CriminalIntent",
                "Initial date: " + DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", myCrime.getDateTime() ) );
        myDateTimeButton.setEnabled( true );
        myDateTimeButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Log.d( "CriminalIntent", "Changing date and time!" );

                FragmentManager fm = getActivity().getSupportFragmentManager();
                DateTimePickerFragment picker = DateTimePickerFragment.newInstance( myCrime.getDateTime() );
                picker.setTargetFragment( CrimeFragment.this, Integer.MIN_VALUE );
                picker.show( fm, DIALOG_DATETIME );

            }
        } );

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

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void updateDate()
    {
        myDateTimeButton.setText( DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", myCrime.getDateTime() ) );
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        Log.d( "CriminalIntent", "Result received!" );
        if( Activity.RESULT_OK != resultCode )
        {
            Log.d( "CriminalIntent", "Result code not ok!" );
            return;
        }
        if( REQUEST_DATE == requestCode )
        {
            Date date = ( Date ) data.getSerializableExtra( DatePickerFragment.EXTRA_DATE );
            Log.d( "CriminalIntent", "Result code ok!" );
            Log.d( "CriminalIntent", "New date: " + DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", date ) );
            myCrime.setDateTime( date );
            updateDate();
        }
        else if( REQUEST_TIME == requestCode )
        {
            Date date = ( Date ) data.getSerializableExtra( TimePickerFragment.EXTRA_TIME );
            Log.d( "CriminalIntent", "Result code ok!" );
            Log.d( "CriminalIntent", "New date: " + DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", date ) );
            myCrime.setDateTime( date );
            updateDate();
        }
    }
}
