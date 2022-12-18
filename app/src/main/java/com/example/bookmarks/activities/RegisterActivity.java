package com.example.bookmarks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmarks.R;
import com.example.bookmarks.ReadWriteUserDetails;
import com.example.bookmarks.Saves;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    ProgressBar progressBar;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        etName = findViewById(R.id.usernameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);

        btnRegister = findViewById(R.id.registerButton);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Name is required");
                etName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                etPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                etPassword.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
            registerUser(name, email, password);
            // register the user in firebase

        });
    }

    private void registerUser(String name, String email, String password) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                            ArrayList<Saves> bookmarks = new ArrayList<>();
                            bookmarks.add(new Saves("a","a","a","a"));

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name,email,bookmarks);

                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User");
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        btnRegister.setVisibility(View.VISIBLE);
                                        //send verification email
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(RegisterActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                        //open user profile after successful registration
                                        Intent intent = new Intent(RegisterActivity.this, DashActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "User registration failed", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        btnRegister.setVisibility(View.VISIBLE);
                                    }
                                }
                            });




                        }else{
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e) {
                                etPassword.setError("Password must be at least 6 characters");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                etEmail.setError("Invalid email");
                                etEmail.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e){
                                etEmail.setError("Email already exists");
                                etEmail.requestFocus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}