package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//THIS IS ANOTHER TEST

public class CustomerProfileActivity extends AppCompatActivity {

    private static final String TAG = "CustomerProfileActivity";

    //initialize ShovelHeroDB userTable(Firebase)
    DatabaseReference userTable;


    private TextView usernameTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private DatePicker birthdateDatePicker;
    private TextView emailTV;
    private TextView phoneTV;
    private Spinner addressSpinner;
    private User user;
    private String userId;



    //buttons
    Button btnAddAddress;
    Button btnOrderShoveling;
    Button btnEditProfile;
    Button btnManagePaymentInfo;
    Button btnEditPassword;
    Button btnViewMyRatings;
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);

        //Instantiate userTable to current listing
        userTable = FirebaseDatabase.getInstance().getReference("users");

        usernameTV = findViewById(R.id.tvUsername);
        firstNameTV = findViewById(R.id.tvFirstName);
        lastNameTV = findViewById(R.id.tvLastname);
        emailTV = findViewById(R.id.tvEmail);
        phoneTV = findViewById(R.id.tvPhone);
        addressSpinner = findViewById(R.id.spinnerAddress);

        btnOrderShoveling = findViewById(R.id.btnOrderShoveling);
        btnManagePaymentInfo = findViewById(R.id.btnManagePaymentInfo);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnEditProfile = findViewById(R.id.btnEditUserInfo);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnViewMyRatings = findViewById(R.id.btnViewMyRatings);


        //GET USERID FROM LOGIN OR REGISTRATION
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
            if (userId != null) {
                System.out.println("customer ID recieved: " + userId);  //WORKING
                retrieveCustomerProfileData(userId);
            }
        }


        //Navigation Bar Activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_workorders) {
                startActivity(new Intent(CustomerProfileActivity.this, ListAllOpenWorkOrdersActivity.class));
                return true;
            } else if (itemId == R.id.menu_orderhistory) {
                startActivity(new Intent(CustomerProfileActivity.this, OrderHistoryActivity.class));
                return true;
            } else if (itemId == R.id.menu_logout) {
                startActivity(new Intent(CustomerProfileActivity.this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void retrieveCustomerProfileData(String userId) {
        System.out.println("customer ID recieved to retrieve cx profile: " + userId);  //WORKING

        userTable.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);

                    if (user != null) {
                        //display user profile data
                        usernameTV.setText("Username: " + user.getUsername());
                        firstNameTV.setText("Name: " + user.getFirstName());
                        lastNameTV.setText(user.getLastName());
                        emailTV.setText("Email: " + user.getEmail());
                        phoneTV.setText("Phone Number: " + user.getPhoneNo());

                        System.out.println("User data loaded: " + user.getUsername());
                        System.out.println("Sending userid to read addresses: " + user);

                        // Load profile Image
                        String profileImageUrl = user.getProfilePictureUrl();
                        ImageView profileImageView = findViewById(R.id.imgProfilePicture);
                        if(profileImageUrl != null && !profileImageUrl.isEmpty()){
                            Glide.with(CustomerProfileActivity.this)
                                    .load(profileImageUrl).into(profileImageView);
                        }

                        readAddressesFromFirebase(user);


                        //*******
                        //CUSTOMER BUTTONS
                        //*******

                        //ORDER SHOVELLING BUTTON
                        btnOrderShoveling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v){
                                Address selectedAddress = (Address) addressSpinner.getSelectedItem();
                                createWorkOrder(user.getUserId(), selectedAddress);
                            }
                        });

                        btnEditProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(CustomerProfileActivity.this, "Temp msg: Manage Youth activity under construction", Toast.LENGTH_SHORT).show();
                                Intent intentManageCustomerProfile = new Intent(CustomerProfileActivity.this, EditUserProfileActivity.class);
                                String youthId = user.getUserId();
                                intentManageCustomerProfile.putExtra("USER_ID", youthId);
                                startActivity(intentManageCustomerProfile);
                            }
                        });

                        //MANAGE PAYMENT BUTTON
                        btnManagePaymentInfo.setOnClickListener(new View.OnClickListener() {
                            @Override

                            public void onClick(View view) {
                                /**
                                 Intent intentManagePayment = new Intent(CustomerProfileActivity.this, ManagePayemntActivity.class);
                                 String customerId = user.getUserId();
                                 intentManagePayment.putExtra("USER_ID", customerId);
                                 startActivity(intentManagePayment);
                                 **/
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
                ArrayAdapter<Address> addressAdapter = new ArrayAdapter<>(CustomerProfileActivity.this, android.R.layout.simple_spinner_item, addresses);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addressSpinner.setAdapter(addressAdapter);

                // Enable or disable the "Create Work Order" button based on the presence of addresses
                btnOrderShoveling.setEnabled(!addresses.isEmpty());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    public void createWorkOrder(String userId, Address address) {
        if (address.getAddress() == null) {
            Toast.makeText(this, "Please select a valid address from your list", Toast.LENGTH_SHORT).show();
        } else {

            //create WO item in firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference workOrderReference = database.getReference("workorders");

            //work order elements
            String workOrderID = workOrderReference.push().getKey();
            Date requestDate = Calendar.getInstance().getTime();
            String status = "Started";
            int squareFootage = address.getDrivewaySquareFootage();
            List<String> itemsRequested = null;
            String addressId = address.getAddressId();

            //create new work order
            WorkOrder newWO = new WorkOrder(workOrderID, requestDate, status, squareFootage, itemsRequested, userId, addressId);

            workOrderReference.child(workOrderID).setValue(newWO)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CustomerProfileActivity.this, "New Shovelling Request Created", Toast.LENGTH_SHORT).show();

                            Intent intentCreateWO = new Intent(CustomerProfileActivity.this, CreateWorkOrderActivity.class);
                            String wOID = workOrderID;
                            intentCreateWO.putExtra("WORK_ORDER_ID", wOID);
                            startActivity(intentCreateWO);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CustomerProfileActivity.this, "Could not create Shovelling Job", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}



