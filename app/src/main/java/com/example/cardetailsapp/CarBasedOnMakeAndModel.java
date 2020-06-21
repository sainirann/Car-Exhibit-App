package com.example.cardetailsapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Cars based on make and model representartion class
 */
public class CarBasedOnMakeAndModel implements Parcelable {

    String color;
    String createdTime;
    int id;
    String imageUrl;
    int mileage;
    String model;
    int price;
    String vehicleDescription;
    String vehicleMake;
    String vehicleUrl;
    String vinNumber;

    CarBasedOnMakeAndModel(String color, String createdTime, int id, String imageUrl, int mileage,
                           String model, int price, String vehicleDescription, String vehicleMake,
                           String vehicleUrl, String vinNumber) {
        this.color = color;
        this.createdTime = createdTime;
        this.id = id;
        this.imageUrl = imageUrl;
        this.mileage = mileage;
        this.model = model;
        this.price = price;
        this.vehicleDescription = vehicleDescription;
        this.vehicleMake = vehicleMake;
        this.vehicleUrl = vehicleUrl;
        this.vinNumber = vinNumber;
    }

    private CarBasedOnMakeAndModel(Parcel in) {
        color = in.readString();
        createdTime = in.readString();
        id = in.readInt();
        imageUrl = in.readString();
        mileage = in.readInt();
        model = in.readString();
        price = in.readInt();
        vehicleDescription = in.readString();
        vehicleMake = in.readString();
        vehicleUrl = in.readString();
        vinNumber = in.readString();
    }

    //Parcel creator
    public static final Creator<CarBasedOnMakeAndModel> CREATOR = new Creator<CarBasedOnMakeAndModel>() {
        @Override
        public CarBasedOnMakeAndModel createFromParcel(Parcel in) {
            return new CarBasedOnMakeAndModel(in);
        }

        @Override
        public CarBasedOnMakeAndModel[] newArray(int size) {
            return new CarBasedOnMakeAndModel[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public String getModel() {
        return model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeString(createdTime);
        dest.writeInt(id);
        dest.writeString(imageUrl);
        dest.writeInt(mileage);
        dest.writeString(model);
        dest.writeInt(price);
        dest.writeString(vehicleDescription);
        dest.writeString(vehicleMake);
        dest.writeString(vehicleUrl);
        dest.writeString(vinNumber);
    }
}
