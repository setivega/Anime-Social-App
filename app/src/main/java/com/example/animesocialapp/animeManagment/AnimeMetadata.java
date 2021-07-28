package com.example.animesocialapp.animeManagment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

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
    public String studio;
    public String rating;
    public String description;
    public Boolean added;

    public AnimeMetadata() {}

    public AnimeMetadata(JSONObject jsonObject) throws JSONException {
        JSONArray studios = jsonObject.getJSONArray("studios");

        malID = String.valueOf(jsonObject.getInt("mal_id"));
        posterPath = jsonObject.getString("image_url");
        title = jsonObject.getString("title");
        rank = String.valueOf(jsonObject.getInt("rank"));
        popularity = String.valueOf(jsonObject.getInt("popularity"));
        genres = Genre.fromJSONArray(jsonObject.getJSONArray("genres"));
        rating = jsonObject.getString("rating");
        description = jsonObject.getString("synopsis").replace("[Written by MAL Rewrite]", "");
        score = jsonObject.getDouble("score");
        if (studios.length() != 0) {
            studio = studios.getJSONObject(0).getString("name");
        }

    }

    public String getMalID() { return malID; }

    public String getPosterPath() { return posterPath; }

    public String getTitle() { return title; }

    public Double getScore() { return score; }

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

    public String getStudio() { return studio; }

    public String getRating() { return rating; }

    public String getDescription() { return description; }

}
