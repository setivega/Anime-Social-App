package com.example.animesocialapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_REVIEW = "review";
    public static final String KEY_ANIME = "anime";
    public static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_REVIEW);
    }

    public void setDescription(String review) {
        put(KEY_REVIEW, review);
    }

    public ParseObject getImage() {
        return getParseObject(KEY_ANIME);
    }

    public void setImage(ParseFile anime) {
        put(KEY_ANIME, anime);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}
