package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shovelheroapp.Controllers.AsyncTasks.AddToAddressDatabaseTask;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateAddressActivity extends AppCompatActivity {
    private static final String TAG = "CreateAddressActivity";

    private String addressId;
    private ImageView customerAddressImage;
    private EditText addressEditText;
    private EditText cityEditText;
    private EditText provinceEditText;
    private EditText postalCodeEditText;
    private Spinner countrySpinner;
    private EditText addressNotesEditText;
    private List<String> itemsRequestedList;
    private CheckBox drivewayCB;
    private CheckBox walkwayCB;
    private CheckBox sidewalkCB;
    private EditText drivewaySquareFootageEditText;
    private CheckBox accessibleCB;
    private CheckBox shovelAvailableOnsiteCB;
    private String accessible;
    private String shovelAvailable;

    private User currentUser;
    private String customerId;
    private User.Address1 currentAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_create);

        //get text input fields
        customerAddressImage = findViewById(R.id.imgPropertyImage);
        addressEditText = findViewById(R.id.etAddress);
        cityEditText = findViewById(R.id.etCity);
        provinceEditText = findViewById(R.id.etProvince);
        postalCodeEditText = findViewById(R.id.etPostalCode);
        countrySpinner = findViewById(R.id.spCountry);
        addressNotesEditText = findViewById(R.id.etAddressNotes);
        drivewaySquareFootageEditText = findViewById(R.id.etSqFoot);
        drivewayCB = findViewById(R.id.cbDriveway);
        walkwayCB = findViewById(R.id.cbWalkway);
        sidewalkCB = findViewById(R.id.cbSidewalk);
        accessibleCB = findViewById(R.id.cbAccessible);
        shovelAvailableOnsiteCB = findViewById(R.id.cbShovelAvailable);

        //get username from customer profile intent
        Intent intent = getIntent();
        String currentCustomerId = intent.getStringExtra("USER_ID");
        // Get user ID from the intent
        customerId = getIntent().getStringExtra("USER_ID");
        if (customerId == null) {
            Toast.makeText(this, "Temp msg: CustomerID is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void createAddress(View view) {

        //initialize itemsRequestedList
        itemsRequestedList = new ArrayList<>();

        //intialize ShovelHeroDB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference addressReference = database.getReference("addresses");

        ///TO ADD THE PIC LATER

        addressId = addressReference.push().getKey();
        String address = addressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String province = provinceEditText.getText().toString();
        String postalCode = postalCodeEditText.getText().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String addressNotes = addressNotesEditText.getText().toString();
        String sqFootageStr = drivewaySquareFootageEditText.getText().toString();
        int sqFootage = sqFootageStr.isEmpty() ? 0 : Integer.parseInt(sqFootageStr);

        if (accessibleCB.isChecked()) {
            accessible = "Accessible";
        } else {
            accessible = null;
        }

        if (shovelAvailableOnsiteCB.isChecked()) {
            shovelAvailable = "Available";
        } else {
            shovelAvailable = null;
        }

        //ITEMS REQUESTED LIST
        if (drivewayCB.isChecked()) {
            itemsRequestedList.add("Driveway");
        } else {
            itemsRequestedList.add("NO Driveway Please");
        }

        if (sidewalkCB.isChecked()) {
            itemsRequestedList.add("Sidewalk");
        } else {
            itemsRequestedList.add("NO Sidewalk Please");
        }

        if (walkwayCB.isChecked()) {
            itemsRequestedList.add("Walkway");
        } else {
            itemsRequestedList.add("NO Walkway Please");
        }

        //create new address
        //Address newAddress = new Address(addressId, address, city, province, postalCode, country, addressNotes, sqFootage, accessible, shovelAvailable);
        User.Address1 newAddress = new User.Address1(addressId, address, city, province, postalCode, country, addressNotes, sqFootage, accessible, shovelAvailable);

        //push to ShovelHeroDB & add ID (this does it automatically)
        //addressReference.child(addressId).setValue(newAddress)
        new AddToAddressDatabaseTask().execute(newAddress);

        if(newAddress.getAddress1() != null) {
            Toast.makeText(CreateAddressActivity.this, "Address created successfully", Toast.LENGTH_SHORT).show();

            //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS

            saveAndReturnToProfile(customerId);
        }

        /**
            if(accountType != null) {
                switch (accountType) {
                    case "Youth Shoveller":
                        Intent intentYouth = new Intent(CreateAddressActivity.this, YouthShovelerProfileActivity.class);
                        String youthID = currentUser.getUserId();
                        intentYouth.putExtra("USER_ID", youthID);
                        startActivity(intentYouth);
                        break;
                    case "Customer":
                        Intent intentCustomer = new Intent(CreateAddressActivity.this, CustomerProfileActivity.class);
                        String customerId = currentUser.getUserId();
                        intentCustomer.putExtra("USER_ID", customerId);
                        startActivity(intentCustomer);
                        break;
                    case "Guardian":
                        Intent intentGuardian = new Intent(CreateAddressActivity.this, GuardianProfileActivity.class);
                        String guardianId = currentUser.getUserId();
                        intentGuardian.putExtra("USER_ID", guardianId);
                        startActivity(intentGuardian);
                        break;
                    default:
                        Intent intent = new Intent(CreateAddressActivity.this, UserRegistrationActivity.class);
                        startActivity(intent);
                        break;
                }
            } else{
                System.out.println("Account Type is Null");
            }
        }
         **/

        /**
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(CreateAddressActivity.this, "Address created successfully", Toast.LENGTH_SHORT).show();

                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS

                        if(accountType != null) {
                            switch (accountType) {
                                case "Youth Shoveller":
                                    Intent intentYouth = new Intent(CreateAddressActivity.this, YouthShovelerProfileActivity.class);
                                    String youthID = currentUser.getUserId();
                                    intentYouth.putExtra("USER_ID", youthID);
                                    startActivity(intentYouth);
                                    break;
                                case "Customer":
                                    Intent intentCustomer = new Intent(CreateAddressActivity.this, CustomerProfileActivity.class);
                                    String customerId = currentUser.getUserId();
                                    intentCustomer.putExtra("USER_ID", customerId);
                                    startActivity(intentCustomer);
                                    break;
                                case "Guardian":
                                    Intent intentGuardian = new Intent(CreateAddressActivity.this, GuardianProfileActivity.class);
                                    String guardianId = currentUser.getUserId();
                                    intentGuardian.putExtra("USER_ID", guardianId);
                                    startActivity(intentGuardian);
                                    break;
                                default:
                                    Intent intent = new Intent(CreateAddressActivity.this, UserRegistrationActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                        } else{
                            System.out.println("Account Type is Null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CreateAddressActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
         **/
    }

    private void saveAndReturnToProfile(String customerId){
        DatabaseReference userTable = FirebaseDatabase.getInstance().getReference("users").child(customerId);
        userTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                String accountType = currentUser.getAccountType();

                //**todo** ADD ADULT SHOEVLLER AND GUARDIAN
                if(accountType != null) {
                    switch (accountType) {
                        case "Youth Shoveller":
                            Intent intentYouth = new Intent(CreateAddressActivity.this, YouthShovelerProfileActivity.class);
                            String youthID = currentUser.getUserId();
                            intentYouth.putExtra("USER_ID", youthID);
                            startActivity(intentYouth);
                            break;
                        case "Customer":
                            Intent intentCustomer = new Intent(CreateAddressActivity.this, CustomerProfileActivity.class);
                            String customerId = currentUser.getUserId();
                            intentCustomer.putExtra("USER_ID", customerId);
                            startActivity(intentCustomer);
                            break;
                        case "Guardian":
                            Intent intentGuardian = new Intent(CreateAddressActivity.this, GuardianProfileActivity.class);
                            String guardianId = currentUser.getUserId();
                            intentGuardian.putExtra("USER_ID", guardianId);
                            startActivity(intentGuardian);
                            break;
                        default:
                            Intent intent = new Intent(CreateAddressActivity.this, UserRegistrationActivity.class);
                            startActivity(intent);
                            break;
                    }
                } else{
                    System.out.println("Account Type is Null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


