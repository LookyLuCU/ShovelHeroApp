package com.example.shovelheroapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shovelheroapp.R;

public class ManagePropertyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_property);

        Button addButton = findViewById(R.id.addshovellerButton);

        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(ManagePropertyActivity.this, CreateAddressActivity.class);
                                             startActivity(intent);
                                         }
                                     }

        );
    }
}