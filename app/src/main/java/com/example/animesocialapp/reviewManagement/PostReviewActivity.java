package com.example.animesocialapp.reviewManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.animesocialapp.mainManagement.MainActivity;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import timber.log.Timber;

public class PostReviewActivity extends AppCompatActivity {

    public static final String TAG = "PostReviewActivity";
    private Anime anime;
    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvSeason;
    private EditText etReview;
    private ParseAnime parseAnime;

    public static Intent createIntent(Context context, Anime anime){
        Intent intent = new Intent(context, PostReviewActivity.class);
        intent.putExtra(Anime.class.getSimpleName(), Parcels.wrap(anime));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbList);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.post_review_title);

        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvSeason = findViewById(R.id.tvSeason);
        etReview = findViewById(R.id.etDescription);

        anime = (Anime) Parcels.unwrap(getIntent().getParcelableExtra(Anime.class.getSimpleName()));

        tvTitle.setText(anime.getTitle());
        tvSeason.setText(anime.getSeason());

        Glide.with(this).load(anime.getPosterPath())
                .transform(new CenterCrop(), new RoundedCorners(4))
                .into(ivPoster);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.post) {
            final String review = etReview.getText().toString();
            ParseUser currentUser = ParseUser.getCurrentUser();
            // Check if edit text contains text
            if (review.isEmpty()) {
                Toast.makeText(PostReviewActivity.this, R.string.empty_review, Toast.LENGTH_SHORT).show();
            } else {
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
                                saveAnime(anime.getMalID(), anime.getTitle(), anime.getPosterPath(), anime.getSeason());
                            } else {
                                //unknown error, debug
                            }
                        } else {
                            saveReview(review, object, currentUser);
                        }
                        Timber.i("Current Parse Anime: " + object);
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveReview(String review, ParseAnime anime, ParseUser currentUser) {
        Review newReview = new Review();
        newReview.setDescription(review);
        newReview.setUser(currentUser);
        newReview.setAnime(anime);
        newReview.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(PostReviewActivity.this, R.string.save_error, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.i("Review save was successful!");
                    Toast.makeText(PostReviewActivity.this, R.string.save_review, Toast.LENGTH_SHORT).show();
                    goMainActivity();
                }
            }
        });
    }

    private void saveAnime(String malID, String title, String posterPath, String season) {
        final String review = etReview.getText().toString();
        ParseUser currentUser = ParseUser.getCurrentUser();
        parseAnime = new ParseAnime();
        parseAnime.setMalID(malID);
        parseAnime.setTitle(title);
        parseAnime.setPosterPath(posterPath);
        parseAnime.setSeason(season);
        parseAnime.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.i("Anime save was successful!");
                    Toast.makeText(PostReviewActivity.this, R.string.save_anime, Toast.LENGTH_SHORT).show();
                    saveReview(review, parseAnime, currentUser);
                } else {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(PostReviewActivity.this, R.string.save_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}