package com.example.animesocialapp.animeManagment;

import android.content.Context;
import android.widget.Toast;

import com.example.animesocialapp.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import timber.log.Timber;

public class RatingManager {

    public static final String TAG = "RatingManager";
    private ParseUser currentUser;
    private Context context;

    public RatingManager(Context context) {
        this.context = context;
    }

    public void checkRating(@Nullable String ratingName, AnimeDetailActivity.LikeState state) {
        currentUser = ParseUser.getCurrentUser();
        // Check Parse to see if the anime in the review exists as an object
        ParseQuery<Rating> query = ParseQuery.getQuery(Rating.class);
        query.include(Rating.KEY_USER);
        query.include(Rating.KEY_RATING);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("rating", ratingName);
        query.getFirstInBackground(new GetCallback<Rating>() {
            @Override
            public void done(@Nullable Rating object, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //object doesn't exist
                        // Save Rating
                        saveRating(ratingName);
                    } else {
                        //unknown error, debug
                    }
                } else {
                    // Update Rating
                    updateRating(object, state);
                }
            }
        });
    }

    private void saveRating(@Nonnull String ratingName) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        Rating rating = new Rating();
        rating.setRating(ratingName);
        rating.setUser(currentUser);
        rating.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Rating save was successful!");
                    Toast.makeText(context, R.string.save_rating, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while saving: " + e);
                }
            }
        });
    }

    private void updateRating(@Nonnull Rating rating, AnimeDetailActivity.LikeState state) {
        Integer weight = rating.getWeight();
        int newWeight;
        if (state == AnimeDetailActivity.LikeState.LIKED){
            newWeight = ++weight;
        } else {
            newWeight = --weight;
        }
        rating.setWeight(newWeight);

        rating.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Rating update was successful: " + rating.getRating());
                    Toast.makeText(context, R.string.update_rating, Toast.LENGTH_SHORT).show();
                    if (newWeight == 0) {
                        rating.deleteInBackground();
                    }
                } else {
                    Timber.e("Error while updating: " + e + rating.getRating());
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
