
package com.example.hellomoon.activity;

import com.example.hellomoon.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class HelloMoonActivity extends FragmentActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_hello_moon );
    }
}
