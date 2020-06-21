package com.example.cardetailsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

/**
 * Car information fragment which shows the details of the selected car.
 */
public class CarInformation extends Fragment {

    TextView displayMake, displayModel, displayPrice, displayVINNumber,displayDesc;
    TextView displayColor, displayCarID, displayMilage, displayLastUpdateDate;
    ImageView displayImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            updateFragment(view);
        }
    }

    /**
     * Update the fragment with car details
     * @param view view to fetch information
     */
    private void updateFragment(@NonNull View view) {
        Bundle bundle = getArguments();
        if (bundle != null) { // bundle can be null if the arguments are not yet set.
            CarBasedOnMakeAndModel basedOnMakeAndModel = bundle.getParcelable("MakeAndModel");

            displayMake = view.findViewById(R.id.display_make);
            displayModel = view.findViewById(R.id.display_model);
            displayPrice = view.findViewById(R.id.display_car_price);
            displayVINNumber = view.findViewById(R.id.display_vin_number);
            displayColor = view.findViewById(R.id.display_color);
            displayCarID = view.findViewById(R.id.display_car_id);
            displayMilage = view.findViewById(R.id.display_milage);
            displayLastUpdateDate = view.findViewById(R.id.last_update_date);
            displayImg = view.findViewById(R.id.display_img);
            displayDesc = view.findViewById(R.id.display_desc);

            if (basedOnMakeAndModel != null) {
                displayMake.setText(basedOnMakeAndModel.vehicleMake);
                displayModel.setText(basedOnMakeAndModel.model);
                displayPrice.setText(String.valueOf(basedOnMakeAndModel.price));
                displayDesc.setText(basedOnMakeAndModel.vehicleDescription);
                displayVINNumber.setText(basedOnMakeAndModel.vinNumber);
                displayColor.setText(basedOnMakeAndModel.color);
                displayCarID.setText(String.valueOf(basedOnMakeAndModel.id));
                displayMilage.setText(String.valueOf(basedOnMakeAndModel.mileage));
                displayLastUpdateDate.setText(basedOnMakeAndModel.createdTime);
                //Glide.with(getContext()).asBitmap().load(basedOnMakeAndModel.imageUrl).into(displayImg);
                Glide.with(getContext()).asBitmap().load(basedOnMakeAndModel.imageUrl)
                        .placeholder(R.drawable.image_not_found).into(displayImg);
            }
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        //view can be null on phone, where before creating the activity this is called.
        if (getView() != null) {
            updateFragment(getView());
        }
    }
}
