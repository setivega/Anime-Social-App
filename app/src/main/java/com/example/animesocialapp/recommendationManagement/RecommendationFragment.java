package com.example.animesocialapp.recommendationManagement;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.GridSpacingItemDecoration;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.Genre;
import com.example.animesocialapp.reviewManagement.ReviewAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    public static final String TAG = "RecommendationFragment";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=&page=1&genre=";
    public static final String PARAMS = "&order_by=members&sort=desc";
    private RecyclerView rvAnimes;
    private RecommendationAdapter recommendationAdapter;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recommendations");
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAnimes = view.findViewById(R.id.rvAnimes);

        // Create an adapter
        recommendationAdapter = new RecommendationAdapter(view.getContext());

        // Set adapter on the recycler view
        rvAnimes.setAdapter(recommendationAdapter);

        // Set layout manager on recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvAnimes.setLayoutManager(gridLayoutManager);
        rvAnimes.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.item_offset), true));

        getGenres();

    }

    private void getGenres() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Check Parse to see if the anime in the review exists as an object
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
//                    Timber.i("Results: " + results.toString());
                    //Update Adapter
                    recommendationAdapter.clear();
                    recommendationAdapter.addAll(Anime.fromJSONArray(results));
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
}