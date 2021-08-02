package com.example.animesocialapp.listManagment;

import android.content.Context;
import android.content.Intent;
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
import com.example.animesocialapp.reviewManagement.ReviewDetailActivity;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ListDetailAdapter extends RecyclerView.Adapter<ListDetailAdapter.ViewHolder> {

    public static final String TAG = "ListDetailAdapter";
    private Context context;
    private List<ParseAnime> animeList;

    public ListDetailAdapter(Context context) {
        this.context = context;
        animeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(context).inflate(R.layout.item_list_detail, parent, false);
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDetailAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        ParseAnime anime = animeList.get(position);
        // Bind the post data into the View Holder
        holder.bind(anime);
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public void addAll(List<ParseAnime> list){
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

        public void bind(ParseAnime anime) {

            Glide.with(context).load(anime.getPosterPath())
                    .transform(new CenterCrop(), new RoundedCorners(4))
                    .into(ivPoster);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // validating position
            if (position != RecyclerView.NO_POSITION) {
                ParseAnime parseAnime = animeList.get(position);
                Anime anime = new Anime(parseAnime);
                context.startActivity(AnimeDetailActivity.createIntent(context, anime));
            }
        }
    }
}
