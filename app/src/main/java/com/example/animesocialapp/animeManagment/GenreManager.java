package com.example.animesocialapp.animeManagment;

import android.content.Context;
import android.widget.Toast;

import com.example.animesocialapp.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import timber.log.Timber;

public class GenreManager {

    public static final String TAG = "GenreManager";
    private static ParseUser currentUser;
    private static Context context;

    public GenreManager(Context context) {
        this.context = context;
    }

    public static void checkGenre(String genreID, String name, AnimeDetailActivity.LikeState state) {
        currentUser = ParseUser.getCurrentUser();
        // Check Parse to see if the anime in the review exists as an object
        ParseQuery<Genre> query = ParseQuery.getQuery(Genre.class);
        query.include(Genre.KEY_USER);
        query.include(Genre.KEY_GENRE_ID);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("genreID", genreID);
        query.getFirstInBackground(new GetCallback<Genre>() {
            @Override
            public void done(Genre object, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //object doesn't exist
                        // Save Genre
                        saveGenre(genreID, name);
                    } else {
                        //unknown error, debug
                    }
                } else {
                    // Update Genre
                    updateGenre(object, state);
                }
            }
        });
    }

    public static void saveGenre(String genreID, String name) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        Genre genre = new Genre();
        genre.setGenreID(genreID);
        genre.setName(name);
        genre.setUser(currentUser);
        genre.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Genre save was successful!");
                    Toast.makeText(context, R.string.save_genre, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void updateGenre(Genre genre, AnimeDetailActivity.LikeState state) {
        Integer weight = genre.getWeight();
        if (state == AnimeDetailActivity.LikeState.LIKED){
            genre.setWeight(++weight);
        } else {
            int newWeight = --weight;
            genre.setWeight(newWeight);
            if (newWeight == 0) {
                genre.deleteInBackground();
            }
        }
        genre.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Genre update was successful!");
                    Toast.makeText(context, R.string.save_genre, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while updating: " + e);
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
