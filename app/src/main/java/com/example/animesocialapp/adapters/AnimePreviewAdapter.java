package com.example.animesocialapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.style.ParagraphStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.animesocialapp.models.Anime;
import com.example.animesocialapp.models.ParseAnime;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class AnimePreviewAdapter extends RecyclerView.Adapter<AnimePreviewAdapter.ViewHolder>{


    public interface OnClickListener {
        void onAnimeClicked(int position);
    }

    public static final String TAG = "SearchAdapter";
    private Context context;
    private OnClickListener clickListener;
    private List<Anime> animes;

    public AnimePreviewAdapter(Context context) {
        this.context = context;
        animes = new ArrayList<>();
    }

    @NonNull
    @Override
    public AnimePreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_anime_preview, parent, false);
        return new ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimePreviewAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        Anime anime = animes.get(position);
        // Check if recycler view is displaying added items
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Anime anime) {

            Glide.with(context).load(anime.getPosterPath())
                    .transform(new CenterCrop(), new RoundedCorners(4))
                    .into(ivPoster);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // validating position
            if (position != RecyclerView.NO_POSITION) {

            }
        }
    }
}
