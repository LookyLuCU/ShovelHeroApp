package com.example.shovelheroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner userTypeSpinner = findViewById(R.id.spinnerUsertype);

        String[] userTypes = {"Shoveller", "Guardian", "Customer", "Fundraiser", "Administrator"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userTypeSpinner.setAdapter(adapter);
    }
}