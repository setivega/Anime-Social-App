package com.example.animesocialapp.models;

import com.example.animesocialapp.ParseRelativeDate;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Anime {

    String malID;
    String posterPath;
    String title;
    String description;
    Double score;
    String startDate;

    public Anime() {}

    public Anime(JSONObject jsonObject) throws JSONException {
        malID = String.valueOf(jsonObject.getInt("mal_id"));
        posterPath = jsonObject.getString("image_url");
        title = jsonObject.getString("title");
        description = jsonObject.getString("synopsis");
        score = jsonObject.getDouble("score");
        startDate = jsonObject.getString("start_date");
    }

    public static List<Anime> fromJSONArray(JSONArray animeJsonArray) throws JSONException {
        List<Anime> animes = new ArrayList<>();
        for (int i = 0; i < animeJsonArray.length(); i++) {
            animes.add(new Anime(animeJsonArray.getJSONObject(i)));
        }
        return animes;
    }

    public String getMalID() { return malID; }

    public String getPosterPath() { return posterPath; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public Double getScore() { return score; }

    public String getSeason() { return ParseRelativeDate.getRelativeSeasonYear(startDate); }

}
