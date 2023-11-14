package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class GuardianProfileActivity extends AppCompatActivity {
    private static final String TAG = "GuardianProfileActivity";

    //initialize ShovelHeroDB (Firebase)
    DatabaseReference userTable;


    private TextView usernameTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView emailTV;
    private TextView phoneTV;

    private User currentUser;
    private String currentGuardianId;

    //address list

    private ListView addressListView;
    private ArrayAdapter<String> addressAdapter;
    private List<String> addressList;
    private RecyclerView addressRecyclerView;
    AddressAdapter adapter;

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
        addressTV = findViewById(R.id.tvAddress);

        /**
        //instantiate addressList + adapter
        btnAddAddress = findViewById(R.id.btnAddAddress);
        //addressRecyclerView = findViewById(R.id.addressRecyclerView);
        addressList = new ArrayList<>();
        //adapter = new AddressAdapter(addressList);
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressRecyclerView.setAdapter(adapter);
         **/

        btnViewRatings = findViewById(R.id.btnLinkedYouths);
        btnViewJobs = findViewById(R.id.btnManageYouthInfo);
        btnManagePaymentInfo = findViewById(R.id.btnManagePaymentInfo);
        btnManageProfileInfo = findViewById(R.id.btnManageProfileInfo);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnViewRatings = findViewById(R.id.btnViewYouthRatings);

        btnLogout = findViewById(R.id.btnLogout);
        //addressTV = findViewById(R.id.tvAddress);

        /**
        //ADDRESS LIST
        addressListView = findViewById(R.id.listMyAddresses);
        addressList = new ArrayList<>();
        addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(addressAdapter);
         **/

        //get Username from registration page or or UserID from Login
        //GET USERID FROM LOGIN OR REGISTRATION
        Intent intent = getIntent();
        if (intent != null) {
            String currentUserId = intent.getStringExtra("USER_ID");
            if (currentUserId != null) {
                retrieveGuardianProfile(currentUserId);
            }
        }
    }


    private void retrieveGuardianProfile(String currentUserId) {
        userTable.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
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


                        //readAddressesFromFirebase();
                        //retrieveAddressesFromFirebase();

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
                        btnViewJobs.setOnClickListener(new View.OnClickListener() {
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

                        //Logout BUTTON
                        btnLogout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentLogout = new Intent(GuardianProfileActivity.this, MainActivity.class);
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

    private void displayAddresses(List<Address> addresses) {
        addressList.clear();
        for (Address address : addresses) {
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
