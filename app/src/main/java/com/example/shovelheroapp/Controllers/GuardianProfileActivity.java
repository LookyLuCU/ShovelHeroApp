package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuardianProfileActivity extends AppCompatActivity {
    private static final String TAG = "GuardianProfileActivity";

    //initialize ShovelHeroDB (Firebase)
    DatabaseReference userTable;


    private TextView usernameTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView emailTV;
    private TextView phoneTV;
    private Spinner addressSpinner;

    private User currentUser;
    private String userId;



    //buttons
    Button btnLinkedYouths;
    Button btnViewJobs;
    Button btnManagePaymentInfo;
    Button btnManageProfileInfo;
    Button btnAddAddress;
    Button btnEditPassword;
    Button btnViewRatings;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_guardian);

        userTable = FirebaseDatabase.getInstance().getReference("users");

        usernameTV = findViewById(R.id.tvUsername);
        firstNameTV = findViewById(R.id.tvFirstName);
        lastNameTV = findViewById(R.id.tvLastname);
        emailTV = findViewById(R.id.tvEmail);
        phoneTV = findViewById(R.id.tvPhone);
        addressSpinner = findViewById(R.id.spinnerAddress);

        btnLinkedYouths = findViewById(R.id.btnLinkedYouths);
        btnViewRatings = findViewById(R.id.btnLinkedYouths);
        btnViewJobs = findViewById(R.id.btnManageYouthInfo);
        btnManagePaymentInfo = findViewById(R.id.btnManagePaymentInfo);
        btnManageProfileInfo = findViewById(R.id.btnManageProfileInfo);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnViewRatings = findViewById(R.id.btnViewYouthRatings);


        //get Username from registration page or or UserID from Login
        //GET USERID FROM LOGIN OR REGISTRATION
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
            if (userId != null) {
                retrieveGuardianProfile(userId);
            }
        }

        //Navigation Bar Activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewGuardian);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.menu_workorders) {
                startActivity(new Intent(GuardianProfileActivity.this, ListAllOpenWorkOrdersActivity.class));
                return true;
            } else if (itemId == R.id.menu_orderhistory) {
                startActivity(new Intent(GuardianProfileActivity.this, OrderHistoryActivity.class));
                return true;
            } else if (itemId == R.id.menu_logout) {
                startActivity(new Intent(GuardianProfileActivity.this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }


    private void retrieveGuardianProfile(String userId) {
        userTable.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        //display user profile info
                        usernameTV.setText("Username: " + user.getUsername());
                        firstNameTV.setText("First Name: " + user.getFirstName());
                        lastNameTV.setText("Last Name: " + user.getLastName());
                        emailTV.setText("Email: " + user.getEmail());
                        phoneTV.setText("Phone Number: " + user.getPhoneNo());


                        // Load profile Image
                        String profileImageUrl = user.getProfilePictureUrl();
                        ImageView profileImageView = findViewById(R.id.imgProfilePicture);
                        if(profileImageUrl != null && !profileImageUrl.isEmpty()){
                            Glide.with(GuardianProfileActivity.this)
                                    .load(profileImageUrl).into(profileImageView);
                        }

                        readAddressesFromFirebase(user);

                        if(user.getAddresses() == null){
                            System.out.println("Please add your address");
                            Toast.makeText(GuardianProfileActivity.this, "Please add your address", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //ANOTHER TRY AT LISTING ADDRESS
                            //displayAddresses(user.getAddresses());
                        }



                        //BUTTON: ADD A LINKED YOUTH ACCOUNT (by username)

                        //IMAGE: ADD ID - once dropped by user, to notify app team for verification (only app team can verify)

                        //VIEW MY YOUTHS BUTTON - **todo**an Array list like addresses?
                        btnLinkedYouths.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /**
                                Intent intentViewYouthJobs = new Intent(GuardianProfileActivity.this, YouthShovelerProfileActivity.class);
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
                                Toast.makeText(GuardianProfileActivity.this, "Temp msg: Manage Payment activity under construction", Toast.LENGTH_SHORT).show();
                                 Intent intentManageYouthPayment = new Intent(GuardianProfileActivity.this, ManagePaymentActivity.class);
                                 String youthId = user.getUserId();
                                 intentManageYouthPayment.putExtra("USER_ID", youthId);
                                 startActivity(intentManageYouthPayment);
                            }
                        });

                        //MANAGE PROFILE BUTTON
                        btnManageProfileInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(GuardianProfileActivity.this, "Temp msg: Manage user profile under construction", Toast.LENGTH_SHORT).show();
                                 Intent intentManageYouthProfile = new Intent(GuardianProfileActivity.this, EditUserProfileActivity.class);
                                 String youthId = user.getUserId();
                                 intentManageYouthProfile.putExtra("USER_ID", youthId);
                                 startActivity(intentManageYouthProfile);
                            }
                        });

                        //ADD ADDRESS BUTTON
                        btnAddAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentNewAddress = new Intent(GuardianProfileActivity.this, CreateAddressActivity.class);
                                String customerId = user.getUserId();
                                intentNewAddress.putExtra("USER_ID", customerId);
                                startActivity(intentNewAddress);
                            }
                        });

                        //EDIT PASSWORD BUTTON
                        btnEditPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentEditPassword = new Intent(GuardianProfileActivity.this, EditPasswordActivity.class);
                                String youthId = user.getUserId();
                                intentEditPassword.putExtra("USER_ID", youthId);
                                startActivity(intentEditPassword);
                            }
                        });

                        //VIEW RATINGS BUTTON
                        btnViewRatings.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(GuardianProfileActivity.this, "Temp msg: View ratings activity under construction", Toast.LENGTH_SHORT).show();

                                /**
                                 Intent intentViewRatings = new Intent(YouthShovelerProfileActivity.this, ViewRatingsActivity.class);
                                 String youthId = user.getUserId();
                                 intentViewRatings.putExtra("USER_ID", youthId);
                                 startActivity(intentViewRatings);
                                 **/
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

    private void readAddressesFromFirebase(User user) {
        System.out.println("userid received by read database: " + user);

        userTable.child(user.getUserId()).child("addresses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the addresses field in the User class
                user.setAddresses(new HashMap<String, Address>());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //manually deserialize the HashMap
                    Map<String, Object> addressMap = (Map<String, Object>) snapshot.getValue();

                    // retrieve values from the Hashmap
                    String addressId = (String) addressMap.get("addressId");
                    String address = (String) addressMap.get("address");
                    String city = (String) addressMap.get("city");
                    String province = (String) addressMap.get("province");
                    String postalCode = (String) addressMap.get("postalCode");
                    String country = (String) addressMap.get("country");
                    String addressNotes = (String) addressMap.get("addressNotes");
                    int drivewaySquareFootage = ((Long) addressMap.get("drivewaySquareFootage")).intValue();
                    String accessible = (String) addressMap.get("accessible");
                    String shovelAvailable = (String) addressMap.get("shovelAvailable");

                    // Create new Address object
                    Address addressObject = new Address(addressId, address, city, province, postalCode, country, addressNotes, drivewaySquareFootage, accessible, shovelAvailable);

                    // Add the Address object to the addresses HashMap in User model
                    user.addAddress(addressId, addressObject);
                }

                // Retrieve list of addresses from Hashmap
                List<Address> addresses = new ArrayList<>(user.getAddresses().values());

                // Update the Spinner with addresses
                ArrayAdapter<Address> addressAdapter = new ArrayAdapter<>(GuardianProfileActivity.this, android.R.layout.simple_spinner_item, addresses);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addressSpinner.setAdapter(addressAdapter);

                // Enable or disable the "Create Work Order" button based on the presence of addresses
                //btnOrderShoveling.setEnabled(!addresses.isEmpty());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
