package com.example.animesocialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.models.Anime;
import com.example.animesocialapp.models.ParseAnime;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class AddAnimeActivity extends AppCompatActivity {

    public static final String TAG = "AddAnimeActivity";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=";
    public static final String PARAMS = "&order_by=title";
    private RecyclerView rvAnime;
    private SearchAdapter searchAdapter;
    private SearchView svAnime;
    private ParseAnime parseAnime;
    private List<ParseAnime> parseAnimeList;
    private List<Anime> animeList;
    private List<String> animeIDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbAdd);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        setSupportActionBar(toolbar);

        parseAnimeList = new ArrayList<>();
        animeList = new ArrayList<>();
        animeIDs = new ArrayList<>();

        rvAnime = findViewById(R.id.rvAnime);

        SearchAdapter.OnClickListener onClickListener = new SearchAdapter.OnClickListener() {
            @Override
            public void onButtonClicked(int position, Drawable background, Integer btn, SearchAdapter.Display display) {
                final Anime anime = searchAdapter.getAnime(position);
                Log.i(TAG, "Anime: " + anime);
                // Check Parse to see if the anime in the review exists as an object
                ParseQuery<ParseAnime> query = ParseQuery.getQuery(ParseAnime.class);
                query.include(ParseAnime.KEY_MAL_ID);
                query.whereEqualTo("malID", anime.getMalID());
                query.getFirstInBackground(new GetCallback<ParseAnime>() {
                    @Override
                    public void done(ParseAnime object, ParseException e) {
                        if (e != null) {
                            if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                //object doesn't exist
                                saveAnime(anime.getMalID(), anime.getTitle(), anime.getPosterPath(), anime.getSeason());
                            } else {
                                //unknown error, debug
                            }
                        }

                        if (btn == R.drawable.add_icon) {
                            Log.i(TAG, "Anime added to list");
                            parseAnimeList.add(object);
                            animeList.add(anime);
                            animeIDs.add(anime.getMalID());
                            searchAdapter.addIDs(animeIDs);
                            searchAdapter.notifyItemChanged(position);
                        } else {
                            Log.i(TAG, "Anime removed from list");
                            parseAnimeList.remove(object);
                            animeList.remove(anime);
                            animeIDs.remove(anime.getMalID());
                            searchAdapter.addIDs(animeIDs);
                            if (display == SearchAdapter.Display.ADDED){
                                searchAdapter.clear();
                            } else {
                                searchAdapter.notifyItemChanged(position);
                            }
                            searchAdapter.addAll(animeList);
                        }
                    }
                });
            }
        };

        // Create an adapter
        searchAdapter = new SearchAdapter(this, SearchAdapter.PostType.LIST, onClickListener);

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
        svAnime.setIconifiedByDefault(false);
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
                    searchAdapter.display = SearchAdapter.Display.SEARCH;
                    queryAnime(newText);
                    return true;
                }

                if (newText.isEmpty()) {
                    searchAdapter.clear();
                    searchAdapter.display = SearchAdapter.Display.ADDED;
                    searchAdapter.addAll(animeList);
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


    private void saveAnime(String malID, String title, String posterPath, String season) {
        parseAnime = new ParseAnime();
        parseAnime.setMalID(malID);
        parseAnime.setTitle(title);
        parseAnime.setPosterPath(posterPath);
        parseAnime.setSeason(season);
        parseAnime.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Anime save was successful!");
                    Toast.makeText(AddAnimeActivity.this, "Saved Anime", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error while saving: ", e);
                    Toast.makeText(AddAnimeActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}