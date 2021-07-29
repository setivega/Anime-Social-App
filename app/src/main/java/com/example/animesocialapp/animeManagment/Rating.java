package com.example.animesocialapp.animeManagment;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Rating")
public class Rating extends ParseObject {

    public String rating;

    public static final String KEY_RATING = "rating";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_USER = "user";

    public Rating() {}

    public Rating(JSONObject jsonObject) throws JSONException {
        rating = jsonObject.getString("rating");
    }

    public String getRating() { return getString(KEY_RATING); }

    public void setRating(String rating) { put(KEY_RATING, rating); }

    public Integer getWeight() { return getInt(KEY_WEIGHT); }

    public void setWeight(int weight) { put(KEY_WEIGHT, weight); }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}