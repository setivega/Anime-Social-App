package com.example.animesocialapp.exploreManagement;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.SearchAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */

public class ExploreFragment extends Fragment {

    public static final String TAG = "PostFragment";
    public static final String REST_URL = "https://api.jikan.moe/v3/search/anime?q=";
    public static final String PARAMS = "&order_by=title";
    private static final int MIN_QUERY_LENGTH = 3;
    private RecyclerView rvAnime;
    private SearchAdapter searchAdapter;
    private SearchView svAnime;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAnime = view.findViewById(R.id.rvAnime);

        SearchAdapter.OnClickListener onClickListener = new SearchAdapter.OnClickListener() {
            @Override
            public void onButtonClicked(int position, Drawable background, Integer btn, SearchAdapter.Display display) {
                return;
            }
        };

        // Create an adapter
        searchAdapter = new SearchAdapter(view.getContext(), SearchAdapter.PostType.EXPLORE, onClickListener);

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
}