package com.example.shovelheroapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shovelheroapp.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        final EditText newPasswordEditText = findViewById(R.id.etNewPass);
        final EditText confirmPasswordEditText = findViewById(R.id.etConfirmPass);
        Button saveButton = findViewById(R.id.btnUpdatePass);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Check if the new password meets the criteria
                if (newPassword.length() < 8 || !newPassword.matches(".*\\d.*") || !newPassword.matches(".*[a-zA-Z].*")) {
                    Toast.makeText(EditPasswordActivity.this, "New password must be at least 8 characters long and contain at least one letter and one digit", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the new password and confirm password match
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(EditPasswordActivity.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(EditPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}