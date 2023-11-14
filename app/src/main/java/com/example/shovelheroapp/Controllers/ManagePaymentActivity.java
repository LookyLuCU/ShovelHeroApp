package com.example.shovelheroapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.shovelheroapp.R;

public class ManagePaymentActivity extends AppCompatActivity {

    private Spinner cardTypeSpinner;
    private EditText cardNumberEditText;
    private EditText expiryDateEditText;
    private EditText cvvEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment);

        // Initialize views
        cardTypeSpinner = findViewById(R.id.spinnerCardType);
        cardNumberEditText = findViewById(R.id.editTextCardNumber);
        expiryDateEditText = findViewById(R.id.editTextExpiryDate);
        cvvEditText = findViewById(R.id.editTextCvv);
        saveButton = findViewById(R.id.buttonSave);

        // Populate card type spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.payment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardTypeSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCardDetails()) {
                    saveCardDetails();
                }
            }
        });
    }
    private boolean validateCardDetails() {

        String cardType = cardTypeSpinner.getSelectedItem().toString();
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();

        // Validate card number (16 digits)
        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            cardNumberEditText.setError("Invalid card number");
            return false;
        }

        // Validate expiry date (mm/yy)
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/YY");
        dateFormat.setLenient(false);
        try {
            Date expiryDateObj = dateFormat.parse(expiryDate);
            if (expiryDateObj == null || expiryDateObj.before(new Date())) {
                expiryDateEditText.setError("Invalid expiry date");
                return false;
            }
        } catch (ParseException e) {
            expiryDateEditText.setError("Invalid expiry date");
            return false;
        }

        // Validate CVV (3 digits)
        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            cvvEditText.setError("Invalid CVV");
            return false;
        }

        // All validations passed
        return true;
    }
    private void saveCardDetails() {
        String cardType = cardTypeSpinner.getSelectedItem().toString();
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();

        String message = "Card details saved:\n" +
                "Card Type: " + cardType + "\n" +
                "Card Number: " + cardNumber + "\n" +
                "Expiry Date: " + expiryDate + "\n" +
                "CVV: " + cvv;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}