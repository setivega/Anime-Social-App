package com.example.animesocialapp.animeManagment;

import android.content.Context;

import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.ParseRelativeDate;
import com.example.animesocialapp.recommendationManagement.RecommendationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import okhttp3.Headers;
import timber.log.Timber;

@Parcel
public class Anime {

    public String malID;
    public String posterPath;
    public String title;
    public Double score;
    public String startDate;
    public String rating;
    public String yearRange;
    public Boolean added;
    public Integer recScore;
    public String season;

    public Anime() {}

    public Anime(JSONObject jsonObject, @Nullable String seasonName, @Nullable Integer seasonYear) throws JSONException {
        malID = String.valueOf(jsonObject.getInt("mal_id"));
        posterPath = jsonObject.getString("image_url");
        title = jsonObject.getString("title");
        if (jsonObject.has("score") && !jsonObject.isNull("score")) {
            score = jsonObject.getDouble("score");
        }

        if (jsonObject.has("start_date")) {
            startDate = jsonObject.getString("start_date");
            season = ParseRelativeDate.getRelativeSeasonYear(startDate);
        } else {
            season = seasonName + seasonYear;
        }

        if (jsonObject.has("rated")){
            rating = jsonObject.getString("rated");
        }



    }

    public Anime(ParseAnime parseAnime) {
        malID = parseAnime.getMalID();
        posterPath = parseAnime.getPosterPath();
        title = parseAnime.getTitle();
        season = parseAnime.getSeason();
    }

    public static List<Anime> fromJSONArray(JSONArray animeJsonArray) throws JSONException {
        List<Anime> animes = new ArrayList<>();
        for (int i = 0; i < animeJsonArray.length(); i++) {
            animes.add(new Anime(animeJsonArray.getJSONObject(i), null, null));
        }
        return animes;
    }

    public static List<Anime> fromJSONSeason(JSONArray animeJsonArray, JSONObject jsonObject) throws JSONException {
        List<Anime> animes = new ArrayList<>();
        String seasonName = jsonObject.getString("season_name");
        Integer seasonYear = jsonObject.getInt("season_year");

        for (int i = 0; i < animeJsonArray.length(); i++) {
            animes.add(new Anime(animeJsonArray.getJSONObject(i), seasonName, seasonYear));
        }
        return animes;
    }

    public static List<Anime> fromParseAnime(List<ParseAnime> parseAnimeList) {
        List<Anime> animeList = new ArrayList<>();
        for (int i = 0; i < parseAnimeList.size(); i++) {
            animeList.add(new Anime(parseAnimeList.get(i)));
        }
        return animeList;
    }

    public String getMalID() { return malID; }

    public String getPosterPath() { return posterPath; }

    public String getTitle() { return title; }

    public Double getScore() { return score; }

    public String getSeason() { return season; }

    public String getRating() { return rating; }

    public String getYearRange() { return ParseRelativeDate.getYearRange(startDate); }

    public Boolean getState() { return added; }

    public Integer getRecScore() { return recScore; }

}
