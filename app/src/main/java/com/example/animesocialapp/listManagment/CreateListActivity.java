package com.example.animesocialapp.listManagment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animesocialapp.mainManagement.MainActivity;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.AnimePreviewAdapter;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class CreateListActivity extends AppCompatActivity {

    public static final String TAG = "CreateListActivity";
    private static final int ANIME_ACTIVITY_REQUEST_CODE = 42;
    public static final String ANIME_DICT_CODE = "ANIME_DICT_CODE";
    public static final String PARSE_ANIME_DICT_CODE = "PARSE_ANIME_DICT_CODE";
    private EditText etTitle;
    private EditText etDescription;
    private RecyclerView rvAnimePreviews;
    private Button btnAddAnime;
    private HashMap<String, ParseAnime> parseAnimeDict;
    private HashMap<String, Anime> animeDict;
    private List<Anime> animeList;
    private List<ParseAnime> parseAnimeList;
    private AnimePreviewAdapter previewAdapter;

    public static Intent createIntent(Context context, HashMap<String, Anime> animeDict, HashMap<String, ParseAnime> parseAnimeDict){
        Intent intent = new Intent(context, CreateListActivity.class);
        intent.putExtra(ANIME_DICT_CODE, Parcels.wrap(animeDict));
        intent.putExtra(PARSE_ANIME_DICT_CODE, Parcels.wrap(parseAnimeDict));
        return intent;
    }

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

        getSupportActionBar().setTitle(R.string.create_list_title);

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
                Intent intent = AddAnimeActivity.createIntent(CreateListActivity.this, animeDict, parseAnimeDict);
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
                    btnAddAnime.setText(R.string.add_anime_label);
                } else {
                    rvAnimePreviews.setVisibility(View.VISIBLE);
                    btnAddAnime.setText(R.string.edit_anime_label);
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
        final String title = etTitle.getText().toString();
        final String description = etDescription.getText().toString();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(item.getItemId() == R.id.create) {
            // Check if edit text contains text and list isn't empty
            if (title.isEmpty()) {
                Toast.makeText(CreateListActivity.this, R.string.empty_title, Toast.LENGTH_SHORT).show();
            } else if (parseAnimeList.isEmpty()) {
                Toast.makeText(CreateListActivity.this, R.string.empty_list, Toast.LENGTH_SHORT).show();
            } else {
                saveList(title, description, parseAnimeList, currentUser);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveList(String title, String description, List<ParseAnime> anime, ParseUser currentUser) {
        AnimeList newList = new AnimeList();
        newList.setTitle(title);
        if (description!= null && !description.trim().isEmpty()){
            newList.setDescription(description);
        }
        newList.setUser(currentUser);
        newList.setAnime(anime);
        newList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Timber.e("Error while saving: " + e);
                    Toast.makeText(CreateListActivity.this, R.string.save_error, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.i("List save was successful!");
                    Toast.makeText(CreateListActivity.this, R.string.save_list, Toast.LENGTH_SHORT).show();
                    goMainActivity();
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