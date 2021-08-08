package com.example.animesocialapp.mainManagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.animesocialapp.listManagment.ListsFragment;
import com.example.animesocialapp.profileManagement.LikedFragment;
import com.example.animesocialapp.profileManagement.ProfileListsFragment;
import com.example.animesocialapp.profileManagement.ProfileReviewsFragment;
import com.example.animesocialapp.reviewManagement.ReviewsFragment;

import org.jetbrains.annotations.NotNull;

public class ProfileFragmentAdapter extends FragmentStateAdapter {
    public ProfileFragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ProfileListsFragment();
            case 2:
                return new LikedFragment();
            default:
                return new ProfileReviewsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
