package com.example.animesocialapp.listManagment;

import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("List")
public class AnimeList extends ParseObject {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "user";
    public static final String KEY_ANIME = "anime";
    public int viewType;

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public List<ParseAnime> getAnime() { return (List<ParseAnime>) get(KEY_ANIME); }

    public void setAnime(List<ParseAnime> anime) { put(KEY_ANIME, anime); }

}
