package com.example.bookmarks.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmarks.AddFragment;
import com.example.bookmarks.BookmarksFragment;
import com.example.bookmarks.ExploreFragment;
import com.example.bookmarks.ProfileFragment;
import com.example.bookmarks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //set default as bookmarks
        bottomNavigationView.setSelectedItemId(R.id.bookmarks);
        bottomNavigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new BookmarksFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case (R.id.bookmarks):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookmarksFragment).commit();
                        return true;
                    case (R.id.add):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, addFragment).commit();
                        return true;
                    case (R.id.explore):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, exploreFragment).commit();
                        return true;
                    case (R.id.profile):
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;

                }
                return false;
            }
        });

    }

    BookmarksFragment bookmarksFragment = new BookmarksFragment();
    AddFragment addFragment = new AddFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    ProfileFragment profileFragment = new ProfileFragment();


}