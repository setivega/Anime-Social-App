package com.example.animesocialapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.animesocialapp.models.Anime;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    public static final String TAG = "SearchAdapter";
    private Context context;
    private List<Anime> animes;

    public SearchAdapter(Context context) {
        this.context = context;
        animes = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false);
        return new ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        Anime anime = animes.get(position);
        // Bind the post data into the View Holder
        holder.bind(anime);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public void addAll(List<Anime> list){
        animes.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        animes.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvDate;
        ImageButton btnListAnime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvSeason);
            btnListAnime = itemView.findViewById(R.id.btnListAnime);
            itemView.setOnClickListener(this);
        }

        public void bind(Anime anime) {

            tvTitle.setText(anime.getTitle());
            tvDate.setText(ParseRelativeDate.getRelativeSeasonYear(anime.getStartDate()));

            Glide.with(context).load(anime.getPosterPath())
                    .transform(new CenterCrop(), new RoundedCorners(4))
                    .into(ivPoster);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // validating position
            if (position != RecyclerView.NO_POSITION) {
                Anime anime = animes.get(position);
                Log.i(TAG, "Tapped on: " + anime.getTitle());
                Log.i(TAG, ParseRelativeDate.getRelativeSeasonYear(anime.getStartDate()));
            }
        }
    }
}
