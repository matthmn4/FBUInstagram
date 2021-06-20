package com.example.fbuinstagram.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    public interface onClickListener {
        //void onProfileImageClick(Tweet tweet);
        void onComment(int pos, String objectId);
        void onItemClick(View itemView, int position);
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private Context context;
    private List<Post> posts;
    private onClickListener onClickListener;

    ViewHolder viewHolder;

    public PostsAdapter(Context context, List<Post> posts, onClickListener onClickListener) {
        this.context = context;
        this.posts = posts;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        viewHolder = new ViewHolder(view, onClickListener);
        return viewHolder;
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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        ImageView ivImage;
        TextView tvDescription;
        TextView tvUsernameTop;
        TextView tvDate;
        ImageView ivHeaderImage;
        ImageButton btnLike;
        ImageButton btnComment;
        Animation scaleUp, scaleDown;
        TextView tvLikes;
        TextView tvComments;


        public ViewHolder(@NonNull View itemView, onClickListener listener) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivPost);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsernameTop = itemView.findViewById(R.id.tvHeaderName);
            ivHeaderImage = itemView.findViewById(R.id.ivProfilePostHeader);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            tvLikes = itemView.findViewById(R.id.tvLikes);

            tvComments = itemView.findViewById(R.id.tvComments);

            scaleUp = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale);
            scaleDown = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_down);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsernameTop.setText(post.getUser().getUsername());

            int likeCount = 0;
            if(post.getUsersLiked() != null) {
                likeCount = post.getUsersLiked().length();
            }
            tvLikes.setText(String.format("%s likes", likeCount));

            // see if the current user has already liked this post, then make the heart red
            if(post.isLikedBy(ParseUser.getCurrentUser())) {
                // make the heart red
                btnLike.setImageResource(R.drawable.ufi_heart_active);
                btnLike.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFDC3B3B")));
            }

            tvDate.setText(String.format("%s", getRelativeTimeAgo(post.getCreatedAt())));

            tvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onComment(getAdapterPosition(), post.getObjectId());
                }
            });

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onComment(getAdapterPosition(), post.getObjectId());
                }
            });

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivImage);
            }
            ParseFile profile = post.getUser().getParseFile("profile");
            if (profile != null) {
                Glide.with(context)
                        .load(profile.getUrl())
                        .circleCrop()
                        .into(ivHeaderImage);
            }
            btnLike.setTag(R.drawable.ufi_heart);
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isLikedByCurrentUser = post.isLikedBy(ParseUser.getCurrentUser());
                    int likeCount = 0;

                    if(post.getUsersLiked() != null) {
                        likeCount = post.getUsersLiked().length();
                    }

                    if(isLikedByCurrentUser) {
                        likeCount--;
                        btnLike.setImageResource(R.drawable.ufi_heart);
                        btnLike.setTag(R.drawable.ufi_heart);
                        btnLike.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF000000")));
                        btnLike.startAnimation(scaleDown);
                        tvLikes.setText(String.format("%s likes", likeCount));
                        try {
                            post.removeFromLikeArray(ParseUser.getCurrentUser());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        post.saveInBackground();
                    } else {
                        likeCount++;
                        btnLike.setImageResource(R.drawable.ufi_heart_active);
                        btnLike.setTag(R.drawable.ufi_heart_active);
                        btnLike.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFDC3B3B")));
                        btnLike.startAnimation(scaleUp);
                        tvLikes.setText(String.format("%s likes", likeCount));
                        post.addToLikeArray(ParseUser.getCurrentUser());
                        post.saveInBackground();
                    }

//                    onClickListener.onFavoriteClick(getLayoutPosition(), post.liked, l); // saves to backend
                }

            });


        }


    }


    public String getRelativeTimeAgo(Date rawJsonDate) {
        String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        sf.setLenient(true);

        long time = rawJsonDate.getTime();
        long now = System.currentTimeMillis();

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + "d";
        }

    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
