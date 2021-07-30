package com.example.animesocialapp.animeManagment;

import com.example.animesocialapp.ParseRelativeDate;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

@ParseClassName("YearRange")
public class YearRange extends ParseObject {

    public String startDate;

    public static final String KEY_YEAR_RANGE = "yearRange";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_USER = "user";

    public static final String fullG = "G - All Ages";
    public static final String fullPG = "PG - Children";
    public static final String fullPG13 = "PG-13 - Teens 13 or older";
    public static final String fullR = "R - 17+ (violence & profanity)";
    public static final String fullRPlus = "R+ - Mild Nudity";
    public static final String fullRX = "Rx - Hentai";

    public YearRange() {}

    public YearRange(JSONObject jsonObject) throws JSONException {
        JSONObject aired = jsonObject.getJSONObject("aired");
        startDate = aired.getString("from");
    }

    public String getYearRangeString(String rawDate) { return ParseRelativeDate.getYearRange(rawDate); }

    public String getYearRange() { return getString(KEY_YEAR_RANGE); }

    public void setYearRange(String yearRange) { put(KEY_YEAR_RANGE, yearRange); }

    public Integer getWeight() { return getInt(KEY_WEIGHT); }

    public void setWeight(int weight) { put(KEY_WEIGHT, weight); }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}
