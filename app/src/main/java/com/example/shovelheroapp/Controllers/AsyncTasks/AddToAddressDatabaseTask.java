package com.example.shovelheroapp.Controllers.AsyncTasks;

import android.os.AsyncTask;

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddToAddressDatabaseTask extends AsyncTask<Address, Void, Void> {
    private DatabaseReference addressTable;
    public AddToAddressDatabaseTask(){
        addressTable = FirebaseDatabase.getInstance().getReference("addresses");
    }

    @Override
    protected Void doInBackground(Address... params) {
        if (params.length > 0) {
            Address address = params[0];
            String addressId = addressTable.push().getKey();

            addressTable.child(addressId).setValue(address);
        }
        return null;
    }
}
