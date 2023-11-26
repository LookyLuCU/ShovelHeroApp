package com.example.shovelheroapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shovelheroapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CustomerAcceptedWOActivity extends AppCompatActivity {

    DatabaseReference userTable;
    private String userId;
    private TextView shovellerName;
    private TextView shovellerGuardian;
    Button btnViewProfile;
    private CheckBox chkGuardianVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_accepted_woactivity);

        userTable = FirebaseDatabase.getInstance().getReference("users");

        shovellerName = findViewById(R.id.txtName);
        shovellerGuardian = findViewById(R.id.txtGuardian);
        btnViewProfile = findViewById(R.id.btnViewShovellerProfile);
        chkGuardianVerify = findViewById(R.id.checkBoxGuardianVerification);

        //GET USERID FROM LOGIN OR REGISTRATION
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
            if (userId != null) {
                retrieveShovellerProfile(userId);
            }
        }
    }
    private void retrieveShovellerProfile(String userId) {
        userTable.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        //display user profile info
                        shovellerName.setText(user.getFirstName() + " " + user.getLastName());
                        shovellerGuardian.setText(user.getGuardianIdUrl());

                        // Load profile Image
                        String profileImageUrl = user.getProfilePictureUrl();
                        ImageView profileImageView = findViewById(R.id.imgViewShoveller);
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(CustomerAcceptedWOActivity.this)
                                    .load(profileImageUrl).into(profileImageView);
                        }
                        btnViewProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentViewYouthJobs = new Intent(CustomerAcceptedWOActivity.this, YouthShovelerProfileActivity.class);
                                String youthId = user.getUserId();
                                intentViewYouthJobs.putExtra("USER_ID", youthId);
                                startActivity(intentViewYouthJobs);
                            }
                        });
                        if(user.getGuardianIdValidated()){
                            chkGuardianVerify.isChecked();
                        }
                    } else {
                        //handle no user data error
                    }
                } else {
                    //handle user id does not exist
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle error
            }
        });
    }
}