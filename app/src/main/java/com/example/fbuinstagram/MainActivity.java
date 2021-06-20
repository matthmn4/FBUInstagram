package com.example.fbuinstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fbuinstagram.fragments.ComposeFragment;
import com.example.fbuinstagram.fragments.PostsFragment;
import com.example.fbuinstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.reuse_toolbar);
        setSupportActionBar(myToolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        Log.d(TAG, "Profile");
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_compose:
                        Log.d(TAG, "Compose");
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_home:
                    default:
                        Log.d(TAG, "Home");
                        fragment = new PostsFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}