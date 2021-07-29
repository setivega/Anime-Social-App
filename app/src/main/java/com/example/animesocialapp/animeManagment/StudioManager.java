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

public class StudioManager {

    public static final String TAG = "StudioManager";
    private static ParseUser currentUser;
    private static Context context;

    public StudioManager(Context context) {
        this.context = context;
    }

    public static void checkStudio(@Nullable String studioID, @Nullable String name, AnimeDetailActivity.LikeState state) {
        currentUser = ParseUser.getCurrentUser();
        // Check Parse to see if the anime in the review exists as an object
        ParseQuery<Studio> query = ParseQuery.getQuery(Studio.class);
        query.include(Studio.KEY_USER);
        query.include(Studio.KEY_STUDIO_ID);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("studioID", studioID);
        query.getFirstInBackground(new GetCallback<Studio>() {
            @Override
            public void done(@Nullable Studio object, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //object doesn't exist
                        // Save Studio
                        saveStudio(studioID, name);
                    } else {
                        //unknown error, debug
                    }
                } else {
                    // Update Studio
                    updateStudio(object, state);
                }
            }
        });
    }

    public static void saveStudio(@Nonnull String studioID, @Nonnull String name) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        Studio studio = new Studio();
        studio.setStudioID(studioID);
        studio.setName(name);
        studio.setUser(currentUser);
        studio.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Studio save was successful!");
                    Toast.makeText(context, R.string.save_studio, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while saving: " + e);
                }
            }
        });
    }

    public static void updateStudio(@Nonnull Studio studio, AnimeDetailActivity.LikeState state) {
        Integer weight = studio.getWeight();
        int newWeight;
        if (state == AnimeDetailActivity.LikeState.LIKED){
            newWeight = ++weight;
        } else {
            newWeight = --weight;
        }
        studio.setWeight(newWeight);

        studio.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Studio update was successful: " + studio.getName());
                    Toast.makeText(context, R.string.update_studio, Toast.LENGTH_SHORT).show();
                    if (newWeight == 0) {
                        studio.deleteInBackground();
                    }
                } else {
                    Timber.e("Error while updating: " + e + studio.getName());
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
