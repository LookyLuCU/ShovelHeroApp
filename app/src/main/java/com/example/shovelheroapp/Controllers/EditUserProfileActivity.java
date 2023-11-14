package com.example.shovelheroapp.Controllers;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.shovelheroapp.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;


import com.example.shovelheroapp.R;

public class EditUserProfileActivity extends AppCompatActivity {

    // Tested and works.
    // However, the Date Picker will not load if there isn't navigation to the view.

    private EditText editPassword, confirmPassword, editFirstname, editLastname;
    private EditText editBirthdate, editEmail, editPhoneNumber;
    private Button updateProfile;
    private String userId;

    private String selectedBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        // Get Views
        editPassword = findViewById(R.id.etEditPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        editFirstname = findViewById(R.id.etEditFirstname);
        editLastname = findViewById(R.id.etEditLastname);
        editBirthdate = findViewById(R.id.etEditBirthdate);
        editEmail = findViewById(R.id.etEditEmail);
        editPhoneNumber = findViewById(R.id.etEditPhoneNumber);
        updateProfile = findViewById(R.id.btnUpdateProfile);

        editBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerPrompt();
            }
        });

        //Retrieve User ID
        userId = getIntent().getStringExtra("USER_ID");
        if(userId == null || userId.isEmpty()){
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }
        //Load the User Data
        loadUserProfile();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
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
                EditUserProfileActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Date formatting
                    selectedBirthdate =  (selectedMonth + 1) + "-" + selectedDay + "-" + selectedYear;
                    editBirthdate.setText(selectedBirthdate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void loadUserProfile(){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if(user != null) {
                        editFirstname.setText(user.getFirstName());
                        editLastname.setText(user.getLastName());
                        editBirthdate.setText(user.getBirthdate());
                        editEmail.setText(user.getEmail());
                        editPhoneNumber.setText(user.getPhoneNo());
                    } else {
                        Toast.makeText(EditUserProfileActivity.this, "User data cannot be found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditUserProfileActivity.this, "User doesn't exist in the database", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditUserProfileActivity.this, "Could not load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUserProfile() {
        // TODO: input field validation

        String newPassword = editPassword.getText().toString();
        String newFirstname = editFirstname.getText().toString();
        String newLastname = editLastname.getText().toString();
        String newBirthdate = editBirthdate.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPhoneNumber = editPhoneNumber.getText().toString();

        Map<String, Object> updateProfileData = new HashMap<>();
        updateProfileData.put("password", newPassword);
        updateProfileData.put("firstName", newFirstname);
        updateProfileData.put("lastName", newLastname);
        updateProfileData.put("birthdate", newBirthdate);
        updateProfileData.put("email", newEmail);
        updateProfileData.put("phoneNo", newPhoneNumber);

        // Update in Firebase
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userReference.updateChildren(updateProfileData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditUserProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUserProfileActivity.this, "Profile update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}