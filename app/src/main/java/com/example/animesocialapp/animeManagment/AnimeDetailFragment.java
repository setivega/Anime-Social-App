package com.example.animesocialapp.animeManagment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animesocialapp.R;
import com.example.animesocialapp.reviewManagement.ReviewsDetailFragment;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimeDetailFragment extends Fragment {


    private TextView tvGenre;
    private TextView tvDescription;
    private AnimeMetadata animeMetadata;

    public AnimeDetailFragment() {
        // Required empty public constructor
    }

    public static AnimeDetailFragment newInstance(AnimeMetadata animeMetadata) {
        AnimeDetailFragment fragment = new AnimeDetailFragment();
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
        return inflater.inflate(R.layout.fragment_anime_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGenre = view.findViewById(R.id.tvGenre);
        tvDescription = view.findViewById(R.id.tvDescription);

        tvGenre.setText(animeMetadata.getGenres());
        tvDescription.setText(animeMetadata.getDescription());


    }
}