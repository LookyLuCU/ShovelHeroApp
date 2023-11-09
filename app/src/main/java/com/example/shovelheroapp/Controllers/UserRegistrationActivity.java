package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistrationActivity";
    private int userId;
    private Spinner spinnerAccountType;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private DatePicker birthdateDatePicker;
    private EditText emailEditText;
    private EditText phoneEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Get User Input from activity_user_registration
        //select account type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerAccountType = findViewById(R.id.spAccountType);

        //get text input fields
        usernameEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPassword);
        firstNameEditText = findViewById(R.id.etFirstname);
        lastNameEditText = findViewById(R.id.etLastname);
        birthdateDatePicker = findViewById(R.id.cvBirthdate);
        emailEditText = findViewById(R.id.etEmail);
        phoneEditText = findViewById(R.id.etPhone);
    }

    public void createUser(View view) {

        //intialize ShovelHeroDB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference("users");

        // save fields to proper input type
        String accountType = spinnerAccountType.getSelectedItem().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        CalendarView birthdate = birthdateDatePicker.findViewById(R.id.cvBirthdate);
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        //create new user
        User newUser = new User(userId, accountType, username, password, firstName, lastName, birthdate, email, phone);

        //push to ShovelHeroDB & add ID (this does it automatically)
        userReference.child("users").push().setValue(newUser)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(UserRegistrationActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS

                        switch (accountType) {
                            case "Youth Shoveller":
                                Intent intentYouth = new Intent(UserRegistrationActivity.this, YouthShovelerProfileActivity.class);
                                int youthID = userId;
                                intentYouth.putExtra("USER_ID", youthID);
                                startActivity(intentYouth);
                                break;
                            case "Customer":
                                Intent intentCustomer = new Intent(UserRegistrationActivity.this, CustomerProfileActivity.class);
                                int customerId = userId;
                                intentCustomer.putExtra("USER_ID", customerId);
                                startActivity(intentCustomer);
                                break;
                            case "Guardian":
                                Intent intentGuardian = new Intent(UserRegistrationActivity.this, GuardianProfileActivity.class);
                                int guardianId = userId;
                                intentGuardian.putExtra("USER_ID", guardianId);
                                startActivity(intentGuardian);
                            default:
                                Intent intent = new Intent(UserRegistrationActivity.this, UserRegistrationActivity.class);
                                startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserRegistrationActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

