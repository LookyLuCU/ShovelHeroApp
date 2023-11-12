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

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private String accessibleOK;
    private String shovelAvaialable;

    private User currentUser;
    private Address currentAddress;


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
        drivewayCB = findViewById(R.id.cbDriveway);
        walkwayCB = findViewById(R.id.cbWalkway);
        sidewalkCB = findViewById(R.id.cbSidewalk);
        drivewaySquareFootageEditText = findViewById(R.id.etSqFoot);
        accessibleCB = findViewById(R.id.cbAccessible);
        shovelAvailableOnsiteCB = findViewById(R.id.cbShovelAvailable);

    }

    public void newAddress(View view) {

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
        int sqFootage = Integer.parseInt(drivewaySquareFootageEditText.getText().toString());

        if(accessibleCB.isChecked()){
            String accessibleOK = "Accessible";
        }

        if(shovelAvailableOnsiteCB.isChecked()){
            String shovelAvailable = "Available";
        }


        //ITEMS REQUESTED LIST
        if (drivewayCB.isChecked()) {
            itemsRequestedList.add("Driveway");
        }

        if (sidewalkCB.isChecked()) {
            itemsRequestedList.add("Sidewalk");
        }

        if (walkwayCB.isChecked()) {
            itemsRequestedList.add("Walkway");
        }


        String accountType = currentUser.getAccountType().toString();
        String currentCustomerId = currentUser.getUserId();

        //create new address
        Address newAddress = new Address(addressId, address, city, province, postalCode, country, addressNotes, sqFootage, accessibleOK, shovelAvaialable);
        //push to ShovelHeroDB & add ID (this does it automatically)
        addressReference.child(addressId).setValue(newAddress)


                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(CreateAddressActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS

                        switch (accountType) {
                            case "Youth Shoveller":
                                Intent intentYouth = new Intent(CreateAddressActivity.this, YouthShovelerProfileActivity.class);
                                String youthID = currentCustomerId;
                                intentYouth.putExtra("USER_ID", youthID);
                                startActivity(intentYouth);
                                break;
                            case "Customer":
                                Intent intentCustomer = new Intent(CreateAddressActivity.this, CustomerProfileActivity.class);
                                String customerId = currentCustomerId;
                                intentCustomer.putExtra("USER_ID", customerId);
                                startActivity(intentCustomer);
                                break;
                            case "Guardian":
                                Intent intentGuardian = new Intent(CreateAddressActivity.this, GuardianProfileActivity.class);
                                String guardianId = currentCustomerId;
                                intentGuardian.putExtra("USER_ID", guardianId);
                                startActivity(intentGuardian);
                            default:
                                Intent intent = new Intent(CreateAddressActivity.this, UserRegistrationActivity.class);
                                startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateAddressActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}