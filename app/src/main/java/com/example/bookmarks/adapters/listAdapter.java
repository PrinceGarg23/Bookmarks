package com.example.bookmarks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarks.R;
import com.example.bookmarks.Saves;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder> {

    private ArrayList<Saves> bookmarks;
    private Context context;

    public listAdapter(ArrayList<Saves> bookmarks, Context context) {
        this.bookmarks = bookmarks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.heading.setText(bookmarks.get(position).getHeading());
        //holder.description.setText(bookmarks.get(position).getDescription());
        //holder.url.setText(bookmarks.get(position).getUrl());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(bookmarks.get(position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);

            }
        });
        Picasso.get().load(bookmarks.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading, description, url;
        ImageView image;
        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            //description = itemView.findViewById(R.id.description);
            //url = itemView.findViewById(R.id.url);
            image = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.bookmark_item);
        }
    }

}
