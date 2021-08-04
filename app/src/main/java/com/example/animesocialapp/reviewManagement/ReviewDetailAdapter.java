package com.example.animesocialapp.reviewManagement;

import android.content.Context;
import android.content.Intent;
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
import com.example.animesocialapp.ParseRelativeDate;
import com.example.animesocialapp.R;
import com.example.animesocialapp.animeManagment.ParseAnime;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ReviewDetailAdapter extends RecyclerView.Adapter<ReviewDetailAdapter.ViewHolder>{

    public static final String TAG = "ReviewDetailAdapter";
    public static final String PROFILE_IMAGE = "profileImage";
    private Context context;
    private List<Review> reviews;

    public ReviewDetailAdapter(Context context) {
        this.context = context;
        reviews = new ArrayList<>();
    }


    @NonNull
    @Override
    public ReviewDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_review_detail, parent, false);
        return new ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewDetailAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        Review review = reviews.get(position);
        // Bind the post data into the View Holder
        holder.bind(review);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvCreatedAt;
        TextView tvReview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvReview = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Review review) {
            ParseUser user = review.getUser();

            tvUsername.setText(user.getUsername());
            tvCreatedAt.setText(ParseRelativeDate.getRelativeTimeAgo(review.getCreatedAt().toString()));
            tvReview.setText(review.getDescription());


            ParseFile profileImage = (ParseFile) user.get(PROFILE_IMAGE);
            Glide.with(context).load(profileImage.getUrl())
                    .transform(new CircleCrop())
                    .into(ivProfileImage);

        }


    }
}
