package com.example.animesocialapp.listManagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.SearchAdapter;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import timber.log.Timber;

public class AddAnimeActivity extends AppCompatActivity {

    public static final String TAG = "AddAnimeActivity";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=";
    public static final String PARAMS = "&order_by=title";
    public static final String PARSE_ANIME_DICT_CODE = "PARSE_ANIME_DICT_CODE";
    public static final String ANIME_DICT_CODE = "ANIME_DICT_CODE";
    private static final int MIN_QUERY_LENGTH = 3;
    private RecyclerView rvAnime;
    private SearchAdapter searchAdapter;
    private SearchView svAnime;
    private ParseAnime parseAnime;
    private HashMap<String, ParseAnime> parseAnimeDict;
    private HashMap<String, Anime> animeDict;
    private List<String> animeIDs;
    private List<Anime> animeList;

    public static Intent createIntent(Context context, HashMap<String, Anime> animeDict, HashMap<String, ParseAnime> parseAnimeDict){
        Intent intent = new Intent(context, AddAnimeActivity.class);
        intent.putExtra(ANIME_DICT_CODE, Parcels.wrap(animeDict));
        intent.putExtra(PARSE_ANIME_DICT_CODE, Parcels.wrap(parseAnimeDict));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbAdd);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.add_anime_title);

        parseAnimeDict = new HashMap<>();
        animeDict = new HashMap<>();
        animeIDs = new ArrayList<>();
        animeList = new ArrayList<>();

        rvAnime = findViewById(R.id.rvAnime);

        animeDict = Parcels.unwrap(getIntent().getParcelableExtra(ANIME_DICT_CODE));
        parseAnimeDict = Parcels.unwrap(getIntent().getParcelableExtra(PARSE_ANIME_DICT_CODE));
        animeList = new ArrayList<Anime>(animeDict.values());
        animeIDs = new ArrayList<String>(animeDict.keySet());

        SearchAdapter.OnClickListener onClickListener = new SearchAdapter.OnClickListener() {
            @Override
            public void onButtonClicked(int position, Drawable background, Integer btn, SearchAdapter.Display display) {
                final Anime anime = searchAdapter.getAnime(position);
                Timber.i("Anime: " + anime);
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
                                saveAnime(anime.getMalID(), anime.getTitle(), anime.getPosterPath(), anime.getSeason(), btn, anime, object, position, display);
                            } else {
                                //unknown error, debug
                                Toast.makeText(AddAnimeActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                                Timber.e(e);
                                return;
                            }
                        } else {
                            handleAnimeList(btn, anime, object, position, display);
                        }

                        Timber.i("Animes Added: " + animeDict);
                        Timber.i("Parse Animes Added: " + parseAnimeDict);
                        Timber.i("Parse Object: " + object);
                    }
                });
            }
        };

        // Create an adapter
        searchAdapter = new SearchAdapter(this, SearchAdapter.PostType.LIST, onClickListener);

        if (animeList != null) {
            searchAdapter.display = SearchAdapter.Display.ADDED;
            searchAdapter.addIDs(animeIDs);
            searchAdapter.addAll(animeList);
        }

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
                    animeList = new ArrayList<Anime>(animeDict.values());
                    searchAdapter.addAll(animeList);
                    return true;
                }

                return false;
            }
        });
    }

    private void handleAnimeList(Integer btn, Anime anime, ParseAnime parseAnime, Integer position, SearchAdapter.Display display) {
        // Handle when when add or remove button is clicked
        if (btn == R.drawable.add_icon) {
            Timber.i("Anime added to dict");
            parseAnimeDict.put(parseAnime.getMalID(), parseAnime);
            animeDict.put(anime.getMalID(), anime);

            animeIDs = new ArrayList<String>(animeDict.keySet());
            searchAdapter.addIDs(animeIDs);
            searchAdapter.notifyItemChanged(position);
        } else {
            Timber.i("Anime removed from dict");

            parseAnimeDict.remove(parseAnime.getMalID());
            animeDict.remove(anime.getMalID());

            animeIDs = new ArrayList<String>(animeDict.keySet());
            searchAdapter.addIDs(animeIDs);

            if (display == SearchAdapter.Display.ADDED){
                searchAdapter.clear();
            } else {
                searchAdapter.notifyItemChanged(position);
            }

            animeList = new ArrayList<Anime>(animeDict.values());
            searchAdapter.addAll(animeList);
        }
    }

    public void queryAnime(String searchQuery) {
        AsyncHttpClient client = new AsyncHttpClient();
        String NOW_PLAYING_URL = REST_URL + searchQuery;

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                if (svAnime.getQuery().length() >= MIN_QUERY_LENGTH) {
                    try {
                        JSONArray results = jsonObject.getJSONArray("results");
                        Timber.i("Results: " + results.toString());
                        //Update Adapter
                        searchAdapter.clear();
                        searchAdapter.addAll(Anime.fromJSONArray(results));

                    } catch (JSONException e) {
                        Timber.e("Hit JSON Exception " + e);
                    }
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Search Query is less than 3 characters");
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
            Intent intent = AddAnimeActivity.createIntent(AddAnimeActivity.this, animeDict, parseAnimeDict);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveAnime(String malID, String title, String posterPath, String season, Integer btn, Anime anime, ParseAnime object, int position, SearchAdapter.Display display) {
        parseAnime = new ParseAnime();
        parseAnime.setMalID(malID);
        parseAnime.setTitle(title);
        parseAnime.setPosterPath(posterPath);
        parseAnime.setSeason(season);
        parseAnime.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Anime save was successful!");
                    Toast.makeText(AddAnimeActivity.this, R.string.save_anime, Toast.LENGTH_SHORT).show();
                    handleAnimeList(btn, anime, parseAnime, position, display);
                } else {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(AddAnimeActivity.this, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}