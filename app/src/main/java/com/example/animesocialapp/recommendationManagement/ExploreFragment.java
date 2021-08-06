package com.example.animesocialapp.recommendationManagement;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.SearchAdapter;
import com.example.animesocialapp.animeManagment.Studio;
import com.example.animesocialapp.animeManagment.YearRange;
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

public class ExploreFragment extends Fragment {

    public static final String TAG = "PostFragment";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=";
    public static final String TOP_ANIME_URL = "https://api.jikan.moe/v3/search/anime?q=&order_by=score";
    public static final String POPULAR_ANIME_URL = "https://api.jikan.moe/v3/search/anime?q=&order_by=members";
    public static final String CURRENT_SEASON_URL = "https://api.jikan.moe/v3/season";
    public static final String PARAMS = "&order_by=title";
    private static final int MIN_QUERY_LENGTH = 3;
    private RecyclerView rvAnime;
    private ExploreAdapter exploreAdapter;
    private SearchAdapter searchAdapter;
    private SearchView svAnime;
    private List<List<Anime>> exploreLists;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.explore_title);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAnime = view.findViewById(R.id.rvAnime);

        exploreLists = new ArrayList<>();

        SearchAdapter.OnClickListener onClickListener = new SearchAdapter.OnClickListener() {
            @Override
            public void onButtonClicked(int position, Drawable background, Integer btn, SearchAdapter.Display display) {
                return;
            }
        };

        // Create an adapter
        searchAdapter = new SearchAdapter(view.getContext(), SearchAdapter.PostType.EXPLORE, onClickListener);

        exploreAdapter = new ExploreAdapter(view.getContext());

        // Set adapter on the recycler view
        rvAnime.setAdapter(exploreAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvAnime.setLayoutManager(linearLayoutManager);

        svAnime = view.findViewById(R.id.svAnime);
        svAnime.setIconifiedByDefault(false);

        svAnime.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean hasFocus) {
                if (hasFocus) {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                            imm.showSoftInput(view.findFocus(), 0);
                        }
                    }, 0);
                    rvAnime.setAdapter(searchAdapter);

                    rvAnime.setBackgroundColor(getResources().getColor(R.color.background_gray));

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
                } else {
                    rvAnime.setAdapter(exploreAdapter);

                    rvAnime.setBackgroundColor(getResources().getColor(R.color.app_black));

                    rvAnime.removeOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            return;
                        }
                    });

                }
            }
        });

        svAnime.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= MIN_QUERY_LENGTH) {
                    queryAnime(query);
                }
                svAnime.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= MIN_QUERY_LENGTH) {
                    searchAdapter.display = SearchAdapter.Display.SEARCH;
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

        getTopAnime();

    }


    public void getTopAnime() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(TOP_ANIME_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                    try {
                        JSONArray results = jsonObject.getJSONArray("results");
                        Timber.i("Results: " + results.toString());
                        //Update Adapter
                        exploreLists.add(Anime.fromJSONArray(results));
                        getPopularAnime();
                    } catch (JSONException e) {
                        Timber.e("Hit JSON Exception " + e);
                    }
                }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Top Anime " + throwable.getLocalizedMessage());
            }
        });
    }

    public void getPopularAnime() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(POPULAR_ANIME_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Timber.i("Results: " + results.toString());
                    //Update Adapter
                    exploreLists.add(Anime.fromJSONArray(results));
                    getCurrentSeason();
                } catch (JSONException e) {
                    Timber.e("Hit JSON Exception " + e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Popular Anime " + throwable.getLocalizedMessage());
            }
        });
    }

    public void getCurrentSeason() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(CURRENT_SEASON_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("anime");
                    Timber.i("Results: " + results.toString());
                    //Update Adapter

                    exploreLists.add(Anime.fromJSONSeason(results, jsonObject));
                    exploreAdapter.clear();
                    exploreAdapter.addAll(exploreLists);
                } catch (JSONException e) {
                    Timber.e("Hit JSON Exception " + e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Current Anime " + throwable.getLocalizedMessage());
            }
        });
    }

    public void queryAnime(String searchQuery) {
        AsyncHttpClient client = new AsyncHttpClient();
        String SEARCH_URL = REST_URL + searchQuery;

        client.get(SEARCH_URL, new JsonHttpResponseHandler() {
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
}