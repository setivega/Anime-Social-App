package com.example.animesocialapp.animeManagment;

import com.example.animesocialapp.ParseRelativeDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Anime {

    public String malID;
    public String posterPath;
    public String title;
    public Double score;
    public String startDate;
    public Boolean added;

    public Anime() {}

    public Anime(JSONObject jsonObject) throws JSONException {
        malID = String.valueOf(jsonObject.getInt("mal_id"));
        posterPath = jsonObject.getString("image_url");
        title = jsonObject.getString("title");
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

    public Double getScore() { return score; }

    public String getSeason() { return ParseRelativeDate.getRelativeSeasonYear(startDate); }

    public Boolean getState() { return added; }

}
