package com.example.animesocialapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animesocialapp.adapters.PostAdapter;
import com.example.animesocialapp.R;
import com.example.animesocialapp.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        // Create an adapter
        postAdapter = new PostAdapter(view.getContext());

        // Set adapter on the recycler view
        rvPosts.setAdapter(postAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER);
        query.include(Review.KEY_ANIME);
        query.include("createdAt");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting posts: " + e.getLocalizedMessage());
                    if (e.getCode() == ParseException.CONNECTION_FAILED){
                        Timber.i("Network error being handled");
                        //Handle Network Error

                    }
                    return;
                }
                postAdapter.clear();
                postAdapter.addAll(reviews);
                for (Review review : reviews) {
                    Timber.i("Review: " + review.getDescription() + ", username: " + review.getUser().getUsername());
                }
            }
        });

    }
}