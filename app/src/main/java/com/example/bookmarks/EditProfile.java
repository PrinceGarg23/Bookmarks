package com.example.bookmarks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageView imageView = findViewById(R.id.editImage);
        TextView nameView = findViewById(R.id.editName);
        TextView emailView = findViewById(R.id.editEmail);
        Button saveButton = findViewById(R.id.submit);

        imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //choose image from phone
                 imageChooser();
             }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                String email = emailView.getText().toString();

            }
        });
    }

    private void imageChooser() {
        //choose image from phone
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 1);
    }
}