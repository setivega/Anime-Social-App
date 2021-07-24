package com.example.animesocialapp.exploreManagement;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {

    public static final String KEY_ANIME = "anime";
    public static final String KEY_USER = "user";

    public ParseObject getAnime() {
        return getParseObject(KEY_ANIME);
    }

    public void setAnime(ParseObject anime) {
        put(KEY_ANIME, anime);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}