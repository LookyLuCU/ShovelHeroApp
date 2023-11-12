package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference shovelHeroDatabaseReference;
    private EditText usernameEditText, passwordEditText;
    private Spinner accountTypeSpinner;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shovelHeroDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        accountTypeSpinner = findViewById(R.id.spinnerAccountType);
        usernameEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPassword);
    }


        //tried slightly different way to be consistent with registration
        /**
        Spinner userTypeSpinner = findViewById(R.id.spinnerUsertype);
        String[] userTypes = {"Shoveller", "Guardian", "Customer", "Fundraiser", "Administrator"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userTypeSpinner.setAdapter(adapter);
         **/

        //added own method for user registration below
        /**
        //New Account Registration
        Button registerButton = findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserRegistrationActivity.class);
                startActivity(intent);
            }
        });
         **/

        public void loginUser(View view) {
            final String username = usernameEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();
            final String accountType = accountTypeSpinner.getSelectedItem().toString();

            //Check if username exists
            shovelHeroDatabaseReference.orderByChild("username").equalTo(username)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                                    User user = userSnapShot.getValue(User.class);

                                    if (user != null && user.getPassword().equals(password) && user.getAccountType().equals(accountType)){

                                        //valid username and password
                                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS
                                        switch (accountType) {
                                            case "Youth Shoveller":
                                                Intent intentYouth = new Intent(MainActivity.this, YouthShovelerProfileActivity.class);
                                                int youthID = user.getUserId();
                                                intentYouth.putExtra("USER_ID", youthID);
                                                startActivity(intentYouth);
                                                break;
                                            case "Customer":
                                                Intent intentCustomer = new Intent(MainActivity.this, CustomerProfileActivity.class);
                                                int customerId = user.getUserId();
                                                intentCustomer.putExtra("USER_ID", customerId);
                                                startActivity(intentCustomer);
                                                break;
                                            case "Guardian":
                                                Intent intentGuardian = new Intent(MainActivity.this, GuardianProfileActivity.class);
                                                int guardianId = user.getUserId();
                                                intentGuardian.putExtra("USER_ID", guardianId);
                                                startActivity(intentGuardian);
                                            default:
                                                Intent intent = new Intent(MainActivity.this, UserRegistrationActivity.class);
                                                startActivity(intent);
                                        }
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "invalid password or account type", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "User not found. Pleae try again or create a new account.", Toast.LENGTH_SHORT).show();
;                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //handle error
                        }
                    });
        }

        public void createNewUser(View view){
            Intent intent = new Intent(MainActivity.this, UserRegistrationActivity.class);
            startActivity(intent);
        }
    }



    //***************************
    //FIREBASE AUTH ONLY ALLOWS 3 USER FIELDS - CONSIDERING SETTING UP AUTH VIA REALTIME DB
    //**************************

    /**
    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    public void getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String userName = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // [END get_user_profile]
    }
     **/

