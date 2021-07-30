package com.example.animesocialapp.animeManagment;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

@ParseClassName("Rating")
public class Rating extends ParseObject {

    public String rating;

    public static final String KEY_RATING = "rating";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_USER = "user";

    public static final String fullG = "G - All Ages";
    public static final String fullPG = "PG - Children";
    public static final String fullPG13 = "PG-13 - Teens 13 or older";
    public static final String fullR = "R - 17+ (violence & profanity)";
    public static final String fullRPlus = "R+ - Mild Nudity";
    public static final String fullRX = "Rx - Hentai";

    private String G = "G";
    private String PG = "PG";
    private String PG13 = "PG-13";
    private String R = "R";
    private String RPlus = "R+";
    private String RX = "Rx";

    public Rating() {}

    public Rating(JSONObject jsonObject) throws JSONException {
        rating = jsonObject.getString("rating");
    }

    public String getRatingString(String rating) {
        Timber.i(rating);
        switch (rating) {
            case fullPG:
                return PG;
            case fullPG13:
                return PG13;
            case fullR:
                return R;
            case fullRPlus:
                return RPlus;
            case fullRX:
                return RX;
            default:
                return G;
        }
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