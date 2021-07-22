package com.example.animesocialapp.animeManagment;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.parceler.Parcel;


@ParseClassName("Anime")
public class ParseAnime extends ParseObject {

    public static final String KEY_MAL_ID = "malID";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "posterPath";
    public static final String KEY_SEASON = "season";

    public String getMalID() {
        return getString(KEY_MAL_ID);
    }

    public void setMalID(String malID) {
        put(KEY_MAL_ID, malID);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getPosterPath() {
        return getString(KEY_POSTER_PATH);
    }

    public void setPosterPath(String posterPath) {
        put(KEY_POSTER_PATH, posterPath);
    }

    public String getSeason() {
        return getString(KEY_SEASON);
    }

    public void setSeason(String season) {
        put(KEY_SEASON, season);
    }

}
