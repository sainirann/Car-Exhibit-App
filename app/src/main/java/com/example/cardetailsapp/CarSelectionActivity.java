package com.example.cardetailsapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Main car selection activity after login
 */
public class CarSelectionActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selection);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        // If this is the car information fragment, don't show it until a particular car is selected based on model and make
        if (fragment instanceof CarInformation) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }
    }
}
