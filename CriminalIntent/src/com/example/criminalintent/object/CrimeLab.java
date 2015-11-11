
package com.example.criminalintent.object;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.content.Context;

public class CrimeLab
{
    public static CrimeLab myCrimeLab;

    public static CrimeLab get( Context context )
    {
        if( null == myCrimeLab )
        {
            myCrimeLab = new CrimeLab( context.getApplicationContext() );
        }
        return myCrimeLab;
    }

    private Context myContext;

    private ArrayList< Crime > myCrimes;

    private CrimeLab( Context appContext )
    {
        myContext = appContext;
        myCrimes = new ArrayList< Crime >();
        for( int i = 0; i < 100; i++ )
        {
            Crime crime = new Crime();
            crime.setTitle( "Crime #" + i );
            crime.setSolved( i % 2 == 0 );
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime( date );
            c.add( Calendar.YEAR, -2 );
            crime.setDate( c.getTime() );
            myCrimes.add( crime );
        }
    }

    public Crime getCrime( UUID id )
    {
        for( Crime crime : myCrimes )
        {
            if( id.equals( crime.getId() ) )
            {
                return crime;
            }
        }
        return null;
    }

    public ArrayList< Crime > getCrimes()
    {
        return myCrimes;
    }
}
