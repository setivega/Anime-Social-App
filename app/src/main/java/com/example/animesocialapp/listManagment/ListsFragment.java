package com.example.animesocialapp.listManagment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animesocialapp.R;
import com.example.animesocialapp.listManagment.ListAdapter;
import com.example.animesocialapp.models.AnimeList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListsFragment extends Fragment {

    public static final String TAG = "ReviewsFragment";
    private RecyclerView rvLists;
    private ListAdapter listAdapter;

    public ListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvLists = view.findViewById(R.id.rvLists);

        // Create an adapter
        listAdapter = new ListAdapter(view.getContext());

        // Set adapter on the recycler view
        rvLists.setAdapter(listAdapter);

        // Set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvLists.setLayoutManager(linearLayoutManager);

        queryLists();
    }

    private void queryLists() {
        ParseQuery<AnimeList> query = ParseQuery.getQuery(AnimeList.class);
        query.include(AnimeList.KEY_USER);
        query.include(AnimeList.KEY_ANIME);
        query.include("createdAt");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AnimeList>() {
            @Override
            public void done(List<AnimeList> list, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Timber.e("Issue getting posts: " + e.getLocalizedMessage());
                    if (e.getCode() == ParseException.CONNECTION_FAILED){
                        Timber.i("Network error being handled");
                        //Handle Network Error

                    }
                    return;
                }
                listAdapter.clear();
                listAdapter.addAll(list);
            }
        });

    }
}