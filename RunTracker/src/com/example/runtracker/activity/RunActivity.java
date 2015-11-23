
package com.example.runtracker.activity;

import com.example.runtracker.R;
import com.example.runtracker.fragment.RunFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class RunActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new RunFragment();
    }
}
