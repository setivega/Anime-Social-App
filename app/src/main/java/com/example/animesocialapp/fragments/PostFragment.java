package com.example.animesocialapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.R;
import com.example.animesocialapp.SearchAdapter;
import com.example.animesocialapp.models.Anime;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    public static final String TAG = "PostFragment";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=";
    public static final String PARAMS = "&order_by=title";
    private RecyclerView rvAnime;
    private SearchAdapter searchAdapter;
    private SearchView svAnime;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAnime = view.findViewById(R.id.rvAnime);

        // Create an adapter
        searchAdapter = new SearchAdapter(view.getContext(), SearchAdapter.PostType.REVIEW);

        // Set adapter on the recycler view
        rvAnime.setAdapter(searchAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvAnime.setLayoutManager(linearLayoutManager);

        rvAnime.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        svAnime.clearFocus();
                    default:
                        break;

                }
            }
        });

        svAnime = view.findViewById(R.id.svAnime);
        svAnime.requestFocus();

        svAnime.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 3) {
                    queryAnime(query);
                }
                svAnime.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    queryAnime(newText);
                    return true;
                }

                if (newText.isEmpty()) {
                    searchAdapter.clear();
                    return true;
                }

                return false;
            }
        });

    }

    public void queryAnime(String searchQuery) {
        AsyncHttpClient client = new AsyncHttpClient();
        String NOW_PLAYING_URL = REST_URL + searchQuery;

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                if (svAnime.getQuery().length() >= 3) {
                    try {
                        JSONArray results = jsonObject.getJSONArray("results");
                        Log.i(TAG, "Results: " + results.toString());
                        //Update Adapter
                        searchAdapter.clear();
                        searchAdapter.addAll(Anime.fromJSONArray(results));
                    } catch (JSONException e) {
                        Log.e(TAG, "Hit JSON Exception ", e);
                    }
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "Search Query is less than 3 characters");
            }
        });
    }

}