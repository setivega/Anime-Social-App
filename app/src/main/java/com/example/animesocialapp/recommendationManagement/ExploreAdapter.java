package com.example.animesocialapp.recommendationManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder>{


    public static final String TAG = "ListAdapter";
    public static final List<String> EXPLORE_TITLES = new ArrayList<>(Collections.unmodifiableList(List.of("Top Anime", "Most Popular", "Current Season")));
    private Context context;
    private List<List<Anime>> animeLists;

    public ExploreAdapter(Context context) {
        this.context = context;
        animeLists = new ArrayList<>();
    }


    @NonNull
    @Override
    public ExploreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_explore_list, parent, false);
        return new ExploreAdapter.ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        List<Anime> animeList = animeLists.get(position);
        // Bind the post data into the View Holder
        holder.bind(animeList);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return animeLists.size();
    }

    public void addAll(List<List<Anime>> list){
        animeLists.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        animeLists.clear();
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        RecyclerView rvAnimes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            rvAnimes = itemView.findViewById(R.id.rvAnimes);
            itemView.setOnClickListener(this);
        }

        public void bind(List<Anime> animeList) {
            int position = getAdapterPosition();

            tvTitle.setText(EXPLORE_TITLES.get(position));

            // Create an adapter
            ExplorePreviewAdapter previewAdapter = new ExplorePreviewAdapter(itemView.getContext());

            // Set adapter on the recycler view
            rvAnimes.setAdapter(previewAdapter);

            // Set layout manager on recycler view
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvAnimes.setLayoutManager(linearLayoutManager);

            if (animeList != null) {
                previewAdapter.addAll(animeList);
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            // validating position
            if (position != RecyclerView.NO_POSITION) {
                return;
            }
        }
    }
}