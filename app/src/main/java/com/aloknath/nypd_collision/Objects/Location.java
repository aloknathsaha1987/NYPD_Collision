package com.aloknath.nypd_collision.Objects;

/**
 * Created by ALOKNATH on 4/11/2015.
 */
public class Location {

    private boolean needs_recoding;
    private String latitude = "";
    private String longitude = "";

    public boolean isNeeds_recoding() {
        return needs_recoding;
    }

    public void setNeeds_recoding(boolean needs_recoding) {
        this.needs_recoding = needs_recoding;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                " latitude='" + latitude + '\'' +
                "\n longitude='" + longitude + '\'' +
                '}';
    }
}
