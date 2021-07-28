package com.example.animesocialapp.mainManagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.animesocialapp.listManagment.CreateListActivity;
import com.example.animesocialapp.loginManagement.LoginActivity;
import com.example.animesocialapp.R;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private TextView tvUsername;
    private ParseUser currentUser;

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

        currentUser = ParseUser.getCurrentUser();

        tvUsername = view.findViewById(R.id.tvUsername);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvUsername.setText(currentUser.getUsername());

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}