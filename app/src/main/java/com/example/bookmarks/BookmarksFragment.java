package com.example.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarks.adapters.listAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookmarksFragment extends Fragment {

    ArrayList<Saves> bookmarks;
    RecyclerView recyclerView;
    listAdapter adapter;
    int n = 0;
    ProgressBar load;
    TextView noBookmarks;
    private  static  final int LOADER_ID = 1;

    public BookmarksFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        load = rootView.findViewById(R.id.progressBar);
        noBookmarks = rootView.findViewById(R.id.noBookmarksTextView);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            bookmarks = new ArrayList<>();
            recyclerView = rootView.findViewById(R.id.bookmarksRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
            adapter = new listAdapter(bookmarks, getContext());
            recyclerView.setAdapter(adapter);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Registered User");
            Log.d("TAG", "onCreateView: " + databaseReference);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(getContext(), "Please Login to view your bookmarks", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }else {
                String uid = user.getUid();
                DatabaseReference userReference = databaseReference.child(uid);
                Log.d("TAG", "onCreateView: " + userReference);
                DatabaseReference bookmarkReference = userReference.child("bookmark");
                Log.d("BookmarksFragment", "onCreateView: " + bookmarkReference);
                bookmarkReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        n=0;
                        bookmarks.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (n==0){
                                n++;
                                continue;
                            }
                            Log.d("Bookmarks", "onDataChange: " + dataSnapshot);
                            Saves save = dataSnapshot.getValue(Saves.class);
                            bookmarks.add(save);
                        }
                        load.setVisibility(View.GONE);
                        if(bookmarks.isEmpty()){
                            Log.d("Bookmarks", "onDataChange: No Bookmarks");
                            noBookmarks.setVisibility(View.VISIBLE);
                        }
                        Log.d("Bookmarks", "onDataChange: " + bookmarks.size());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            } else{
                // Display error
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                load.setVisibility(View.GONE);
                noBookmarks.setVisibility(View.VISIBLE);
            }


        return rootView;
    }
}