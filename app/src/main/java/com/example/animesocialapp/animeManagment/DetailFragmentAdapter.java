package com.example.animesocialapp.animeManagment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.animesocialapp.profileManagement.LikedFragment;
import com.example.animesocialapp.profileManagement.ProfileListsFragment;
import com.example.animesocialapp.profileManagement.ProfileReviewsFragment;
import com.example.animesocialapp.reviewManagement.ReviewsDetailFragment;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class DetailFragmentAdapter extends FragmentStateAdapter {

    private AnimeMetadata animeMetadata;

    public DetailFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public DetailFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, AnimeMetadata animeMetadata) {
        super(fragmentManager, lifecycle);
        this.animeMetadata = animeMetadata;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return ReviewsDetailFragment.newInstance(animeMetadata);
            default:
                Timber.i(animeMetadata.getTitle());
                return AnimeDetailFragment.newInstance(animeMetadata);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
