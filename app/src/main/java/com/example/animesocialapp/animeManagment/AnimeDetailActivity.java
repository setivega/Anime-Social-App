package com.example.animesocialapp.animeManagment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.animesocialapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

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
    private Anime anime;

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

        anime = (Anime) Parcels.unwrap(getIntent().getParcelableExtra(Anime.class.getSimpleName()));

        getAnimeMetadata(anime.getMalID());

        tvTitle.setText(anime.getTitle());
        tvSeason.setText(anime.getSeason());
        Timber.i(anime.getDescription());

        Glide.with(this).load(anime.getPosterPath())
                .into(ivBackground);

        Glide.with(this).load(anime.getPosterPath())
                .transform(new CenterCrop(), new RoundedCorners(8))
                .into(ivPoster);



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


}