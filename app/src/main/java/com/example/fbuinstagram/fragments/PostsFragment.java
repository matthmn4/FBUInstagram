package com.example.fbuinstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fbuinstagram.EndlessRecyclerViewScrollListener;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.adapters.PostsAdapter;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment implements PostsAdapter.onClickListener{

    public static final String TAG = "PostsFragment";
    protected RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts, this);

        rvPosts.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                queryPosts();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);


        queryPosts();
        }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER); // pointer
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_DATE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post);
                }
                adapter.clear();
                adapter.notifyDataSetChanged();
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }
        });
    }


    @Override
    public void onComment(int pos, String objectId) {
        FragmentManager fm = getFragmentManager();
        Log.d(TAG, allPosts.get(pos).toString());
        CommentsFragment commentsFragment = CommentsFragment.newInstance(pos, Parcels.wrap(allPosts.get(pos)));
        commentsFragment.show(fm, "fragment_comments");
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }
}