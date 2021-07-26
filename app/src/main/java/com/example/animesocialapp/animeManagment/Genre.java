package com.example.animesocialapp.animeManagment;

import com.example.animesocialapp.ParseRelativeDate;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Genre")
public class Genre extends ParseObject {

    public String genreID;
    public String name;

    public static final String KEY_GENRE_ID = "genreID";
    public static final String KEY_NAME = "name";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_USER = "user";


    public Genre() {}

    public Genre(JSONObject jsonObject) throws JSONException {
        genreID = String.valueOf(jsonObject.getInt("mal_id"));
        name = jsonObject.getString("name");
    }

    public static List<Genre> fromJSONArray(JSONArray genreJsonArray) throws JSONException {
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genreJsonArray.length(); i++) {
            genres.add(new Genre(genreJsonArray.getJSONObject(i)));
        }
        return genres;
    }

    public String getGenreID() { return getString(KEY_GENRE_ID); }

    public void setGenreID(String genreID) { put(KEY_GENRE_ID, genreID); }

    public String getName() { return getString(KEY_NAME); }

    public void setName(String name) { put(KEY_NAME, name); }

    public Integer getWeight() { return getInt(KEY_WEIGHT); }

    public void setWeight(int weight) { put(KEY_WEIGHT, weight); }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}
