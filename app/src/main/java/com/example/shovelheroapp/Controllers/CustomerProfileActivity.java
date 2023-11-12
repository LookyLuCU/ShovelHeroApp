package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    DatabaseReference shovelHeroDatabase;

    private String customerId;
    private TextView accountTypeTV;
    private TextView usernameTV;
    private TextView passwordTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private DatePicker birthdateDatePicker;
    private TextView emailTV;
    private TextView phoneTV;
    private int userId;

    //address list
    private ListView addressListView;
    private ArrayAdapter<String> addressAdapter;
    private List<String> addressList;


    //buttons
    Button btnOrderShoveling;
    Button btnManagePaymentInfo;
    Button btnAddAddress;
    Button btnEditPassword;
    Button btnViewMyRatings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);

        shovelHeroDatabase = FirebaseDatabase.getInstance().getReference("users");

        usernameTV = findViewById(R.id.tvUsername);
        passwordTV = findViewById(R.id.tvPassword);
        firstNameTV = findViewById(R.id.tvFirstName);
        lastNameTV = findViewById(R.id.tvLastname);
        emailTV = findViewById(R.id.tvEmail);
        phoneTV = findViewById(R.id.tvPhone);
        btnOrderShoveling = findViewById(R.id.btnOrderShoveling);

        addressListView = findViewById(R.id.listMyAddresses);
        addressList = new ArrayList<>();
        addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(addressAdapter);

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
        shovelHeroDatabase.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    //Address address = snapshot.getValue(Address.class);

                    if (user != null) {
                        //display customer profile data
                        usernameTV.setText(user.getUsername().toString());
                        passwordTV.setText(user.getPassword().toString());
                        firstNameTV.setText(user.getFirstName().toString());
                        lastNameTV.setText(user.getLastName().toString());
                        emailTV.setText(user.getEmail().toString());
                        phoneTV.setText(user.getPhoneNo().toString());

                        if(user.getAddresses() == null){
                            System.out.println("Please add your address to place an order");
                            Toast.makeText(CustomerProfileActivity.this, "Please add your address to place an order", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //**todo**Fix to print only address for current customer
                            displayAddresses(user.getAddresses());
                        }


                        /** 1st TRY CREATING ADRESS LIST
                        List<Address> addressList = new ArrayList<Address>();

                        for (Address customerAddress : addressList) {
                            System.out.print(customerAddress.getAddress() +
                                    ", " + customerAddress.getCity() +
                                    ", " + customerAddress.getProvince() +
                                    ", " + customerAddress.getPostalCode() +
                                    ", " + customerAddress.getCountry()
                            );
                         **/



                        btnOrderShoveling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentNewWO = new Intent(CustomerProfileActivity.this, CreateWorkOrderActivity.class);
                                String customerId = user.getUserId();
                                intentNewWO.putExtra("USER_ID", customerId);
                                startActivity(intentNewWO);
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

        private void displayAddresses(List<User.Address1> addresses) {
            addressList.clear();
            for (User.Address1 address : addresses) {
                String addressString = address.getAddress1() +
                        ", " + address.getCity1() +
                        ", " + address.getProvince1() +
                        ", " + address.getPostalCode1() +
                        ", " + address.getCountry1();
                addressList.add(addressString);
            }
            addressAdapter.notifyDataSetChanged();
        }
    }





