package com.example.fbuinstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    LayoutInflater inflater;

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGrid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGrid = itemView.findViewById(R.id.ivGridPicture);
            ivGrid.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        public void bind(Post post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivGrid);
            }

        }
    }

}
