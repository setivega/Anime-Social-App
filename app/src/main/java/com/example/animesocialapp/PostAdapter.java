package com.example.animesocialapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.animesocialapp.models.ParseAnime;
import com.example.animesocialapp.models.Review;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public static final String TAG = "PostAdapter";
    public static final String PROFILE_IMAGE = "profileImage";
    private Context context;
    private List<Review> reviews;

    public PostAdapter(Context context) {
        this.context = context;
        reviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        Review review = reviews.get(position);
        // Bind the post data into the View Holder
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addAll(List<Review> list){
        reviews.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivBackground;
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvSeason;
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvCreatedAt;
        TextView tvReview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackground = itemView.findViewById(R.id.ivBackground);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSeason = itemView.findViewById(R.id.tvSeason);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvReview = itemView.findViewById(R.id.tvReview);
            itemView.setOnClickListener(this);
        }

        public void bind(Review review) {
            ParseAnime anime = (ParseAnime) review.getAnime();
            ParseUser user = review.getUser();

            tvTitle.setText(anime.getTitle());
            tvSeason.setText(anime.getSeason());
            tvUsername.setText(user.getUsername());
            tvCreatedAt.setText(ParseRelativeDate.getRelativeTimeAgo(review.getCreatedAt().toString()));
            tvReview.setText(review.getDescription());

            Glide.with(context).load(anime.getPosterPath())
                    .transform(new CenterCrop(), new RoundedCorners( 29))
                    .into(ivBackground);

            Glide.with(context).load(anime.getPosterPath())
                    .transform(new CenterCrop(), new RoundedCorners(8))
                    .into(ivPoster);

            ParseFile profileImage = (ParseFile) user.get(PROFILE_IMAGE);
            Glide.with(context).load(profileImage.getUrl())
                    .transform(new CircleCrop())
                    .into(ivProfileImage);

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
