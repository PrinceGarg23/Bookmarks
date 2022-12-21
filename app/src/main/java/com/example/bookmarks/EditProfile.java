package com.example.bookmarks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookmarks.activities.DashActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    ImageView imageView;
    Uri imageUri;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageView = findViewById(R.id.editImage);
        TextView nameView = findViewById(R.id.editName);
        TextView emailView = findViewById(R.id.editEmail);
        Button saveButton = findViewById(R.id.submit);

        DatabaseReference d = FirebaseDatabase.getInstance().getReference("Registered User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(uid).child("name").getValue().toString();
                String email = snapshot.child(uid).child("email").getValue().toString();
                image = snapshot.child(uid).child("image").getValue().toString();

                nameView.setText(name);
                emailView.setText(email);
                Glide.with(EditProfile.this).load(image).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                d.child(uid).child("name").setValue(name);
                d.child(uid).child("email").setValue(email);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Users");
                StorageReference imageReference = storageReference.child(uid).child("profile.png");
                if(imageUri == null){
                    d.child(uid).child("image").setValue(image);}else{
                UploadTask uploadTask = imageReference.putFile(imageUri);
                uploadTask.addOnFailureListener(exception -> {
                    Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(EditProfile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                });
                Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        d.child(uid).child("image").setValue(downloadUri.toString());
                    } else {
                        Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });}

                Intent intent = new Intent(EditProfile.this, DashActivity.class);
                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                startActivity(intent);



            }
        });
    }

    private void imageChooser() {
        //choose image from phone
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            //set image to image view
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}