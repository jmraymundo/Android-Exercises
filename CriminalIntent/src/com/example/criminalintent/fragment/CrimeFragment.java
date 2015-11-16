
package com.example.criminalintent.fragment;

import java.util.Date;
import java.util.UUID;

import com.example.criminalintent.R;
import com.example.criminalintent.activity.CrimeCameraActivity;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;
import com.example.criminalintent.object.Photo;
import com.example.criminalintent.utils.PictureUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CrimeFragment extends Fragment
{
    public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";

    public static final String TAG = "CrimeFragment";

    private static final String DIALOG_DATE = "date";

    private static final String DIALOG_IMAGE = "image";

    private static final int REQUEST_DATE = 0;

    private static final int REQUEST_PHOTO = 1;

    private static final int REQUEST_CONTACT = 2;

    Crime mCrime;

    Button mDateButton;

    ImageButton mPhotoButton;

    ImageView mPhotoView;

    CheckBox mSolvedCheckBox;

    EditText mTitleField;

    Button mSuspectButton;

    private Button mCallButton;

    public static CrimeFragment newInstance( UUID crimeId )
    {
        Bundle args = new Bundle();
        args.putSerializable( EXTRA_CRIME_ID, crimeId );

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( resultCode != Activity.RESULT_OK ) return;
        if( requestCode == REQUEST_DATE )
        {
            Date date = ( Date ) data.getSerializableExtra( DatePickerFragment.EXTRA_DATE );
            mCrime.setDate( date );
            updateDate();
        }
        else if( requestCode == REQUEST_PHOTO )
        {
            String filename = data.getStringExtra( CrimeCameraFragment.EXTRA_PHOTO_FILENAME );
            if( filename != null )
            {
                Photo photo = new Photo( filename );
                if( mCrime.getPhoto() != null )
                {
                    Photo oldPhoto = mCrime.getPhoto();
                    getActivity().deleteFile( oldPhoto.getFilename() );
                }
                mCrime.setPhoto( photo );
                showPhoto();
            }
        }
        else if( requestCode == REQUEST_CONTACT )
        {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
            Cursor cursor = getActivity().getContentResolver().query( contactUri, queryFields, null, null, null );
            if( cursor.getCount() == 0 )
            {
                cursor.close();
                return;
            }
            cursor.moveToFirst();
            String suspect = cursor.getString( 0 );
            mCrime.setSuspect( suspect );
            mSuspectButton.setText( suspect );
            mCallButton.setEnabled( mCrime.getSuspect() != null );
            cursor.close();
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        UUID crimeId = ( UUID ) getArguments().getSerializable( EXTRA_CRIME_ID );
        mCrime = CrimeLab.get( getActivity() ).getCrime( crimeId );

        setHasOptionsMenu( true );
    }

    @Override
    @TargetApi( 11 )
    public View onCreateView( LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_crime, parent, false );

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
        {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled( true );
        }

        mTitleField = ( EditText ) v.findViewById( R.id.crime_title );
        mTitleField.setText( mCrime.getTitle() );
        mTitleField.addTextChangedListener( new TextWatcher()
        {
            public void afterTextChanged( Editable c )
            {
                // this one too
            }

            public void beforeTextChanged( CharSequence c, int start, int count, int after )
            {
                // this space intentionally left blank
            }

            public void onTextChanged( CharSequence c, int start, int before, int count )
            {
                mCrime.setTitle( c.toString() );
            }
        } );

        mDateButton = ( Button ) v.findViewById( R.id.crime_date );
        updateDate();
        mDateButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance( mCrime.getDate() );
                dialog.setTargetFragment( CrimeFragment.this, REQUEST_DATE );
                dialog.show( fm, DIALOG_DATE );
            }
        } );

        mSolvedCheckBox = ( CheckBox ) v.findViewById( R.id.crime_solved );
        mSolvedCheckBox.setChecked( mCrime.isSolved() );
        mSolvedCheckBox.setOnCheckedChangeListener( new OnCheckedChangeListener()
        {
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
            {
                // set the crime's solved property
                mCrime.setSolved( isChecked );
            }
        } );

        mPhotoButton = ( ImageButton ) v.findViewById( R.id.crime_imageButton );
        mPhotoButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                // launch the camera activity
                Intent i = new Intent( getActivity(), CrimeCameraActivity.class );
                startActivityForResult( i, REQUEST_PHOTO );
            }
        } );

        // if camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        if( !pm.hasSystemFeature( PackageManager.FEATURE_CAMERA )
                && !pm.hasSystemFeature( PackageManager.FEATURE_CAMERA_FRONT ) )
        {
            mPhotoButton.setEnabled( false );
        }

        mPhotoView = ( ImageView ) v.findViewById( R.id.crime_imageView );
        mPhotoView.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Photo p = mCrime.getPhoto();
                if( p == null )
                {
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath( p.getFilename() ).getAbsolutePath();
                ImageFragment.newInstance( path ).show( fm, DIALOG_IMAGE );
            }
        } );

        mSuspectButton = ( Button ) v.findViewById( R.id.crime_suspectButton );
        mSuspectButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Intent intent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
                startActivityForResult( intent, REQUEST_CONTACT );
            }
        } );
        if( mCrime.getSuspect() != null )
        {
            mSuspectButton.setText( mCrime.getSuspect() );
        }

        Button reportButton = ( Button ) v.findViewById( R.id.crime_reportButton );
        reportButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Intent intent = new Intent( Intent.ACTION_SEND );
                intent.setType( "text/plain" );
                intent.putExtra( Intent.EXTRA_TEXT, getCrimeReport() );
                intent.putExtra( Intent.EXTRA_SUBJECT, getString( R.string.crime_report_subject ) );
                intent = Intent.createChooser( intent, getString( R.string.send_report ) );
                startActivity( intent );
            }
        } );

        mCallButton = ( Button ) v.findViewById( R.id.crime_callSuspectButton );
        mCallButton.setEnabled( mCrime.getSuspect() != null );
        mCallButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                dialSuspectNumber();
            }

            private void dialSuspectNumber()
            {
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER };
                String query = ContactsContract.Contacts.DISPLAY_NAME + "='" + mCrime.getSuspect() + "'";
                Cursor suspectContact = getActivity().getContentResolver().query( uri, queryFields, query, null, null );
                if( suspectContact.getCount() == 0 )
                {
                    suspectContact.close();
                    return;
                }
                suspectContact.moveToFirst();
                String suspectNumber = suspectContact.getString( 1 );
                Uri number = Uri.parse( "tel:" + suspectNumber );
                Intent intent = new Intent( Intent.ACTION_DIAL, number );
                startActivity( intent );
            }
        } );
        return v;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask( getActivity() );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get( getActivity() ).saveCrimes();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        PictureUtils.cleanImageView( mPhotoView );
    }

    public void updateDate()
    {
        mDateButton.setText( mCrime.getDate().toString() );
    }

    private void showPhoto()
    {
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if( p != null )
        {
            String path = getActivity().getFileStreamPath( p.getFilename() ).getAbsolutePath();
            b = PictureUtils.getScaledDrawable( getActivity(), path );
        }
        mPhotoView.setImageDrawable( b );
    }

    private String getCrimeReport()
    {
        String solvedString = null;
        if( mCrime.isSolved() )
        {
            solvedString = getString( R.string.crime_report_solved );
        }
        else
        {
            solvedString = getString( R.string.crime_report_unsolved );
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format( dateFormat, mCrime.getDate() ).toString();

        String suspect = mCrime.getSuspect();
        if( suspect == null )
        {
            suspect = getString( R.string.crime_report_no_suspect );
        }
        else
        {
            suspect = getString( R.string.crime_report_suspect, suspect );
        }
        String report = getString( R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect );
        return report;
    }
}
