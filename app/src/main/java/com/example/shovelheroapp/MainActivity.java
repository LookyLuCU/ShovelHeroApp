package com.example.shovelheroapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //***************************
    //FIREBASE AUTH ONLY ALLOWS 3 USER FIELDS - CONSIDERING SETTING UP AUTH VIA REALTIME DB
    //**************************

    /**
    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    public void getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String userName = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // [END get_user_profile]
    }
     **/

}