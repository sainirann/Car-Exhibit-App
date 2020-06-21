package com.example.cardetailsapp;


/**
 * Car Model Representation class
 */
public class CarModel {

    static final CarModel EMPTY = new CarModel(-1, "", -1);

    private int id;
    private String model;
    private int vehicleMakeId;

    CarModel(int id, String model, int vehicleMakeId) {
        this.id = id;
        this.model = model;
        this.vehicleMakeId = vehicleMakeId;
    }

    public int getId() {
        return id;
    }

    public int getVehicleMakeId() {
        return vehicleMakeId;
    }

    @Override
    public String toString() {
        return model;
    }
}
