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
    private ParseUser currentUser;
    private Context context;

    public GenreManager(Context context) {
        this.context = context;
    }

    public void checkGenre(String genreID, String name, AnimeDetailActivity.LikeState state) {
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

    private void saveGenre(String genreID, String name) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        Genre genre = new Genre();
        genre.setGenreID(genreID);
        genre.setName(name);
        genre.setUser(currentUser);
        genre.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Genre save was successful: " + genre.getName());
                    Toast.makeText(context, R.string.save_genre, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateGenre(Genre genre, AnimeDetailActivity.LikeState state) {
        Integer weight = genre.getWeight();
        int newWeight;
        if (state == AnimeDetailActivity.LikeState.LIKED){
            newWeight = ++weight;
        } else {
            newWeight = --weight;
        }
        genre.setWeight(newWeight);

        genre.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Genre update was successful: " + genre.getName());
                    Toast.makeText(context, R.string.update_genre, Toast.LENGTH_SHORT).show();
                    if (newWeight == 0) {
                        genre.deleteInBackground();
                    }
                } else {
                    Timber.e("Error while updating: " + e + genre.getName());
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
