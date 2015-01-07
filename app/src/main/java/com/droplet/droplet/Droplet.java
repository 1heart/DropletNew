package com.droplet.droplet;

/**
 * Created by Edward on 1/6/2015.
 */
public class Droplet {

    private double longitude;
    private double latitude;
    private int score;
    private String user;
    private String message;

    public Droplet(double lon, double lat, String userIn, String messageIn){
        longitude = lon;
        latitude = lat;
        score = 0;
        user = userIn;
        message = messageIn;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getScore() {
        return score;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }
}
