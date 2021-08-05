package com.example.animesocialapp.reviewManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.AnimeDetailActivity;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    public static final String TAG = "ReviewsFragment";
    private RecyclerView rvReviews;
    private ReviewAdapter reviewAdapter;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvReviews = view.findViewById(R.id.rvReviews);

        ReviewAdapter.OnLongClickListener longClickListener = new ReviewAdapter.OnLongClickListener() {
            @Override
            public void onPosterPressed(int position) {
                final Review review = reviewAdapter.getReview(position);
                ParseAnime parseAnime = (ParseAnime) review.getAnime();

                Anime anime = new Anime(parseAnime);
                startActivity(AnimeDetailActivity.createIntent(getContext(), anime,0));
            }
        };

        // Create an adapter
        reviewAdapter = new ReviewAdapter(view.getContext(), longClickListener);

        // Set adapter on the recycler view
        rvReviews.setAdapter(reviewAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(linearLayoutManager);

        queryReviews();
    }

    private void queryReviews() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER);
        query.include(Review.KEY_ANIME);
        query.include("createdAt");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Timber.e("Issue getting posts: " + e.getLocalizedMessage());
                    if (e.getCode() == ParseException.CONNECTION_FAILED){
                        Timber.i("Network error being handled");
                        //Handle Network Error

                    }
                    return;
                }
                reviewAdapter.clear();
                reviewAdapter.addAll(reviews);
                for (Review review : reviews) {
                    Timber.i("Review: " + review.getDescription() + ", username: " + review.getUser().getUsername());
                }
            }
        });

    }

}