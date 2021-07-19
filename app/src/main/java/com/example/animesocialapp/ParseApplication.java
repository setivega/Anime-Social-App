package com.example.animesocialapp;

import android.app.Application;

import com.example.animesocialapp.models.AnimeList;
import com.example.animesocialapp.models.ParseAnime;
import com.example.animesocialapp.models.Review;
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
