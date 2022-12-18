package com.example.bookmarks.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmarks.AddFragment;
import com.example.bookmarks.BookmarksFragment;
import com.example.bookmarks.ExploreFragment;
import com.example.bookmarks.MainActivity;
import com.example.bookmarks.ProfileFragment;
import com.example.bookmarks.R;
import com.example.bookmarks.Saves;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class DashActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    String url, title, description, image;
    Bitmap bitmap;
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


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            } else if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent);
            }
        }

    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
        }
    }

    private void handleSendText(Intent intent) {
        url = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (url != null) {
            new Content().execute();
        }
    }

    BookmarksFragment bookmarksFragment = new BookmarksFragment();
    AddFragment addFragment = new AddFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    ProfileFragment profileFragment = new ProfileFragment();


    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                Document document = Jsoup.connect(url).get();

                Log.d("TAG", "doInBackground: " + document);

                //Get the title from meta data of the website
                try {
                    title = document.select("meta[property=og:title]").first().attr("content");
                } catch (Exception e) {
                    title = document.title();
                }

                try{
                    url = document.select("meta[property=og:url]").first().attr("content");
                }catch (Exception e){
                    url = url;
                }

                //Get the description from meta data
                description = document.select("meta[name=description]").get(0).attr("content");

                //Get the image from link tag
                image = document.select("link[rel=icon]").get(0).attr("href");

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered User").child(uid).child("bookmark");
                    Saves saves = new Saves(title, url, description, image);
                    reference.push().setValue(saves);
                    //close activity
                    finish();
                } else {
                    Toast.makeText(DashActivity.this, "Please login to add bookmarks", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(DashActivity.this, MainActivity.class);
                }
            } else {
                // not connected to the internet
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}