package com.example.animesocialapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

//        ParseObject.registerSubclass(Review.class);
//        ParseObject.registerSubclass(AnimeList.class);
//        ParseObject.registerSubclass(Anime.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("7Kty7dcVkeNbMd6Fwt8k96egw9JhNYGiPECqzUZn")
                .clientKey("lTzmUfsFbXcvKrTYfMluRdOqkPFUzmo4sMiJBszu")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
