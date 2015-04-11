package com.aloknath.nypd_collision.Objects;

/**
 * Created by ALOKNATH on 4/11/2015.
 */
public class CollisionDetail {

    private String number_of_persons_killed;
    private Location location;
    private String number_of_motorist_injured;
    private String date;
    private String number_of_pedestrians_injured;
    private final String unique_key;


    public CollisionDetail(String uniqueKey){
        this.unique_key = uniqueKey;
    }

    public String getNumber_of_persons_killed() {
        return number_of_persons_killed;
    }

    public void setNumber_of_persons_killed(String number_of_persons_killed) {
        this.number_of_persons_killed = number_of_persons_killed;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNumber_of_motorist_injured() {
        return number_of_motorist_injured;
    }

    public void setNumber_of_motorist_injured(String number_of_motorist_injured) {
        this.number_of_motorist_injured = number_of_motorist_injured;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber_of_pedestrians_injured() {
        return number_of_pedestrians_injured;
    }

    public void setNumber_of_pedestrians_injured(String number_of_pedestrians_injured) {
        this.number_of_pedestrians_injured = number_of_pedestrians_injured;
    }

    @Override
    public String toString() {

        return number_of_persons_killed + ":" + number_of_motorist_injured + ":"
                + date.split("T")[0] + ":" + number_of_pedestrians_injured;

    }
}
