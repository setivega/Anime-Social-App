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

public class YearRangeManager {

    public static final String TAG = "YearRangeManager";
    private ParseUser currentUser;
    private Context context;

    public YearRangeManager(Context context) {
        this.context = context;
    }

    public void checkYearRange(@Nullable String yearRange, AnimeDetailActivity.LikeState state) {
        currentUser = ParseUser.getCurrentUser();
        // Check Parse to see if the anime in the review exists as an object
        ParseQuery<YearRange> query = ParseQuery.getQuery(YearRange.class);
        query.include(YearRange.KEY_USER);
        query.include(YearRange.KEY_YEAR_RANGE);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("yearRange", yearRange);
        query.getFirstInBackground(new GetCallback<YearRange>() {
            @Override
            public void done(@Nullable YearRange object, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //object doesn't exist
                        // Save Year Range
                        saveYearRange(yearRange);
                    } else {
                        //unknown error, debug
                    }
                } else {
                    // Update Year Range
                    updateYearRange(object, state);
                }
            }
        });
    }

    private void saveYearRange(@Nonnull String yearRangeName) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        YearRange yearRange = new YearRange();
        yearRange.setYearRange(yearRangeName);
        yearRange.setUser(currentUser);
        yearRange.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Year Range save was successful!");
                    Toast.makeText(context, R.string.save_year_range, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while saving: " + e);
                }
            }
        });
    }

    private void updateYearRange(@Nonnull YearRange yearRange, AnimeDetailActivity.LikeState state) {
        Integer weight = yearRange.getWeight();
        int newWeight;
        if (state == AnimeDetailActivity.LikeState.LIKED){
            newWeight = ++weight;
        } else {
            newWeight = --weight;
        }
        yearRange.setWeight(newWeight);

        yearRange.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Year Rang update was successful: " + yearRange.getYearRange());
                    Toast.makeText(context, R.string.update_year_range, Toast.LENGTH_SHORT).show();
                    if (newWeight == 0) {
                        yearRange.deleteInBackground();
                    }
                } else {
                    Timber.e("Error while updating: " + e + yearRange.getYearRange());
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
