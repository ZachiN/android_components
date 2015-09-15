package com.examples.components.censusapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.examples.components.censusapp.fragments.ContactFragment;

public class CensusApp extends FragmentActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_census_app);
        FragmentManager fm = getSupportFragmentManager();
        Fragment theFragment = fm.findFragmentById(R.id.fragmentContainer);
        if(theFragment == null) {
            theFragment = new ContactFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, theFragment)
                    .commit();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_census_app, menu);
        return true;
    }
}
