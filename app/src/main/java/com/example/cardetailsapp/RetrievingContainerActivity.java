package com.example.cardetailsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

/**
 * Activity which renders the Car information details in the fragment on smaller screen sizes.
 */
public class RetrievingContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieving_container);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof CarInformation) {
            //Set arguments for the rendering fragment.
            Intent i = getIntent();
            CarBasedOnMakeAndModel basedOnMakeAndModel = i.getParcelableExtra("MakeAndModel");

            Bundle bundle = new Bundle();
            bundle.putParcelable("MakeAndModel", basedOnMakeAndModel);
            fragment.setArguments(bundle);
        }
    }
}
