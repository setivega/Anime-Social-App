package com.example.animesocialapp.animeManagment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

import javax.annotation.Nullable;

import timber.log.Timber;

@Parcel
public class AnimeMetadata {

    public String malID;
    public String posterPath;
    public String title;
    public Double score;
    public String rank;
    public String popularity;
    public List<Genre> genres;
    public List<Studio> studios;
    public YearRange yearRange;
    public Rating rating;
    public String description;
    public Boolean added;

    public AnimeMetadata() {}

    public AnimeMetadata(JSONObject jsonObject) throws JSONException {
        malID = String.valueOf(jsonObject.getInt("mal_id"));
        posterPath = jsonObject.getString("image_url");
        title = jsonObject.getString("title");
        rank = String.valueOf(jsonObject.getInt("rank"));
        popularity = String.valueOf(jsonObject.getInt("popularity"));
        genres = Genre.fromJSONArray(jsonObject.getJSONArray("genres"));
        studios = Studio.fromJSONArray(jsonObject.getJSONArray("studios"));
        yearRange = new YearRange(jsonObject);
        rating = new Rating(jsonObject);
        description = jsonObject.getString("synopsis").replace("[Written by MAL Rewrite]", "");
        score = jsonObject.getDouble("score");

    }

    public String getMalID() { return malID; }

    public String getPosterPath() { return posterPath; }

    public String getTitle() { return title; }

    public String getScore() {
        if (score != null) {
            return String.valueOf(score);
        }
        return "N/A";
    }

    public String getRank() { return rank; }

    public String getPopularity() { return popularity; }

    public String getGenres() { if (genres.size() == 1) {
            Timber.i(String.valueOf(genres.size()));
            return genres.get(0).name;
        } else {
            Timber.i(String.valueOf(genres.size()));
            return genres.get(0).name + " & " + genres.get(1).name;
        }
    }

//    public String getStudio() { return studio; }

    public YearRange getYearRange() { return yearRange; }

    public Rating getRating() { return rating; }

    public String getDescription() { return description; }

}
