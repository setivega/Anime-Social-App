package com.example.animesocialapp.reviewManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.animesocialapp.ParseRelativeDate;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class ReviewDetailActivity extends AppCompatActivity {

    public static final String TAG = "ReviewDetailActivity";
    public static final String PROFILE_IMAGE = "profileImage";
    private ImageView ivBackground;
    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvSeason;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvCreatedAt;
    private TextView tvReview;
    private Review review;

    public static Intent createIntent(Context context, Review review){
        Intent intent = new Intent(context, ReviewDetailActivity.class);
        intent.putExtra(Review.class.getSimpleName(), Parcels.wrap(review));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        ivBackground = findViewById(R.id.ivBackground);
        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvSeason = findViewById(R.id.tvSeason);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvReview = findViewById(R.id.tvDescription);

        review = (Review) Parcels.unwrap(getIntent().getParcelableExtra(Review.class.getSimpleName()));

        ParseAnime anime = (ParseAnime) review.getAnime();
        ParseUser user = review.getUser();

        tvTitle.setText(anime.getTitle());
        tvSeason.setText(anime.getSeason());
        tvUsername.setText(user.getUsername());
        tvCreatedAt.setText(ParseRelativeDate.getRelativeTimeAgo(review.getCreatedAt().toString()));
        tvReview.setText(review.getDescription());

        Glide.with(this).load(anime.getPosterPath())
                .into(ivBackground);

        Glide.with(this).load(anime.getPosterPath())
                .transform(new CenterCrop(), new RoundedCorners(8))
                .into(ivPoster);

        ParseFile profileImage = (ParseFile) user.get(PROFILE_IMAGE);
        Glide.with(this).load(profileImage.getUrl())
                .transform(new CircleCrop())
                .into(ivProfileImage);

    }
}