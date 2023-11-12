package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class YouthShovelerProfileActivity extends AppCompatActivity {

    private static final String TAG = "YouthShovelerProfileActivity";

    //initialize ShovelHeroDB (Firebase)
    DatabaseReference shovelHeroDatabase;

    private String youthId;
    private TextView accountTypeTV;
    private TextView usernameTV;
    private TextView passwordTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private DatePicker birthdateDatePicker;
    private TextView emailTV;
    private TextView phoneTV;
    private String userId;
    private String guardianId;

    //address list
    private TextView addressTV;

    //private Address cityTV;
    //private Address provinceTV;
    //private Address postalCodeTV;
    //private Address countryTV;
    //private List<Address> addressList;

    //buttons
    Button btnViewJobs;
    Button btnManagePaymentInfo;
    Button btnManageProfileInfo;
    Button btnEditPassword;
    Button btnViewRatings;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_youth_shoveler);

        shovelHeroDatabase = FirebaseDatabase.getInstance().getReference("users");

        usernameTV = findViewById(R.id.tvUsername);
        passwordTV = findViewById(R.id.tvPassword);
        firstNameTV = findViewById(R.id.tvFirstName);
        lastNameTV = findViewById(R.id.tvLastname);
        emailTV = findViewById(R.id.tvEmail);
        phoneTV = findViewById(R.id.tvPhone);
        addressTV = findViewById(R.id.tvAddress);

        //get username from registration or UserId from Login
        Intent intent = getIntent();
        if (intent != null) {
            String currentYouthId = intent.getStringExtra("USER_ID");
            if (currentYouthId != null) {
                final String youthId = currentYouthId;

                //get and display user data
                retrieveUserProfile(youthId);
            }
        }
    }


    private void retrieveUserProfile(String youthId) {
        shovelHeroDatabase.child(youthId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Address address = snapshot.getValue(Address.class);

                    if (user != null) {

                        //display user profile info
                        usernameTV.setText("Username: " + user.getUsername().toString());
                        passwordTV.setText("Password: " + user.getPassword().toString());
                        firstNameTV.setText("First Name: " + user.getFirstName().toString());
                        lastNameTV.setText("Last Name: " + user.getLastName().toString());
                        emailTV.setText("Email: " + user.getEmail().toString());
                        phoneTV.setText("Phone Number: " + user.getPhoneNo().toString());
                        addressTV.setText("Address: " + address.getAddress() +
                                ", " + address.getCity() +
                                ", " + address.getProvince() +
                                ", " + address.getPostalCode() +
                                ", " + address.getCountry());


                        //VIEW JOBS BUTTON
                        btnViewJobs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentNewWO = new Intent(YouthShovelerProfileActivity.this, GetWorkOrdersActivity.class);
                                String youthId = user.getUserId();
                                intentNewWO.putExtra("USER_ID", youthId);
                                startActivity(intentNewWO);
                            }
                        });

                        //MANAGE PAYMENT BUTTON
                        btnManagePaymentInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: Manage Payment activity under construction", Toast.LENGTH_SHORT).show();
                                /**
                                 Intent intentNewWO = new Intent(YouthShovelerProfileActivity.this, ManagePaymentActivity.class);
                                 String youthId = user.getUserId();
                                 intentNewWO.putExtra("USER_ID", youthId);
                                 startActivity(intentNewWO);
                                 **/
                            }
                        });

                        //VIEW JOBS BUTTON
                        btnManageProfileInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: Manage Youth activity under construction", Toast.LENGTH_SHORT).show();

                                /**
                                 Intent intentViewJobs = new Intent(YouthShovelerProfileActivity.this, MangeYouthShovellerActivity.class);
                                 String youthId = user.getUserId();
                                 intentViewJobs.putExtra("USER_ID", youthId);
                                 startActivity(intentViewJobs);
                                 **/
                            }
                        });

                        //EDIT PASSWORD BUTTON
                        btnEditPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentEditPassword = new Intent(YouthShovelerProfileActivity.this, CreateWorkOrderActivity.class);
                                String youthId = user.getUserId();
                                intentEditPassword.putExtra("USER_ID", youthId);
                                startActivity(intentEditPassword);
                            }
                        });

                        //VIEW RATINGS BUTTON
                        btnViewRatings.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: View ratings activity under construction", Toast.LENGTH_SHORT).show();

                                /**
                                 Intent intentViewRatings = new Intent(YouthShovelerProfileActivity.this, ViewRatingsActivity.class);
                                 String youthId = user.getUserId();
                                 intentViewRatings.putExtra("USER_ID", youthId);
                                 startActivity(intentViewRatings);
                                 **/
                            }
                        });

                        //Logout BUTTON
                        btnLogout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentLogout = new Intent(YouthShovelerProfileActivity.this, MainActivity.class);
                                startActivity(intentLogout);
                            }
                        });
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








