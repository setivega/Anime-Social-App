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
import com.parse.FindCallback;
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
    private RecyclerView rvAnimes;
    private RecommendationAdapter recommendationAdapter;
    private RecommendationManager recommendationManager;

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

        // Setup Manager
        recommendationManager = new RecommendationManager(getContext(), recommendationAdapter);

        // Set adapter on the recycler view
        rvAnimes.setAdapter(recommendationAdapter);

        // Set layout manager on recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvAnimes.setLayoutManager(gridLayoutManager);
        rvAnimes.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.item_offset), true));

        recommendationManager.getGenres();

    }


}