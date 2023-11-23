package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateWorkOrderActivity extends AppCompatActivity {

    private static final String TAG = "CreateWorkOrderActivity";


    //initialized database tables
    private DatabaseReference workOrderTable;
    private DatabaseReference workOrderReference;
    private DatabaseReference userTable;

    //address spinner
    //private Spinner addressSpinner;
    //private List<Address> spinnerItemList;
    //private ArrayAdapter<String> spinnerAdapter;


    private String workOrderId;
    private TextView addressTextView;
    private TextView sqFootageTextView;
    //private EditText squareFootageEditText; //-->This should be from AddressId
    private EditText customShovelerEditText;

    private TextView workOrderPriceTextView;


    //Date fields
    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private TextView requestDate;   //date request was placed
    private EditText requestedDate; //date customer has requested in the future
    private TextClock requestedTime;  //time customer has requested in the future


    private List<String> itemsRequested;
    private CheckBox drivewayCheckBox;
    private CheckBox walkwayCheckBox;
    private CheckBox sidewalkCheckBox;

    private EditText addressNotesEditText;
    private String status;
    TextView dateTimeInLongTextView;


    //Pricing
    private Double wOPrice = 0.0;
    private String currentWOId;


    //Buttons
    private Button btnOrderShovelling;
    private Button btnCancelOrder;


    //instantiated Objects
    private WorkOrder currentWorkOrder;
    private User currentUser;
    private Address currentAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_create);

        //String dateTime;
        //Calendar calendar;
        //SimpleDateFormat simpleDateFormat;

        addressTextView = findViewById(R.id.tvAddress);
        sqFootageTextView = findViewById(R.id.tvSquareFootage);
        requestDate = findViewById(R.id.tvRequestDate);
        customShovelerEditText = findViewById(R.id.etCustomShoveller);
        addressNotesEditText = findViewById(R.id.etAddressNotes);
        workOrderPriceTextView = findViewById(R.id.tvWorkOrderPrice);

        drivewayCheckBox = findViewById(R.id.cbDriveway);
        walkwayCheckBox = findViewById(R.id.cbWalkway);
        sidewalkCheckBox = findViewById(R.id.cbSidewalk);

        requestedDate = findViewById(R.id.etCustomDate);
        requestedTime = findViewById(R.id.tpCustomTime);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        requestedDate.setText(dateTime);

        btnOrderShovelling = findViewById(R.id.btnOrderShovelling);
        btnCancelOrder = findViewById(R.id.btnCancel);

        /**
         addressTextView = findViewById(R.id.tvAddress);

         //spinner adapter
         addressSpinner = findViewById(R.id.spinnerAddress);
         spinnerItemList = new ArrayList<>();
         spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
         spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
         addressSpinner.setAdapter(spinnerAdapter);
         **/

        Intent intent = getIntent();
        if (intent != null) {
            currentWOId = intent.getStringExtra("WORK_ORDER_ID");
            if (currentWOId != null) {
                System.out.println("work order ID recieved: " + currentWOId);  //WORKING
                retrieveWOInfo(currentWOId);
            }
        }

        System.out.println("Work order id intent ok fro profile: " + currentWOId);


        //initialize Firebase
        userTable = FirebaseDatabase.getInstance().getReference("users");

        //System.out.println("work order reference: " + workOrderTable.getKey());

        drivewayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePrice(currentWOId);
            }
        });

        walkwayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePrice(currentWOId);
            }
        });

        sidewalkCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePrice(currentWOId);
            }
        });
    }


    private void retrieveWOInfo(String currentWOID) {
        System.out.println("Work order received to retrieve wo info: " + currentWOID);

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentWorkOrder = snapshot.getValue(WorkOrder.class);

                    if (currentWorkOrder != null) {
                        //display inital WO information
                        System.out.println("Reading address from Firebase");
                        readAddressFromFirebase(currentWorkOrder);
                        //System.out.println("Work order address added to WO info: " + currentAddress.getAddress());

                        workOrderPriceTextView.setText("0");
                        sqFootageTextView.setText("Square Feet: " + currentWorkOrder.getSquareFootage());
                        requestDate.setText("Request Date: " + currentWorkOrder.getRequestDate().toString());

                        System.out.println("Work order data loaded");

                        //*******
                        //WORK ORDER CREATE BUTTONS
                        //*******

                        btnOrderShovelling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                releaseWorkOrder(currentWOID);
                            }
                        });

                        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //releaseWorkOrder(currentWOID);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }


    private void readAddressFromFirebase(WorkOrder workOrder) {
        System.out.println("workOrderId received to retrieve address: " + workOrder);

        DatabaseReference addressReference = userTable.child(workOrder.getCustomerId()).child("addresses").child(workOrder.getCustomerAddressId());

        //DatabaseReference addressReference = FirebaseDatabase.getInstance().getReference("users").child(workOrder.getCustomerId()).child("addresses").child(workOrder.getCustomerAddressId());
        System.out.println("addressed referenced for address, ID: " + addressReference.getKey()); //RETRIEVES OK

        //***TODO**THIS IS WHERE IT BREAKS***

        addressReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> addressMap = (HashMap<String, Object>) snapshot.getValue();

                    //retrieve values from hashmap
                    String address = (String) addressMap.get("address");
                    String city = (String) addressMap.get("city");
                    String province = (String) addressMap.get("province");

                    //currentAddress = snapshot.getValue(Address.class);
                    System.out.println("address object recieved from snapshot reading address from firebase: " + address);

                    addressTextView.setText(address + ", " + city + ", " + province);
                        System.out.println("Address retrieved from Firebase: " + address);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }


    private void updatePrice(String currentWOId) {

        System.out.println("Price to be updated");

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            int squareFootage;

            //wOPrice;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentWorkOrder = snapshot.getValue(WorkOrder.class);

                    if (currentWorkOrder != null) {
                        squareFootage = currentWorkOrder.getSquareFootage();

                        if (drivewayCheckBox.isChecked()) {
                            //itemsRequested.add("Driveway");
                            if (squareFootage <= 600) {
                                wOPrice = wOPrice + 20.00;
                            } else {
                                double pricePerSquareFoot = 0.06;
                                wOPrice = squareFootage * pricePerSquareFoot;
                                System.out.println("Driveway price added, total: " + wOPrice);
                            }
                        }

                        if (sidewalkCheckBox.isChecked()) {
                            //itemsRequested.add("Sidewalk");
                            wOPrice = wOPrice + 10.00;
                            System.out.println("Sidewalk price added, total: " + wOPrice);
                        }

                        if (walkwayCheckBox.isChecked()) {
                            //itemsRequested.add("Walkway");
                            wOPrice = wOPrice + 10.00;
                            System.out.println("Walkway price added, total: " + wOPrice);
                        }
                        workOrderPriceTextView.setText("$" + wOPrice);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }


    public void releaseWorkOrder(String currentWOId) {
        if (drivewayCheckBox.isChecked() || sidewalkCheckBox.isChecked() || walkwayCheckBox.isChecked()) {
            addWorkOrderDetails(currentWOId);
        } else {
            Toast.makeText(CreateWorkOrderActivity.this, "Please add shovelling area", Toast.LENGTH_SHORT).show();
        }
    }


    public void addWorkOrderDetails(String currentWOId) {
        String customerRequestedDate = requestedDate.getText().toString();
        String customerRequestedTime = requestedTime.getFormat12Hour().toString();
        String status = Status.Open.toString();
        String price = workOrderPriceTextView.getText().toString();

        Map<String, Object> updateWorkOrder = new HashMap<>();

        //update status to "Open", price
        updateWorkOrder.put("status", status);
        updateWorkOrder.put("price", price);

        //Specific Date/Time Requests
        if (customerRequestedDate.isEmpty()) {
            customerRequestedDate.isEmpty();
        } else {
            updateWorkOrder.put("requestedDate", requestedDate);
        }

        if (customerRequestedTime.isEmpty()) {
            customerRequestedTime.isEmpty();
        } else {
            updateWorkOrder.put("requestedTime", requestedTime);
        }


        //Add items Requested List
        if (drivewayCheckBox.isChecked()) {
            itemsRequested.add("Driveway");
            /**
             if (sqrFootage <= 600) {
             wOPrice = wOPrice + 20.00;
             } else {
             double pricePerSquareFoot = 0.06;
             wOPrice = sqrFootage * pricePerSquareFoot;
             }
             **/
        }
        if (sidewalkCheckBox.isChecked()) {
            itemsRequested.add("Sidewalk");
            /**
             wOPrice = wOPrice + 10.00;
             **/
        }
        if (walkwayCheckBox.isChecked()) {
            itemsRequested.add("Walkway");
            /**
             wOPrice = wOPrice + 10.00;
             **/
        }
        updateWorkOrder.put("itemsRequested", itemsRequested);

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.updateChildren(updateWorkOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Shovelling Request is now active", Toast.LENGTH_SHORT).show();

                        Intent intentCreateWO = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                        String wOID = currentWOId;
                        intentCreateWO.putExtra("WORK_ORDER_ID", wOID);
                        startActivity(intentCreateWO);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Could not create Shovelling Job", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



/**

        //Handle Spinner address selection - address already selected from profile (for now at least)
        /**
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
    **/


