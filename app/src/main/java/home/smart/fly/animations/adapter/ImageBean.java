package home.smart.fly.animations.adapter;

import java.io.Serializable;

/**
 * Created by co-mall on 2017/6/16.
 */

public class ImageBean implements Serializable {
    private String filepath;
    private double longitude, latitude;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
