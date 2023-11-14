package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.List;

public class CustomerProfileActivity extends AppCompatActivity {

    private static final String TAG = "CustomerProfileActivity";

    //initialize ShovelHeroDB (Firebase)
    DatabaseReference userTable;


    private TextView usernameTV;
    private TextView passwordTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private DatePicker birthdateDatePicker;
    private TextView emailTV;
    private TextView phoneTV;
    private int userId;

    //address list
    //private ListView addressListView;
    //private ArrayAdapter<String> addressAdapter;
    //private List<String> addressList;

    //AddressList setup
    private RecyclerView addressRecyclerView;
    private DatabaseReference userRef;
    AddressAdapter adapter;
    List<Address> addressList;


    //buttons
    Button btnAddAddress;
    Button btnOrderShoveling;
    Button btnManagePaymentInfo;

    Button btnEditPassword;
    Button btnViewMyRatings;
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);

        usernameTV = findViewById(R.id.tvUsername);
        passwordTV = findViewById(R.id.tvPassword);
        firstNameTV = findViewById(R.id.tvFirstName);
        lastNameTV = findViewById(R.id.tvLastname);
        emailTV = findViewById(R.id.tvEmail);
        phoneTV = findViewById(R.id.tvPhone);

        btnAddAddress = findViewById(R.id.btnAddAddress);
        addressRecyclerView = findViewById(R.id.addressRecyclerView);

        btnOrderShoveling = findViewById(R.id.btnOrderShoveling);
        btnManagePaymentInfo = findViewById(R.id.btnManagePaymentInfo);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnViewMyRatings = findViewById(R.id.btnViewMyRatings);
        btnLogout = findViewById(R.id.btnLogout);

        /**
        addressListView = findViewById(R.id.listMyAddresses);
        addressList = new ArrayList<>();
        addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(addressAdapter);
         **/

        //get username from registration or UserId from Login
        Intent intent = getIntent();
        if (intent != null) {
            String currentCustomerId = intent.getStringExtra("USER_ID");
            if (currentCustomerId != null) {

                final String customerId = currentCustomerId;

                //get and display user data
                retrieveCustomerProfile(customerId);
            }
            else {
                Toast.makeText(CustomerProfileActivity.this, "Temp msg: CustomerID is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CustomerProfileActivity.this, "Temp msg: No intent passed", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveCustomerProfile(String customerId) {
        userTable.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        //display customer profile data
                        usernameTV.setText(user.getUsername());
                        passwordTV.setText(user.getPassword());
                        firstNameTV.setText(user.getFirstName());
                        lastNameTV.setText(user.getLastName());
                        emailTV.setText(user.getEmail());
                        phoneTV.setText(user.getPhoneNo());

                        //ADDRESS LISTING
                        userTable = FirebaseDatabase.getInstance().getReference("users").child(user.getUserId());

                        addressList = new ArrayList<>();
                        adapter = new AddressAdapter(addressList);

                       // addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        addressRecyclerView.setAdapter(adapter);

                        readAddressesFromFirebase();


                        /**
                        if(user.getAddresses() == null){
                            System.out.println("Please add your address to place an order");
                            Toast.makeText(CustomerProfileActivity.this, "Please add your address to place an order", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            displayAddresses(user.getAddresses());
                        }
                         **/


                            //ORDER SHOVELLING BUTTON
                        btnOrderShoveling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentNewWO = new Intent(CustomerProfileActivity.this, CreateWorkOrderActivity.class);
                                String customerId = user.getUserId();
                                intentNewWO.putExtra("USER_ID", customerId);
                                startActivity(intentNewWO);
                            }
                        });

                        //MANAGE PAYMENT BUTTON
                        btnManagePaymentInfo.setOnClickListener(new View.OnClickListener() {
                            @Override

                            public void onClick(View view) {

                                Intent intentManagePayment = new Intent(CustomerProfileActivity.this, ManagePaymentActivity.class);
                                String customerId = user.getUserId();
                                intentManagePayment.putExtra("USER_ID", customerId);
                                startActivity(intentManagePayment);

                            }
                        });

                        //ADD ADDRESS BUTTON
                        btnAddAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentNewAddress = new Intent(CustomerProfileActivity.this, CreateAddressActivity.class);
                                String customerId = user.getUserId();
                                intentNewAddress.putExtra("USER_ID", customerId);
                                startActivity(intentNewAddress);
                            }
                        });

                        //EDIT PASSWORD BUTTON
                        btnEditPassword.setOnClickListener(new View.OnClickListener() {
                            @Override

                            public void onClick(View view) {
                                 Intent intentEditPassword = new Intent(CustomerProfileActivity.this, EditPasswordActivity.class);
                                 String customerId = user.getUserId();
                                 intentEditPassword.putExtra("USER_ID", customerId);
                                 startActivity(intentEditPassword);
                            }
                        });

                        //VIEW RATINGS BUTTON
                        btnViewMyRatings.setOnClickListener(new View.OnClickListener() {
                            @Override

                            public void onClick(View view) {
                                /**
                                 Intent intentViewRatings = new Intent(CustomerProfileActivity.this, ViewRatingsActivity.class);
                                 String customerId = user.getUserId();
                                 intentViewRatings.putExtra("USER_ID", customerId);
                                 startActivity(intentViewRatings);
                                 **/
                            }
                        });

                        //Logout BUTTON
                        btnLogout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentLogout = new Intent(CustomerProfileActivity.this, MainActivity.class);
                                startActivity(intentLogout);
                            }
                        });

                    } else {
                        //handle no user data
                    }
                } else {
                    //handle userid does not exist
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerProfileActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
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
     **/

        private void readAddressesFromFirebase() {
        userTable.child("addresses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addressList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Address address = snapshot.getValue(Address.class);
                    if(address != null){
                        addressList.add(address);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }
    }
