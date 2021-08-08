package com.example.animesocialapp.recommendationManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.Anime;
import com.example.animesocialapp.animeManagment.AnimeDetailActivity;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.example.animesocialapp.animeManagment.SearchAdapter;
import com.example.animesocialapp.reviewManagement.PostReviewActivity;

import java.util.ArrayList;
import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    public static final String TAG = "RecommendationAdapter";
    private Context context;
    private List<Anime> animeList;

    public RecommendationAdapter(Context context) {
        this.context = context;
        animeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(context).inflate(R.layout.item_list_detail, parent, false);
        return new RecommendationAdapter.ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        Anime anime = animeList.get(position);
        // Bind the post data into the View Holder
        holder.bind(anime);
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public void addAll(List<Anime> list){
        animeList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        animeList.clear();
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
                // Getting movie at position
                Anime anime = animeList.get(position);
                context.startActivity(AnimeDetailActivity.createIntent(context, anime, 0));
            }
        }
    }
}
