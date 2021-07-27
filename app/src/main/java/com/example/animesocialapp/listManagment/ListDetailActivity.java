package com.example.animesocialapp.listManagment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.animesocialapp.GridSpacingItemDecoration;
import com.example.animesocialapp.ParseRelativeDate;
import com.example.animesocialapp.R;
import com.example.animesocialapp.reviewManagement.Review;
import com.example.animesocialapp.reviewManagement.ReviewDetailActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import timber.log.Timber;

public class ListDetailActivity extends AppCompatActivity {

    public static final String TAG = "ListDetailActivity";
    public static final String PROFILE_IMAGE = "profileImage";
    private TextView tvTitle;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvCreatedAt;
    private TextView tvDescription;
    private RecyclerView rvAnimes;
    private AnimeList animeList;

    public static Intent createIntent(Context context, AnimeList animeList){
        Intent intent = new Intent(context, ListDetailActivity.class);
        intent.putExtra(AnimeList.class.getSimpleName(), Parcels.wrap(animeList));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        tvTitle = findViewById(R.id.tvTitle);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvDescription = findViewById(R.id.tvDescription);
        rvAnimes = findViewById(R.id.rvAnimes);

        animeList = (AnimeList) Parcels.unwrap(getIntent().getParcelableExtra(AnimeList.class.getSimpleName()));

        ParseUser user = animeList.getUser();

        tvTitle.setText(animeList.getTitle());
        tvUsername.setText(user.getUsername());
        tvCreatedAt.setText(ParseRelativeDate.getRelativeTimeAgo(animeList.getCreatedAt().toString()));

        String description = animeList.getDescription();
        if (description!= null && !description.trim().isEmpty()){
            tvDescription.setText(description);
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        // Create an adapter
        ListDetailAdapter detailAdapter = new ListDetailAdapter(this);

        // Set adapter on the recycler view
        rvAnimes.setAdapter(detailAdapter);

        // Set layout manager on recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvAnimes.setLayoutManager(gridLayoutManager);
        rvAnimes.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.item_offset), true));

        if (animeList.getAnime() != null) {
            detailAdapter.addAll(animeList.getAnime());
        }

        ParseFile profileImage = (ParseFile) user.get(PROFILE_IMAGE);
        Glide.with(this).load(profileImage.getUrl())
                .transform(new CircleCrop())
                .into(ivProfileImage);

    }
}