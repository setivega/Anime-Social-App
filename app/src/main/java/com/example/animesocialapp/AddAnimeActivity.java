package com.example.animesocialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.models.Anime;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class AddAnimeActivity extends AppCompatActivity {

    public static final String TAG = "PostFragment";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=";
    public static final String PARAMS = "&order_by=title";
    private RecyclerView rvAnime;
    private SearchAdapter searchAdapter;
    private SearchView svAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbAdd);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        setSupportActionBar(toolbar);

        rvAnime = findViewById(R.id.rvAnime);

        // Create an adapter
        searchAdapter = new SearchAdapter(this, SearchAdapter.PostType.LIST);

        // Set adapter on the recycler view
        rvAnime.setAdapter(searchAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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

        svAnime = findViewById(R.id.svAnime);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.done) {

        }
        return super.onOptionsItemSelected(item);
    }
}