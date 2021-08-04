package com.example.animesocialapp.profileManagement;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.animesocialapp.GridSpacingItemDecoration;
import com.example.animesocialapp.ParseRelativeDate;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.example.animesocialapp.listManagment.AnimeList;
import com.example.animesocialapp.listManagment.ListDetailAdapter;
import com.example.animesocialapp.recommendationManagement.Like;
import com.example.animesocialapp.recommendationManagement.RecommendationAdapter;
import com.example.animesocialapp.recommendationManagement.RecommendationManager;
import com.example.animesocialapp.reviewManagement.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikedFragment extends Fragment {

    public static final String TAG = "RecommendationFragment";
    private RecyclerView rvAnime;
    private RecommendationAdapter recommendationAdapter;

    public LikedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAnime = view.findViewById(R.id.rvAnimes);

        // Create an adapter
        recommendationAdapter = new RecommendationAdapter(view.getContext());

        // Set adapter on the recycler view
        rvAnime.setAdapter(recommendationAdapter);

        // Set layout manager on recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvAnime.setLayoutManager(gridLayoutManager);
        rvAnime.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.item_offset), true));

        queryLikedAnime();

    }

    private void queryLikedAnime() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_ANIME);
        query.whereEqualTo("user", currentUser);
        query.findInBackground(new FindCallback<Like>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Like> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Timber.e("Issue getting posts: " + e.getLocalizedMessage());
                    if (e.getCode() == ParseException.CONNECTION_FAILED){
                        Timber.i("Network error being handled");
                        //Handle Network Error

                    }
                    return;
                }

                List<ParseAnime> animeList = new ArrayList<>();
                for (Like like : objects) {
                    ParseAnime anime = (ParseAnime) like.getAnime();
                    animeList.add(anime);
                }

                recommendationAdapter.clear();
                recommendationAdapter.addAll(Anime.fromParseAnime(animeList));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup Manager

        queryLikedAnime();
    }

}