package com.example.animesocialapp.listManagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.animesocialapp.ParseRelativeDate;
import com.example.animesocialapp.R;
import com.example.animesocialapp.models.AnimeList;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{


    public static final String TAG = "ListAdapter";
    public static final String PROFILE_IMAGE = "profileImage";
    private Context context;
    private List<AnimeList> animeLists;

    public ListAdapter(Context context) {
        this.context = context;
        animeLists = new ArrayList<>();
    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        AnimeList animeList = animeLists.get(position);
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

    public void addAll(List<AnimeList> list){
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
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvCreatedAt;
        TextView tvDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            rvAnimes = itemView.findViewById(R.id.rvAnimes);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        public void bind(AnimeList animeList) {
            ParseUser user = animeList.getUser();

            tvTitle.setText(animeList.getTitle());
            tvUsername.setText(user.getUsername());
            tvCreatedAt.setText(ParseRelativeDate.getRelativeTimeAgo(animeList.getCreatedAt().toString()));

            String description = animeList.getDescription();
            if (description!= null && !description.trim().isEmpty()){
                tvDescription.setText(description);
                tvDescription.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            Timber.i(animeList.getDescription());

            // Create an adapter
            ListPreviewAdapter previewAdapter = new ListPreviewAdapter(itemView.getContext());

            // Set adapter on the recycler view
            rvAnimes.setAdapter(previewAdapter);

            // Set layout manager on recycler view
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvAnimes.setLayoutManager(linearLayoutManager);

            if (animeList.getAnime() != null) {
                previewAdapter.addAll(animeList.getAnime());
            }

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
