package com.example.animesocialapp.listManagment;

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

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ListPreviewAdapter extends RecyclerView.Adapter<ListPreviewAdapter.ViewHolder>{


    public interface OnClickListener {
        void onAnimeClicked(int position);
    }

    public static final String TAG = "ListPreviewAdapter";
    private Context context;
    private OnClickListener clickListener;
    private List<ParseAnime> animeList;

    public ListPreviewAdapter(Context context) {
        this.context = context;
        animeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListPreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View animeView = LayoutInflater.from(context).inflate(R.layout.item_list_preview, parent, false);
        return new ViewHolder(animeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPreviewAdapter.ViewHolder holder, int position) {
        // Get the post at the position
        ParseAnime anime = animeList.get(position);
        // Check if recycler view is displaying added items
        // Bind the post data into the View Holder
        holder.bind(anime);
    }

    @Override
    public int getItemCount() {
        Timber.i("Size: " + animeList.size());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ImageView ivPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPreview = itemView.findViewById(R.id.ivPreview);
            itemView.setOnLongClickListener(this);
        }

        public void bind(ParseAnime anime) {

            Glide.with(context).load(anime.getPosterPath())
                        .transform(new CenterCrop(), new RoundedCorners(4))
                        .into(ivPreview);

        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            // validating position
            if (position != RecyclerView.NO_POSITION) {
                ParseAnime parseAnime = animeList.get(position);
                Anime anime = new Anime(parseAnime);
                context.startActivity(AnimeDetailActivity.createIntent(context, anime, 0));
            }
            return false;
        }
    }
}
