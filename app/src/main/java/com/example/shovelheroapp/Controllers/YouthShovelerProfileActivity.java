package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class YouthShovelerProfileActivity extends AppCompatActivity {

    private static final String TAG = "YouthShovelerProfileActivity";

    //initialize ShovelHeroDB (Firebase)
    DatabaseReference shovelHeroDatabase;


    private TextView usernameTV;
    private TextView passwordTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView emailTV;
    private TextView phoneTV;
    private String userId;
    private String guardianId;

    //address list

    private ListView addressListView;
    private ArrayAdapter<String> addressAdapter;
    private List<String> addressList;
   // private TextView addressTV;

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
        btnViewJobs = findViewById(R.id.btnViewJobs);
        btnManagePaymentInfo = findViewById(R.id.btnManagePaymentInfo);
        btnManageProfileInfo = findViewById(R.id.btnManageProfileInfo);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnViewRatings = findViewById(R.id.btnViewRatings);
        btnLogout = findViewById(R.id.btnLogout);
        //addressTV = findViewById(R.id.tvAddress);

        //ADDRESS LIST
        addressListView = findViewById(R.id.listMyAddresses);
        addressList = new ArrayList<>();
        addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(addressAdapter);

        //get Username from registration page or or UserID from Login
        Intent intent = getIntent();
        if (intent.hasExtra("USER_ID")) {
            String currentYouthId = intent.getStringExtra("USER_ID");
            if (currentYouthId != null) {
                final String youthId = currentYouthId;

                //get and display user data
                retrieveYouthProfile(youthId);
            } else {
                Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: YouthID is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: No intent passed", Toast.LENGTH_SHORT).show();
        }
    }



    private void retrieveYouthProfile(String youthId) {
        shovelHeroDatabase.child(youthId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        //display user profile info
                        usernameTV.setText("Username: " + user.getUsername());
                        passwordTV.setText("Password: " + user.getPassword());
                        firstNameTV.setText("First Name: " + user.getFirstName());
                        lastNameTV.setText("Last Name: " + user.getLastName());
                        emailTV.setText("Email: " + user.getEmail());
                        phoneTV.setText("Phone Number: " + user.getPhoneNo());

                        if(user.getAddresses() == null){
                            System.out.println("Please add your address to view local job listings");
                            Toast.makeText(YouthShovelerProfileActivity.this, "Please add your address to view local job listings", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //ANOTHER TRY AT LISTING ADDRESS
                            displayAddresses(user.getAddresses());
                        }

                        //VIEW JOBS BUTTON
                        btnViewJobs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /**
                                Intent intentViewYouthJobs = new Intent(YouthShovelerProfileActivity.this, GetWorkOrdersActivity.class);
                                String youthId = user.getUserId();
                                intentViewYouthJobs.putExtra("USER_ID", youthId);
                                startActivity(intentViewYouthJobs);
                                 **/
                            }
                        });


                        //MANAGE PAYMENT BUTTON
                        btnManagePaymentInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: Manage Payment activity under construction", Toast.LENGTH_SHORT).show();
                                /**
                                 Intent intentManageYouthPayment = new Intent(YouthShovelerProfileActivity.this, ManagePaymentActivity.class);
                                 String youthId = user.getUserId();
                                 intentManageYouthPayment.putExtra("USER_ID", youthId);
                                 startActivity(intentManageYouthPayment);
                                 **/
                            }
                        });

                        //VIEW JOBS BUTTON
                        btnManageProfileInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(YouthShovelerProfileActivity.this, "Temp msg: Manage Youth activity under construction", Toast.LENGTH_SHORT).show();

                                /**
                                 Intent intentManageYouthProfile = new Intent(YouthShovelerProfileActivity.this, MangeYouthShovellerActivity.class);
                                 String youthId = user.getUserId();
                                 intentManageYouthProfile.putExtra("USER_ID", youthId);
                                 startActivity(intentManageYouthProfile);
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
    private void displayAddresses(List<User.Address> addresses) {
        addressList.clear();
            for (User.Address address : addresses) {
                String addressString = address.getAddress() +
                        ", " + address.getCity() +
                        ", " + address.getProvince() +
                        ", " + address.getPostalCode() +
                        ", " + address.getCountry();
                addressList.add(addressString);
            }
            addressAdapter.notifyDataSetChanged();
        }
    }

