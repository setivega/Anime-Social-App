package com.example.animesocialapp;

import android.app.Application;

import com.example.animesocialapp.animeManagment.Genre;
import com.example.animesocialapp.animeManagment.Rating;
import com.example.animesocialapp.animeManagment.Studio;
import com.example.animesocialapp.animeManagment.YearRange;
import com.example.animesocialapp.recommendationManagement.Like;
import com.example.animesocialapp.listManagment.AnimeList;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.example.animesocialapp.reviewManagement.Review;
import com.parse.Parse;
import com.parse.ParseObject;

import timber.log.Timber;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Review.class);
        ParseObject.registerSubclass(AnimeList.class);
        ParseObject.registerSubclass(ParseAnime.class);
        ParseObject.registerSubclass(Like.class);
        ParseObject.registerSubclass(Genre.class);
        ParseObject.registerSubclass(Studio.class);
        ParseObject.registerSubclass(Rating.class);
        ParseObject.registerSubclass(YearRange.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("7Kty7dcVkeNbMd6Fwt8k96egw9JhNYGiPECqzUZn")
                .clientKey("lTzmUfsFbXcvKrTYfMluRdOqkPFUzmo4sMiJBszu")
                .server("https://parseapi.back4app.com")
                .build()
        );

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
