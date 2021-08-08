package com.example.animesocialapp.reviewManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.AnimeDetailFragment;
import com.example.animesocialapp.animeManagment.AnimeMetadata;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsDetailFragment extends Fragment {

    private AnimeMetadata animeMetadata;
    private RecyclerView rvReviews;
    private ReviewDetailAdapter reviewAdapter;

    public ReviewsDetailFragment() {
        // Required empty public constructor
    }

    public static ReviewsDetailFragment newInstance(AnimeMetadata animeMetadata) {
        ReviewsDetailFragment fragment = new ReviewsDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(AnimeMetadata.class.getSimpleName(), Parcels.wrap(animeMetadata));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            animeMetadata = Parcels.unwrap(getArguments().getParcelable(AnimeMetadata.class.getSimpleName()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvReviews = view.findViewById(R.id.rvReviews);

        // Create an adapter
        reviewAdapter = new ReviewDetailAdapter(view.getContext());

        // Set adapter on the recycler view
        rvReviews.setAdapter(reviewAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(linearLayoutManager);

        getAnime();
    }


    private void getAnime(){
        ParseQuery<ParseAnime> query = ParseQuery.getQuery(ParseAnime.class);
        query.include(ParseAnime.KEY_MAL_ID);
        query.whereEqualTo("malID", animeMetadata.getMalID());
        query.getFirstInBackground(new GetCallback<ParseAnime>() {
            @Override
            public void done(ParseAnime object, ParseException e) {
                queryReviews(object);
            }
        });
    }

    private void queryReviews(ParseAnime parseAnime) {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER);
        query.include(Review.KEY_ANIME);
        query.include("createdAt");
        query.orderByDescending("createdAt");
        query.whereEqualTo("anime", parseAnime);
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
                    review.getAnime().getString("malID");
                }
            }
        });
    }
}