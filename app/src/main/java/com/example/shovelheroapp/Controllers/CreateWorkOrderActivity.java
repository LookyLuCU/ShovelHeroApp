package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.Enums.Status;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateWorkOrderActivity extends AppCompatActivity {

    private static final String TAG = "CreateWorkOrderActivity";

    private DatabaseReference addressTable;

    //address spinner
    private Spinner addressSpinner;
    private List<Address> spinnerItemList;
    private ArrayAdapter<String> spinnerAdapter;


    private String workOrderId;
    private EditText squareFootageEditText; //-->This should be from AddressId
    private EditText customerShovelerEditText; //-->This should be from AddressId
    private TextView workOrderPriceTextView;
    private Date requestDate;
    private EditText requestedDate;
    private TextClock requestedTime;

    private List<String> itemsRequested;
    private CheckBox drivewayCheckBox;
    private CheckBox walkwayCheckBox;
    private CheckBox sidewalkCheckBox;

    private EditText specialInstructionsEditText;
    private String status;
    TextView dateTimeInLongTextView;

    //private int iterator;
    private String workOrderID;

    private Double wOPrice = 0.0;
    private String customerId;
    private String addressId;


    private User currentUser;
    private Address currentAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_create);

        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        //bring over customer username
        Intent intentNewWO = getIntent();
        if (intentNewWO != null) {
            String currentCustomerId = intentNewWO.getStringExtra("USER_ID");
            if (currentCustomerId != null) {

                final String customerId = currentCustomerId;
                squareFootageEditText = findViewById(R.id.etSquareFootage);
                customerShovelerEditText = findViewById(R.id.etCustomShoveller);

                drivewayCheckBox = findViewById(R.id.cbDriveway);
                walkwayCheckBox = findViewById(R.id.cbWalkway);
                sidewalkCheckBox = findViewById(R.id.sidewalk_checkbox);

                requestedDate = findViewById(R.id.etCustomDate);
                requestedTime = findViewById(R.id.tpCustomTime);

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
                dateTime = simpleDateFormat.format(calendar.getTime()).toString();
                requestedDate.setText(dateTime);

                //spinner adapter
                addressSpinner = findViewById(R.id.spinnerAddress);
                spinnerItemList = new ArrayList<>();
                spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                addressSpinner.setAdapter(spinnerAdapter);
            }
        }
    }

    public void createWorkOrder(View view) {

        //initiatize ShovelHero DB
        FirebaseDatabase shovelHeroDatabase = FirebaseDatabase.getInstance();
        DatabaseReference workOrderReference = shovelHeroDatabase.getReference("workorders");
        DatabaseReference usersReference = shovelHeroDatabase.getReference("users");
        addressTable = shovelHeroDatabase.getReference("address");

        if (currentUser.getAddresses() == null) {
            Toast.makeText(CreateWorkOrderActivity.this, "Please add an address", Toast.LENGTH_SHORT).show();
        } else {
            //retrieve addresses from Firebase
            retrieveAddresses();


            //Handle Spinner address selection
            addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Handle the selected address item
                    currentAddress = spinnerItemList.get(position);
                    // You can use 'selectedAddress' for further processing or adding to the work order.
                    Toast.makeText(CreateWorkOrderActivity.this, "Selected Address: " + currentAddress.getAddress() + ", " + currentAddress.getCity() + ", " + currentAddress.getCity(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Do nothing
                }
            });
        }

        //save fields to proper input type
        workOrderId = workOrderReference.push().getKey();
        requestDate = Calendar.getInstance().getTime();
        String address = currentAddress.getAddress();
        String status = Status.Open.toString();
        int sqrFootage = currentAddress.getDrivewaySquareFootage();
        String customerRequestedDate = requestedDate.toString();
        String customerRequestedTime = requestedTime.toString();


        //ORDER COMLEPEXITY AND PRICING
        if (drivewayCheckBox.isChecked()) {
            itemsRequested.add("Driveway");
            if (sqrFootage <= 600) {
                wOPrice = wOPrice + 20.00;
            } else {
                double pricePerSquareFoot = 0.06;
                wOPrice = sqrFootage * pricePerSquareFoot;
            }
        }

        if (sidewalkCheckBox.isChecked()) {
            itemsRequested.add("Sidewalk");
            wOPrice = wOPrice + 10.00;
        }

        if (walkwayCheckBox.isChecked()) {
            itemsRequested.add("Walkway");
            wOPrice = wOPrice + 10.00;
        }


        //create WO object and save to DB
        WorkOrder newWorkOrder = new WorkOrder(workOrderID, requestDate, status, sqrFootage, itemsRequested, currentUser.getUserId(), addressId);
        workOrderReference.child(workOrderId).setValue(newWorkOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String accountType = currentUser.getAccountType().toString();
                        String currentCustomerId = currentUser.getUserId();
                        Toast.makeText(CreateWorkOrderActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS

                        switch (accountType) {
                            case "Youth Shoveller":
                                Intent intentYouth = new Intent(CreateWorkOrderActivity.this, YouthShovelerProfileActivity.class);
                                String youthID = currentCustomerId;
                                intentYouth.putExtra("USER_ID", youthID);
                                startActivity(intentYouth);
                                break;
                            case "Customer":
                                Intent intentCustomer = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                                String customerId = currentCustomerId;
                                intentCustomer.putExtra("USER_ID", customerId);
                                startActivity(intentCustomer);
                                break;
                            case "Guardian":
                                Intent intentGuardian = new Intent(CreateWorkOrderActivity.this, GuardianProfileActivity.class);
                                String guardianId = currentCustomerId;
                                intentGuardian.putExtra("USER_ID", guardianId);
                                startActivity(intentGuardian);
                            default:
                                Intent intent = new Intent(CreateWorkOrderActivity.this, UserRegistrationActivity.class);
                                startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrieveUserAndAddress(String customerId) {
        DatabaseReference userTable = FirebaseDatabase.getInstance().getReference("users").child(customerId);
        userTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                String accountType = currentUser.getAccountType();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //DatabaseReference addressTable = userTable.child("addresses")


    }

    //get address from selection
    private void retrieveAddresses() {
        addressTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    Address spinnerItem = addressSnapshot.getValue(Address.class);
                    if (spinnerItem != null) {
                        spinnerItemList.add(spinnerItem);
                        spinnerAdapter.add(spinnerItem.getAddress() + ", " + spinnerItem.getCity() + ", " + spinnerItem.getProvince()); // Display the street in the Spinner
                    }
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

        //FROM NEW ADDRESS LOGIC - to return to profile after creating work order
        private void saveAndReturnToProfile (String customerId){
            DatabaseReference userTable = FirebaseDatabase.getInstance().getReference("users").child(customerId);
            userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser = snapshot.getValue(User.class);
                    String accountType = currentUser.getAccountType();

                    //**todo** ADD ADULT SHOEVLLER AND GUARDIAN
                    if (accountType != null) {
                        switch (accountType) {
                            case "Youth Shoveller":
                                Intent intentYouth = new Intent(CreateWorkOrderActivity.this, YouthShovelerProfileActivity.class);
                                String youthID = currentUser.getUserId();
                                intentYouth.putExtra("USER_ID", youthID);
                                startActivity(intentYouth);
                                break;
                            case "Customer":
                                Intent intentCustomer = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                                String customerId = currentUser.getUserId();
                                intentCustomer.putExtra("USER_ID", customerId);
                                startActivity(intentCustomer);
                                break;
                            case "Guardian":
                                Intent intentGuardian = new Intent(CreateWorkOrderActivity.this, GuardianProfileActivity.class);
                                String guardianId = currentUser.getUserId();
                                intentGuardian.putExtra("USER_ID", guardianId);
                                startActivity(intentGuardian);
                                break;
                            default:
                                Intent intent = new Intent(CreateWorkOrderActivity.this, UserRegistrationActivity.class);
                                startActivity(intent);
                                break;
                        }
                    } else {
                        System.out.println("Account Type is Null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


