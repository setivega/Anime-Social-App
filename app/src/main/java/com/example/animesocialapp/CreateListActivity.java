package com.example.animesocialapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animesocialapp.models.Anime;
import com.example.animesocialapp.models.ParseAnime;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {

    public static final String TAG = "CreateListActivity";
    private static final int ANIME_ACTIVITY_REQUEST_CODE = 42;
    public static final String PARSE_ANIME_DICT_CODE = "PARSE_ANIME_DICT_CODE";
    public static final String ANIME_DICT_CODE = "ANIME_DICT_CODE";
    private EditText etTitle;
    private EditText etDescription;
    private RecyclerView rvAnimePreviews;
    private Button btnAddAnime;
    private HashMap<String, ParseAnime> parseAnimeDict;
    private HashMap<String, Anime> animeDict;
    private List<Anime> animeList;
    private List<ParseAnime> parseAnimeList;
    private AnimePreviewAdapter previewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        parseAnimeDict = new HashMap<>();
        animeDict = new HashMap<>();
        animeList = new ArrayList<>();
        parseAnimeList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbList);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        setSupportActionBar(toolbar);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        rvAnimePreviews = findViewById(R.id.rvAnimePreviews);
        btnAddAnime = findViewById(R.id.btnAddAnime);

        // Create an adapter
        previewAdapter = new AnimePreviewAdapter(this);

        // Set adapter on the recycler view
        rvAnimePreviews.setAdapter(previewAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvAnimePreviews.setLayoutManager(linearLayoutManager);

        btnAddAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateListActivity.this, AddAnimeActivity.class);
                intent.putExtra(ANIME_DICT_CODE, Parcels.wrap(animeDict));
                intent.putExtra(PARSE_ANIME_DICT_CODE, Parcels.wrap(parseAnimeDict));
                startActivityForResult(intent, ANIME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ANIME_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                animeDict = Parcels.unwrap(data.getParcelableExtra(ANIME_DICT_CODE));
                parseAnimeDict = Parcels.unwrap(data.getParcelableExtra(PARSE_ANIME_DICT_CODE));

                animeList = new ArrayList<Anime>(animeDict.values());
                parseAnimeList = new ArrayList<ParseAnime>(parseAnimeDict.values());

                previewAdapter.clear();
                previewAdapter.addAll(animeList);
                if (animeList.isEmpty()) {
                    rvAnimePreviews.setVisibility(View.GONE);
                } else {
                    rvAnimePreviews.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.create) {

        }
        return super.onOptionsItemSelected(item);
    }
}