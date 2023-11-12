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

public class YouthShovelerProfileActivity extends AppCompatActivity {

    private static final String TAG = "YouthShovelerProfileActivity";
    private int youthId;
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

        //get username from registration or UserId from Login
        Intent intent = getIntent();
        if (intent != null) {
            int currentYouthId = intent.getIntExtra("USER_ID", youthId);
            if (currentYouthId != 0) {

                final int youthId = currentYouthId;

                //initialize ShovelHeroDB (Firebase)
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference youthReference = database.getReference("users/users");
                DatabaseReference addressReference = database.getReference("addresses");


                //Retrieve user data
                youthReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            Address address = snapshot.getValue(Address.class);

                            accountTypeTV = findViewById(R.id.tvAccountType);
                            accountTypeTV.setText(user.getAccountType().toString());

                            usernameTV = findViewById(R.id.tvUsername);
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

                            //try this first to print address
                            addressTV = findViewById(R.id.tvAddress);
                            addressTV.setText(address.getAddress() +
                                    ", " + address.getCity() +
                                    ", " + address.getProvince() +
                                    ", " + address.getPostalCode() +
                                    ", " + address.getCountry()
                            );

                            /**
                            addressTV = findViewById(R.id.tvAddress);
                            addressTV.setText(addressTV.getAddress().toString());

                            cityTV = findViewById(R.id.tvCity);
                            cityTV.setText(addressTV.getCity().toString());

                            provinceTV = findViewById(R.id.tvProvince);
                            provinceTV.setText(addressTV.getProvince().toString());

                            postalCodeTV = findViewById(R.id.tvPostalCode);
                            postalCodeTV.setText(addressTV.getPostalCode().toString());

                            countryTV = findViewById(R.id.tvCountry);
                            countryTV.setText(addressTV.getCountry().toString());


                            //need to fix to print only address for current customer
                            List<Address> addressList = new ArrayList<Address>();
                            for (Address address : addressList) {
                                System.out.print(address.getAddress() +
                                        ", " + address.getCity() +
                                        ", " + address.getProvince() +
                                        ", " + address.getPostalCode() +
                                        ", " + address.getCountry()
                                );

                             **/

                            //VIEW JOBS BUTTON
                            btnViewJobs.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intentNewWO = new Intent(YouthShovelerProfileActivity.this, GetWorkOrdersActivity.class);
                                    int youthId = user.getUserId();
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
                                    int customerId = user.getUserId();
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
                                    int youthId = user.getUserId();
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
                                    int youthId = user.getUserId();
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
                                    int youthId = user.getUserId();
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
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(YouthShovelerProfileActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}