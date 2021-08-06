package com.example.animesocialapp.mainManagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.animesocialapp.listManagment.CreateListActivity;
import com.example.animesocialapp.loginManagement.LoginActivity;
import com.example.animesocialapp.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    public static final String PROFILE_IMAGE = "profileImage";
    private Button btnLogout;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private ParseUser currentUser;
    private TabLayout tabLayout;
    private ViewPager2 vpProfile;
    private ProfileFragmentAdapter fragmentAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUser = ParseUser.getCurrentUser();

        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.profile_label);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvUsername = view.findViewById(R.id.tvUsername);
        tabLayout = view.findViewById(R.id.tabLayout);
        vpProfile = view.findViewById(R.id.vpDetail);
        btnLogout = view.findViewById(R.id.btnLogout);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragmentAdapter = new ProfileFragmentAdapter(fm, getLifecycle());
        vpProfile.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.reviews_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.lists_label));
        tabLayout.addTab(tabLayout.newTab().setText("Liked"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpProfile.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpProfile.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        currentUser = ParseUser.getCurrentUser();

        tvUsername.setText(currentUser.getUsername());

        ParseFile profileImage = (ParseFile) currentUser.get(PROFILE_IMAGE);
        Glide.with(getContext()).load(profileImage.getUrl())
                .transform(new CircleCrop())
                .into(ivProfileImage);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goLoginActivity();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId() == R.id.createList) {
            Intent intent = new Intent(getContext(), CreateListActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        return super.onOptionsItemSelected(item);
    }

    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}