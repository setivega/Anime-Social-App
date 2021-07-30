package com.example.animesocialapp.recommendationManagement;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.Genre;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.example.animesocialapp.animeManagment.Rating;
import com.example.animesocialapp.animeManagment.Studio;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import timber.log.Timber;

public class RecommendationManager {

    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=&page=1&genre=";
    public static final String PARAMS = "&order_by=members&sort=desc";
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
                        getRecommendations(null, objects.size());
                    } else {
                        //unknown error, debug
                    }
                } else {
                    List<String> genreIDs = new ArrayList<>();
                    for (Genre genre : objects) {
                        genreIDs.add(genre.getGenreID());
                    }

                    String genres = String.join(",", genreIDs);

                    Timber.i(String.valueOf(objects.size()));
                    getRecommendations(genres, objects.size());

                }
            }
        });
    }

    private void getRecommendations(@Nullable String genres, int size) {
        AsyncHttpClient client = new AsyncHttpClient();
        String RECOMMENDATION_URL;
        String resultName;

        // Checking if there are any genres weighted for the user
        if (size < 2) {
            RECOMMENDATION_URL = "https://api.jikan.moe/v3/top/anime";
            resultName = "top";
        } else {
            RECOMMENDATION_URL = REST_URL + genres + PARAMS;
            resultName = "results";
        }

        client.get(RECOMMENDATION_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray(resultName);
                    //Update Adapter
                    adapter.clear();
                    adapter.addAll(sortAnime(Anime.fromJSONArray(results)));
                } catch (JSONException e) {
                    Timber.e("Hit JSON Exception " + e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Something wrong with the query");
            }
        });
    }

    private void getLiked(ParseAnime anime) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_ANIME);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("anime", anime);
        query.findInBackground(new FindCallback<Like>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Like> objects, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //objects don't exist
                        return;
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

                    Timber.i(String.valueOf(animeIDs));

                }
            }
        });
    }

    private void getStudios() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<Studio> query = ParseQuery.getQuery(Studio.class);
        query.include(Studio.KEY_USER);
        query.include(Studio.KEY_STUDIO_ID);
        query.include(Studio.KEY_WEIGHT);
        query.whereEqualTo("user", currentUser);
        query.orderByDescending(Studio.KEY_WEIGHT);
        query.findInBackground(new FindCallback<Studio>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Studio> objects, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //objects don't exist
                        return;
                    } else {
                        //unknown error, debug
                        Timber.e("Unknown Error: " + e);
                    }
                } else {
                    List<String> studioIDs = new ArrayList<>();
                    for (Studio studio : objects) {
                        studioIDs.add(studio.getStudioID());
                    }

                    Timber.i(String.valueOf(studioIDs));

                }
            }
        });
    }

    private void getRatings() {
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
                        return;
                    } else {
                        //unknown error, debug
                        Timber.e("Unknown Error: " + e);
                    }
                } else {
                    List<String> ratings = new ArrayList<>();
                    for (Rating rating : objects) {
                        ratings.add(rating.getRating());
                    }

                    Timber.i(String.valueOf(ratings));

                }
            }
        });

    }


    private List<Anime> sortAnime(List<Anime> animeList) {

        return animeList;
    }


}
