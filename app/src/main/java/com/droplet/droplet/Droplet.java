package com.droplet.droplet;

import java.util.Date;

/**
 * Created by Edward on 1/6/2015.
 */
public class Droplet {

    private double longitude;
    private double latitude;
    private int score;
    private String user;
    private String message;
    private long time;

    public Droplet(double lon, double lat, String userIn, String messageIn){
        longitude = lon;
        latitude = lat;
        score = 0;
        user = userIn;
        message = messageIn;
        time = System.currentTimeMillis();
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

    public long getTime() {
        return time;
    }

    public String getHash() {

        return "" + ((int)longitude*1000000 + (int)latitude*1000 + (int)message.hashCode() + (int)time);

    }

}
