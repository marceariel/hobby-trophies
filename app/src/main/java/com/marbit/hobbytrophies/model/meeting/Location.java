package com.marbit.hobbytrophies.model.meeting;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable{
    private String description;
    private double latitude;
    private double longitude;

    public Location(){
        description = "";
        latitude = 0;
        longitude = 0;
    }

    protected Location(Parcel in) {
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Location(String address, double latitude, double longitude) {
        this.description = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
