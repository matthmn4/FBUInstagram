package com.example.fbuinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.LoginActivity;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.adapters.ProfileAdapter;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements EditNameDialogFragment.EditNameDialogListener {

    public static final String TAG = "ProfileFragment";
    protected RecyclerView rvPosts;
    protected ProfileAdapter adapter;
    protected List<Post> allPosts;

    ImageView ivPFP;
    TextView tvName;
    Button btnLogout;
    Button btnEdit;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        }

        rvPosts = view.findViewById(R.id.rvPosts);
        tvName = view.findViewById(R.id.tvProfileName);
        ivPFP = view.findViewById(R.id.ivPFP);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnChangePFP);
        allPosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), allPosts);


        rvPosts.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvPosts.setLayoutManager(gridLayoutManager);

        queryPosts();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

    }


    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Some Title");
        editNameDialogFragment.setTargetFragment(ProfileFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER); // pointer
        query.setLimit(20);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_DATE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                tvName.setText(ParseUser.getCurrentUser().getUsername());
                ParseFile profile = posts.get(0).getUser().getParseFile("profile");
                if (profile != null) {
                    Glide.with(getContext())
                            .load(profile.getUrl())
                            .circleCrop()
                            .into(ivPFP);
                }
            }
        });
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(inputText);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null != e) {
                    Log.e(TAG, "Issue with getting user", e);
                    return;
                }
                tvName.setText(inputText);
            }
        });

    }
}
