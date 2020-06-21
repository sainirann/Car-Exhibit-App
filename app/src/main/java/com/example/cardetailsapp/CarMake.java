package com.example.cardetailsapp;

/**
 * Car make Representation class
 */
public class CarMake {

    static final CarMake EMPTY = new CarMake(-1, "");

    private int id;
    private String vehicleMake;

    CarMake(int id, String vehicleMake) {
        this.id = id;
        this.vehicleMake = vehicleMake;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return vehicleMake;
    }
}
