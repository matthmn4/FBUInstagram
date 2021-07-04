package com.example.fbuinstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.models.Comment;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCommentPFP;
        TextView tvCommentName, tvCommentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentName = itemView.findViewById(R.id.tvCommentName);
            ivCommentPFP = itemView.findViewById(R.id.tvCommentPFP);
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
        }

        public void bind(Comment comment) {
            tvCommentContent.setText(comment.getDescription());
            //Log.d("hello", comment.getUser().toString());//.getUsername());
            tvCommentName.setText(comment.getUser().getUsername());
            ParseFile profile = comment.getUser().getParseFile("profile");
            if (profile != null) {
                Glide.with(context)
                        .load(profile.getUrl())
                        .circleCrop()
                        .into(ivCommentPFP);
            } else {
                ivCommentPFP.setImageResource(R.drawable.instagram_user_outline_24);
            }

        }
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }
}
