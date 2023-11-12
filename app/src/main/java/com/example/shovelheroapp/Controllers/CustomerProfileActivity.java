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

import java.util.ArrayList;
import java.util.List;

public class CustomerProfileActivity extends AppCompatActivity {


    private static final String TAG = "CustomerProfileActivity";
    private int customerId;
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
    private List<Address> addressList;

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

        //get username from registration or UserId from Login
        Intent intent = getIntent();
        if (intent != null) {
            int currentCustomerId = intent.getIntExtra("USER_ID", customerId);
            if (currentCustomerId != 0) {

                final int customerId = currentCustomerId;

                //initialize ShovelHeroDB (Firebase)
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference customerReference = database.getReference("users");
                DatabaseReference addressReference = database.getReference("addresses");


                //Retrieve user data
                customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);

                            accountTypeTV = findViewById(R.id.tvAccountType);
                            accountTypeTV.setText(user.getAccountType().toString());

                            usernameTV = findViewById(R.id.tvUserName);
                            usernameTV.setText(user.getUsername().toString());

                            passwordTV = findViewById(R.id.tvPassword);
                            passwordTV.setText(user.getPassword().toString());

                            firstNameTV = findViewById(R.id.tvFirstName);
                            firstNameTV.setText(user.getFirstName().toString());

                            lastNameTV = findViewById(R.id.tvLastname);
                            lastNameTV.setText(user.getLastName().toString());

                            emailTV = findViewById(R.id.tvEmail);
                            emailTV.setText(user.getEmail().toString());

                            phoneTV = findViewById(R.id.tvPhone);
                            phoneTV.setText(user.getPhoneNo().toString());

                            //need to fix to print only address for current customer

                            List<Address> addressList = new ArrayList<Address>();
                            for (Address address : addressList) {
                                System.out.print(address.getAddress() +
                                        ", " + address.getCity() +
                                        ", " + address.getProvince() +
                                        ", " + address.getPostalCode() +
                                        ", " + address.getCountry()
                                );

                                btnOrderShoveling.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intentNewWO = new Intent(CustomerProfileActivity.this, CreateWorkOrderActivity.class);
                                        int customerId = user.getUserId();
                                        intentNewWO.putExtra("USER_ID", customerId);
                                        startActivity(intentNewWO);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CustomerProfileActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}