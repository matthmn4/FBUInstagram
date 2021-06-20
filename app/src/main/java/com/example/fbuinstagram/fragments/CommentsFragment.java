package com.example.fbuinstagram.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.adapters.CommentsAdapter;
import com.example.fbuinstagram.models.Comment;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends DialogFragment {

    public static final String TAG = "CommentsFragment";

    EditText etComments;
    Button btnSubmit;
    RecyclerView rvComments;
    Post post;
    CommentsAdapter adapter;
    List<Comment> allComments;
    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static CommentsFragment newInstance(int position, Parcelable post) {
        CommentsFragment frag = new CommentsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelable("post", post);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvComments = view.findViewById(R.id.rvComments);
        etComments = (EditText) view.findViewById(R.id.etComments);
        btnSubmit = view.findViewById(R.id.button);
        // Fetch arguments from bundle and set title
        int position = getArguments().getInt("position");
        post = Parcels.unwrap(getArguments().getParcelable("post"));

        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(getContext(), allComments);

        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);

        queryComments();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = etComments.getText().toString();
                if (desc.isEmpty()) {
                    Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveComments(desc, currentUser, post);
            }
        });
    }

    private void saveComments(String desc, ParseUser currentUser, Post post) {
        //pb.setVisibility(ProgressBar.VISIBLE);
        Comment comment = new Comment();
        comment.setDescription(desc);
        comment.setUser(currentUser);
        comment.setPost(post);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                etComments.setText("");
                queryComments();
                //pb.setVisibility(ProgressBar.INVISIBLE);
            }
        });

    }

    private void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER); //pointer
        query.include(Comment.KEY_POST); // pointer
        query.whereEqualTo(Comment.KEY_POST, post);
        //query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                Log.d(TAG, String.valueOf(objects.size()));
                for (Comment comment : objects) {
                    Log.i(TAG, "Comment: " + comment.getDescription());
                }
                adapter.clear();
                adapter.notifyDataSetChanged();
                allComments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });

    }

}