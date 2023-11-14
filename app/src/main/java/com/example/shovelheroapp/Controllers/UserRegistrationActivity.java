package com.example.shovelheroapp.Controllers;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shovelheroapp.Models.Address;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistrationActivity";
    private String userId;
    private Spinner spinnerAccountType;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private TextView birthdateText;
    private String selectedBirthdate;
    private EditText emailEditText;
    private EditText phoneEditText;
    private ImageButton profileChangeImage;
    private Uri selectedImageUri;

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
        emailEditText = findViewById(R.id.etEmail);
        phoneEditText = findViewById(R.id.etPhone);
        birthdateText = findViewById(R.id.btnBirthdate);
        profileChangeImage = findViewById(R.id.imgProfilePicture);

        birthdateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerPrompt();
            }
        });

        //image button listener
        profileChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    public void showDatePickerPrompt() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

       // Should create a custom DatePicker to make selecting year easier.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UserRegistrationActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Date formatting
                    selectedBirthdate =  (selectedMonth + 1) + "-" + selectedDay + "-" + selectedYear;
                    birthdateText.setText(selectedBirthdate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    public void createUser(View view) {

        //intialize ShovelHeroDB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference("users");

        // save fields to proper input type

        userId = userReference.push().getKey();
        String accountType = spinnerAccountType.getSelectedItem().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String birthdate = selectedBirthdate;

        //create new user
        User newUser = new User(userId, accountType, username, password, firstName, lastName, birthdate, email, phone);

        //add to shovelHeroDB
        userReference.child(userId).setValue(newUser)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(UserRegistrationActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATION
                        switch (accountType) {
                            case "Youth Shoveller":
                                Intent intentCreateYouth = new Intent(UserRegistrationActivity.this, YouthShovelerProfileActivity.class);
                                String youthID = userId;
                                intentCreateYouth.putExtra("USER_ID", youthID);
                                startActivity(intentCreateYouth);
                                break;
                            case "Customer":
                                Intent intentCreateCustomer = new Intent(UserRegistrationActivity.this, CustomerProfileWithAddressesActivity.class);
                                String customerId = userId;
                                intentCreateCustomer.putExtra("USER_ID", customerId);
                                startActivity(intentCreateCustomer);
                                break;
                            case "Guardian":
                                Intent intentCreateGuardian = new Intent(UserRegistrationActivity.this, GuardianProfileActivity.class);
                                String guardianId = userId;
                                intentCreateGuardian.putExtra("USER_ID", guardianId);
                                startActivity(intentCreateGuardian);
                                break;
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

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImage.launch(intent);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    selectedImageUri = data.getData();
                    setImageButtonBackground(selectedImageUri);
                    saveImageToDatabase(selectedImageUri);
                }
            });

    private void setImageButtonBackground(Uri imageUri) {
        try {
            // Load the selected image from the URI
            Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            // Set the image as the background of the ImageButton
            profileChangeImage.setImageBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToDatabase(Uri imageBytes) {
        //***TO DO
    }
}

