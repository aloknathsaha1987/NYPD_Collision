package com.aloknath.nypd_collision.JSONParser;

import com.aloknath.nypd_collision.Objects.CollisionDetail;
import com.aloknath.nypd_collision.Objects.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 4/11/2015.
 */
public class JSONParser {

    private static List<CollisionDetail> collisionDetails = new ArrayList<>();

    public static List<CollisionDetail> getCollisionObjects(String jsonString){

        ObjectMapper mapper = new ObjectMapper();
        try {

            collisionDetails = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, CollisionDetail.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return collisionDetails;
    }

    public static List<CollisionDetail> parseFeed(String content) {

        try {

            JSONArray objs = new JSONArray(content);
            ObjectMapper mapper = new ObjectMapper();

            for (int i = 0; i < objs.length(); i++) {

                JSONObject obj = objs.getJSONObject(i);
                Location location = new Location();

                CollisionDetail collisionDetail = new CollisionDetail(obj.getString("unique_key"));

                collisionDetail.setNumber_of_persons_killed(obj.getString("number_of_persons_killed"));
                collisionDetail.setNumber_of_motorist_injured(obj.getString("number_of_motorist_injured"));
                collisionDetail.setDate(obj.getString("date"));
                collisionDetail.setNumber_of_pedestrians_injured(obj.getString("number_of_pedestrians_injured"));

                JsonNode rootNode = mapper.readValue(obj.toString(), JsonNode.class);
                JsonNode locale = rootNode.get("location");

                if (locale != null){
                    location = mapper.treeToValue(locale , Location.class);
                }

                collisionDetail.setLocation(location);
                collisionDetails.add(collisionDetail);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();
        }
        return collisionDetails;
    }
}
