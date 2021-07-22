package com.example.animesocialapp.animeManagment;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.animesocialapp.reviewManagement.PostReviewActivity;
import com.example.animesocialapp.R;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    public enum PostType {
        REVIEW,
        LIST
    }

    public enum Display {
        SEARCH,
        ADDED
    }

    public interface OnClickListener {
        void onButtonClicked(int position, Drawable background, Integer btn, Display display);
    }

    public static final String TAG = "SearchAdapter";
    private Context context;
    private PostType postType;
    private OnClickListener clickListener;
    private List<Anime> animes;
    private List<String> addedAnimeIDs;
    public Display display;

    public SearchAdapter(Context context, PostType postType, OnClickListener clickListener) {
        this.context = context;
        this.postType = postType;
        this.clickListener = clickListener;
        animes = new ArrayList<>();
        addedAnimeIDs = new ArrayList<>();
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
        // Check if recycler view is displaying added items
        if (display == Display.ADDED) {
            // Changing the background color of the item view
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_gray));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.app_black));
        }
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

    public Anime getAnime(int position){
        return animes.get(position);
    }

    public void clear() {
        animes.clear();
        notifyDataSetChanged();
    }

    public void addIDs(List<String> list) {
        addedAnimeIDs.clear();
        addedAnimeIDs.addAll(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvSeason;
        ImageButton btnListAnime;
        View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSeason = itemView.findViewById(R.id.tvSeason);
            btnListAnime = itemView.findViewById(R.id.btnListAnime);
            divider = itemView.findViewById(R.id.divider);
            if (postType == PostType.REVIEW) {
                itemView.setOnClickListener(this);
            }
        }

        public void bind(Anime anime) {

            tvTitle.setText(anime.getTitle());
            tvSeason.setText(anime.getSeason());

            Glide.with(context).load(anime.getPosterPath())
                    .transform(new CenterCrop(), new RoundedCorners(4))
                    .into(ivPoster);

            Timber.i("Anime IDs: " + addedAnimeIDs);

            if (!addedAnimeIDs.contains(anime.getMalID())) {
                btnListAnime.setBackgroundResource(R.drawable.add_icon);
                btnListAnime.setTag(R.drawable.add_icon);
            } else {
                btnListAnime.setBackgroundResource(R.drawable.remove_icon);
                btnListAnime.setTag(R.drawable.remove_icon);
            }

            Integer btnTag = (Integer) btnListAnime.getTag();

            if (postType == PostType.LIST) {
                btnListAnime.setVisibility(View.VISIBLE);
                btnListAnime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onButtonClicked(getAdapterPosition(), btnListAnime.getBackground(), btnTag, display);
                    }
                });
            }

            if (display == Display.ADDED) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // validating position
            if (position != RecyclerView.NO_POSITION) {
                // Getting movie at position
                Anime anime = animes.get(position);
                context.startActivity(PostReviewActivity.createIntent(context, anime));
            }
        }
    }
}
