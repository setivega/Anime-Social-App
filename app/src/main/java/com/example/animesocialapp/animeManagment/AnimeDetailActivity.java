package com.example.animesocialapp.animeManagment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.R;
import com.example.animesocialapp.exploreManagement.Like;
import com.example.animesocialapp.reviewManagement.PostReviewActivity;
import com.example.animesocialapp.reviewManagement.Review;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;
import timber.log.Timber;

public class AnimeDetailActivity extends AppCompatActivity {

    public static final String TAG = "AnimeDetailActivity";
    public static final String REST_URL = "https://api.jikan.moe/v3/anime/";
    private ImageView ivBackground;
    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvSeason;
    private TextView tvScore;
    private TextView tvRank;
    private TextView tvPopularity;
    private TextView tvDescription;
    private ImageButton btnLike;
    private Anime anime;
    private ParseUser currentUser;

    public static Intent createIntent(Context context, Anime anime){
        Intent intent = new Intent(context, AnimeDetailActivity.class);
        intent.putExtra(Anime.class.getSimpleName(), Parcels.wrap(anime));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_detail);

        ivBackground = findViewById(R.id.ivBackground);
        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvSeason = findViewById(R.id.tvSeason);
        tvScore = findViewById(R.id.tvScore);
        tvRank = findViewById(R.id.tvRank);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvDescription = findViewById(R.id.tvDescription);
        btnLike = findViewById(R.id.btnLike);

        anime = (Anime) Parcels.unwrap(getIntent().getParcelableExtra(Anime.class.getSimpleName()));


        getAnimeMetadata(anime.getMalID());

        getAnime(false);


        tvTitle.setText(anime.getTitle());
        tvSeason.setText(anime.getSeason());
        Timber.i(anime.getDescription());

        Glide.with(this).load(anime.getPosterPath())
                .into(ivBackground);

        Glide.with(this).load(anime.getPosterPath())
                .transform(new CenterCrop(), new RoundedCorners(8))
                .into(ivPoster);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnime(true);
            }
        });


    }

    private void getAnimeMetadata(String malID) {
        AsyncHttpClient client = new AsyncHttpClient();
        String NOW_PLAYING_URL = REST_URL + malID;

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Timber.d("onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    //Update Activity
                    Timber.i(String.valueOf(jsonObject.getString("score")));
                    tvScore.setText(jsonObject.getString("score"));
                    tvRank.setText(jsonObject.getString("rank"));
                    tvPopularity.setText(jsonObject.getString("popularity"));
                    tvDescription.setText(jsonObject.getString("synopsis").replace("[Written by MAL Rewrite]", ""));
                } catch (JSONException e) {
                    Timber.e("Hit JSON Exception " + e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Timber.d("Problem getting anime from API");
            }
        });
    }

    private void checkLiked(Boolean likeClicked, ParseAnime parseAnime) {
        currentUser = ParseUser.getCurrentUser();
        // Check Parse to see if the anime in the review exists as an object
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_ANIME);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("anime", parseAnime);
        query.getFirstInBackground(new GetCallback<Like>() {
            @Override
            public void done(Like object, ParseException e) {
                if (e != null) {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        //object doesn't exist
                        if (likeClicked) {
                            // Create Like
                            createLike(parseAnime, currentUser);
                            btnLike.setBackgroundResource(R.drawable.ufi_heart_active);
                        } else {
                            btnLike.setBackgroundResource(R.drawable.ufi_heart);
                        }
                    } else {
                        //unknown error, debug
                    }
                } else {
                    if (likeClicked){
                        // Remove Like
                        object.deleteInBackground();
                        btnLike.setBackgroundResource(R.drawable.ufi_heart);
                    } else {
                        btnLike.setBackgroundResource(R.drawable.ufi_heart_active);
                    }
                }
            }
        });
    }

    private void createLike(ParseAnime anime, ParseUser currentUser) {
        Like like = new Like();
        like.setUser(currentUser);
        like.setAnime(anime);
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Anime save was successful!");
                    Toast.makeText(AnimeDetailActivity.this, R.string.save_like, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(AnimeDetailActivity.this, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAnime(Boolean btnLiked) {
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
                        saveAnime(anime.malID, anime.getTitle(), anime.getPosterPath(), anime.getSeason(), btnLiked);
                    } else {
                        //unknown error, debug
                    }
                } else {
                    checkLiked(btnLiked, object);
                    Timber.i("Made it here");
                }
                Timber.i("Current Parse Anime: " + object);
            }
        });
    }

    private void saveAnime(String malID, String title, String posterPath, String season, Boolean btnLiked) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseAnime parseAnime = new ParseAnime();
        parseAnime.setMalID(malID);
        parseAnime.setTitle(title);
        parseAnime.setPosterPath(posterPath);
        parseAnime.setSeason(season);
        parseAnime.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Anime save was successful!");
                    Toast.makeText(AnimeDetailActivity.this, R.string.save_anime, Toast.LENGTH_SHORT).show();
                    checkLiked(btnLiked, parseAnime);
                } else {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(AnimeDetailActivity.this, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}