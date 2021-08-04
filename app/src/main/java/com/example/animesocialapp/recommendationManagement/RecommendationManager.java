package com.example.animesocialapp.recommendationManagement;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.AnimeMetadata;
import com.example.animesocialapp.animeManagment.Genre;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.example.animesocialapp.animeManagment.Rating;
import com.example.animesocialapp.animeManagment.Studio;
import com.example.animesocialapp.animeManagment.YearRange;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;
import timber.log.Timber;

public class RecommendationManager {

    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=&page=1&genre=";
    private Context context;
    private RecommendationAdapter adapter;

    public RecommendationManager(Context context, RecommendationAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    public void getGenres() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<Genre> query = ParseQuery.getQuery(Genre.class);
        query.include(Genre.KEY_USER);
        query.include(Genre.KEY_GENRE_ID);
        query.include(Genre.KEY_WEIGHT);
        query.whereEqualTo("user", currentUser);
        query.orderByDescending(Genre.KEY_WEIGHT);
        query.setLimit(2);
        query.findInBackground(new FindCallback<Genre>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Genre> objects, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //objects don't exist
                        getYearRanges(null, objects.size());
                    } else {
                        //unknown error, debug
                    }
                } else {
                    List<String> genreIDs = new ArrayList<>();
                    for (Genre genre : objects) {
                        genreIDs.add(genre.getGenreID());
                    }

                    String genres = String.join(",", genreIDs);

                    Timber.i("Genres: " + genres);

                    getYearRanges(genres, objects.size());

                }
            }
        });
    }

    private void getYearRanges(String genres, int size) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<YearRange> query = ParseQuery.getQuery(YearRange.class);
        query.include(YearRange.KEY_USER);
        query.include(YearRange.KEY_YEAR_RANGE);
        query.include(YearRange.KEY_WEIGHT);
        query.whereEqualTo("user", currentUser);
        query.orderByDescending(YearRange.KEY_WEIGHT);
        query.findInBackground(new FindCallback<YearRange>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<YearRange> objects, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //objects don't exist
                        getRatings(genres, null, size);
                    } else {
                        //unknown error, debug
                        Timber.e("Unknown Error: " + e);
                    }
                } else {
                    List<String> yearRanges = new ArrayList<>();
                    for (YearRange yearRange : objects) {
                        yearRanges.add(yearRange.getYearRange());
                    }

                    Timber.i("Year Ranges: " + String.valueOf(yearRanges));

                    getRatings(genres, objects, size);

                }
            }
        });
    }

    private void getRatings(String genres, List<YearRange> yearRanges, int size) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<Rating> query = ParseQuery.getQuery(Rating.class);
        query.include(Rating.KEY_USER);
        query.include(Rating.KEY_RATING);
        query.include(Rating.KEY_WEIGHT);
        query.whereEqualTo("user", currentUser);
        query.orderByDescending(Rating.KEY_WEIGHT);
        query.findInBackground(new FindCallback<Rating>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Rating> objects, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //objects don't exist
                        getLiked(genres, yearRanges, null, size);
                    } else {
                        //unknown error, debug
                        Timber.e("Unknown Error: " + e);
                    }
                } else {
                    List<String> ratings = new ArrayList<>();
                    for (Rating rating : objects) {
                        ratings.add(rating.getRating());
                    }

                    getLiked(genres, yearRanges, objects, size);

                    Timber.i("Ratings: " + String.valueOf(ratings));

                }
            }
        });

    }

    private void getLiked(String genres, List<YearRange> yearRanges, List<Rating> ratings, int size) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_ANIME);
        query.whereEqualTo("user", currentUser);
        query.findInBackground(new FindCallback<Like>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Like> objects, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //objects don't exist
                        getRecommendations(genres, yearRanges, ratings, null, size);
                    } else {
                        //unknown error, debug
                        Timber.e("Unknown Error: " + e);
                    }
                } else {
                    List<String> animeIDs = new ArrayList<>();
                    for (Like like : objects) {
                        ParseAnime anime = (ParseAnime) like.getAnime();
                        animeIDs.add(anime.getMalID());
                    }

                    getRecommendations(genres, yearRanges, ratings, animeIDs, size);

                    Timber.i("Anime IDs: " + String.valueOf(animeIDs));

                }
            }
        });
    }

    private void getRecommendations(@Nullable String genres, List<YearRange> yearRanges, List<Rating> ratings, List<String> animeIDs, int size) {
        AsyncHttpClient client = new AsyncHttpClient();
        String RECOMMENDATION_URL;
        String resultName;

        // Checking if there are any genres weighted for the user
        if (size < 2) {
            RECOMMENDATION_URL = "https://api.jikan.moe/v3/top/anime";
            resultName = "top";
        } else {
            RECOMMENDATION_URL = REST_URL + genres;
            resultName = "results";
        }

        client.get(RECOMMENDATION_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray(resultName);
//                    Timber.i(String.valueOf(results));
                    //Update Adapter
                    adapter.clear();
                    if (size < 2) {
                        adapter.addAll(Anime.fromJSONArray(results));
                    } else {
                        adapter.addAll(sortAnime(Anime.fromJSONArray(results), yearRanges, ratings, animeIDs));
                    }
                } catch (JSONException e) {
                    Timber.e("Hit JSON Exception " + e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Something wrong with the query");
                Timber.d(throwable.getLocalizedMessage());
            }
        });
    }



    private List<Anime> sortAnime(List<Anime> animeList, List<YearRange> yearRanges, List<Rating> ratings, List<String> animeIDs) {

        List<Anime> likedAnime = new ArrayList<Anime>();
        for (Anime anime : animeList) {
            if (animeIDs.contains(anime.getMalID())) {
                likedAnime.add(anime);
            } else {
                int score = 0;

                // Set Score for rating
                for (Rating rating : ratings){
                    if (rating.getRating().equals(anime.getRating())) {
                        score = 5 * rating.getWeight();
                    }
                }

                for (YearRange yearRange : yearRanges) {
                    if (yearRange.getYearRange().equals(anime.getYearRange())){
                        score = score + (3 * yearRange.getWeight());
                    }
                }

                anime.recScore = score;
            }
        }

        // Remove liked anime from recommendations
        animeList.removeAll(likedAnime);

        // Sort new list
        int listSize = animeList.size();
        QuickSort animeSort = new QuickSort(animeList, listSize);

        animeSort.quickSort(0, listSize-1);

        List<String> animeTitles = new ArrayList<>();
        List<Integer> animeScores = new ArrayList<>();

        for (Anime anime : animeList) {
            animeTitles.add(anime.getTitle());
            animeScores.add(anime.getRecScore());
        }
        Timber.i("Sorted Titles: " + String.valueOf(animeTitles));
        Timber.i("Sorted Scores: " + String.valueOf(animeScores));

        return animeList;
    }





}
