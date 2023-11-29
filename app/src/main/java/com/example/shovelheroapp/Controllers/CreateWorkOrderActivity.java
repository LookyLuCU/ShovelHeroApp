package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shovelheroapp.Models.Enums.Status;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


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
    SimpleDateFormat simpleTimeFormat;
    private TextView requestDate;   //date request was placed
    private EditText requestedDate; //date customer has requested in the future
    //private TextClock requestedTime;  //time customer has requested in the future
    private EditText requestedTime;

    //WO Items and pricing
    private List<String> itemsRequested;
    private List<CheckBox> itemCheckBoxList;
    private CheckBox drivewayCheckBox;
    private CheckBox walkwayCheckBox;
    private CheckBox sidewalkCheckBox;
    private Double wOPrice = 2.00;  //app overhead cost - maybe more with stripe fee

    private EditText addressNotesEditText;
    TextView dateTimeInLongTextView;


    //Buttons
    private Button btnOrderShovelling;
    private Button btnCancelOrder;


    //instantiated Objects
    private WorkOrder currentWorkOrder;
    private String currentWOId;


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

        //**TODO: Fix date/time
        requestedDate = findViewById(R.id.etCustomDate);
        //requestedTime = findViewById(R.id.tpCustomTime);
        requestedTime = findViewById(R.id.etCustomTime);

        /**
         calendar = Calendar.getInstance();
         //simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
         simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
         dateTime = simpleDateFormat.format(calendar.getTime()).toString();
         //requestedDate.setText(dateTime);
         simpleTimeFormat = new SimpleDateFormat("HH:mm aaa z");
         */

        itemCheckBoxList = new ArrayList<>();
        itemCheckBoxList.add(findViewById(R.id.cbDriveway));
        itemCheckBoxList.add(findViewById(R.id.cbSidewalk));
        itemCheckBoxList.add(findViewById(R.id.cbWalkway));

        btnOrderShovelling = findViewById(R.id.btnOrderShovelling);
        btnCancelOrder = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        if (intent != null) {
            currentWOId = intent.getStringExtra("WORK_ORDER_ID");
            if (currentWOId != null) {
                System.out.println("work order ID recieved: " + currentWOId);  //WORKING
                retrieveWOInfo(currentWOId);
            }
        }
        System.out.println("Work order id intent ok from profile: " + currentWOId);

        //initialize Firebase
        userTable = FirebaseDatabase.getInstance().getReference("users");

        updateBill(currentWOId);

        //Add checkbox listeners
        for (final CheckBox checkbox : itemCheckBoxList) {
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    updateBill(currentWOId);
                }
            });
        }
    }


    private void retrieveWOInfo(String currentWOID) {
        System.out.println("Work order received to retrieve wo info: " + currentWOID + " - line 179");

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentWorkOrder = snapshot.getValue(WorkOrder.class);

                    if (currentWorkOrder != null) {
                        //display inital WO information
                        System.out.println("Work order found to Read address from Firebase: in retrieveWOInfo - line 190");
                        readAddressFromFirebase(currentWorkOrder);

                        workOrderPriceTextView.setText("Shovelling Price: $2.00");
                        sqFootageTextView.setText("Job Size: " + currentWorkOrder.getSquareFootage() + " square feet");
                        requestDate.setText("Request Date: " + currentWorkOrder.getRequestDate().toString());

                        System.out.println("Work order data loaded into retrieve WO info - line 197");


                        //*******
                        //WORK ORDER CREATE BUTTONS
                        //*******

                        btnOrderShovelling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println("Order Shovelling button clicked");
                                String date = requestedDate.getText().toString();
                                String time = requestedTime.getText().toString();
                                //String time = requestedTime.getFormat12Hour().toString();
                                String customShovelerUsername = customShovelerEditText.getText().toString();
                                System.out.println("Custom Shoveller Username before null decision:" + customShovelerUsername + ". - line 211");

                                /**
                                 if (date != null && !isValidDate(date)) {
                                 Toast.makeText(CreateWorkOrderActivity.this, "Please enter valid date in yyyy-MM-dd format", Toast.LENGTH_SHORT).show();
                                 } else if (time != null && !isValidTime(time)) {
                                 Toast.makeText(CreateWorkOrderActivity.this, "Please enter valid time in h:mm format", Toast.LENGTH_SHORT).show();
                                 } else {
                                 releaseWorkOrder(currentWorkOrder);}
                                 **/

                                if (customShovelerUsername.isEmpty()){
                                    System.out.println("Custom Shoveller Username = NULL, WO being released if job type selected - line 226: " + customShovelerUsername);
                                    releaseWorkOrder(currentWorkOrder);
                                }
                                else {
                                    System.out.println("Custom Shoveller Username deemed NOT null, username being sent for validation - line 230: " + customShovelerUsername);
                                    validateUserName(customShovelerUsername, currentWorkOrder);
                                }
                            }
                        });


                        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("Button clicked for customer to cancel order before release - line 240");
                                cancelWorkOrder(currentWorkOrder);
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


    //READ ADDRESS FROM REALTIMEDB - DONE
    private void readAddressFromFirebase(WorkOrder workOrder) {
        System.out.println("workOrderId received to retrieve address: " + workOrder);

        DatabaseReference addressReference = userTable.child(workOrder.getCustomerId()).child("addresses").child(workOrder.getCustomerAddressId());
        System.out.println("addressed referenced for address, ID: " + addressReference.getKey()); //RETRIEVES OK

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


    //AUTOMATIC PRICING FROM CHECKBOXES - UPDATE BILL - DONE
    private void updateBill(String currentWorkOrderID) {
        System.out.println("updateBill Called - line 291");
        double totalPrice = wOPrice;

        //iterate through checkboxes
        for (CheckBox checkBox : itemCheckBoxList) {
            if (checkBox.isChecked()) {
                totalPrice += getPriceForCheckBoxes(checkBox);

                workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWorkOrderID);
                double finalTotalPrice = totalPrice;
                workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            currentWorkOrder = snapshot.getValue(WorkOrder.class);

                            if (currentWorkOrder != null) {
                                currentWorkOrder.setPrice(finalTotalPrice);
                                System.out.println("Firebase price? - line 285: " + currentWorkOrder.getPrice());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                workOrderPriceTextView.setText("Shovelling Price: $" + currentWorkOrder.getPrice());
            }
        }
    }


    //AUTOMATIC PRICING FROM CHECKBOXES - ADD PRICING - DONE
    private Double getPriceForCheckBoxes(CheckBox checkBox) {
        System.out.println("getPriceForCheckboxes Called - line 306");
        if (checkBox.getId() == R.id.cbDriveway) {
            return 20.00;
        } else if (checkBox.getId() == R.id.cbSidewalk) {
            return 10.00;
        } else if (checkBox.getId() == R.id.cbWalkway) {
            return 10.00;
        }
        return 0.00;
    }


    public void releaseWorkOrder(WorkOrder currentWorkOrder) {
        System.out.println("Release WO now being called with WO - line 318: " + currentWorkOrder);
        //if (drivewayCheckBox.isChecked() || sidewalkCheckBox.isChecked() || walkwayCheckBox.isChecked()) {
        if (!itemCheckBoxList.isEmpty()) {

            System.out.println("Which job types are selected? line 318 : " + itemCheckBoxList.toString());
            addWorkOrderDetails(currentWorkOrder);
        } else {
            Toast.makeText(CreateWorkOrderActivity.this, "Please add shovelling area", Toast.LENGTH_SHORT).show();
        }
    }


    public void addWorkOrderDetails(WorkOrder currentWorkOrder) {
        System.out.println("Add workOrderDetails method started - line 328 - WO rec'd: " + currentWorkOrder);

        String customerRequestedDate = requestedDate.getText().toString();
        String customerRequestedTime = requestedTime.getText().toString();
        System.out.println("Date and Time retrieved from UI in addWorkOrderDetails - line 332: " + customerRequestedDate + " " + customerRequestedTime);

        //String customerRequestedTime = requestedTime.getFormat12Hour().toString();
        String status = Status.Open.toString();
        System.out.println("Updated WO Price added from UI TV in addWODetails - line 338: " + currentWorkOrder.getPrice());

        //Add items Requested List
        if (drivewayCheckBox.isChecked()) {itemsRequested.add("Driveway");}
        if (sidewalkCheckBox.isChecked()) { itemsRequested.add("Sidewalk");}
        if (walkwayCheckBox.isChecked()) { itemsRequested.add("Walkway");}

        System.out.println("Test if itemsRequestedList being populated - addWODetails - line 351: " + itemsRequested);

        //add additional order info to realtimedb
        workOrderReference = FirebaseDatabase.getInstance().getReference("workorder").child(currentWorkOrder.getWorkOrderId());
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    WorkOrder currentWO = snapshot.getValue(WorkOrder.class);

                    currentWO.setStatus(status);
                    System.out.println("addWODetails successfully added updated status: " + currentWO.getStatus());

                    System.out.println("addWODetails successfully added updated price: " + currentWO.getPrice());

                    currentWO.setItemsRequested(itemsRequested);
                    System.out.println("addWODetails successfully added updated itemsRequested: " + currentWO.getItemsRequested());


                    //Specific Date/Time Requests
                    if (customerRequestedDate.isEmpty()) {
                        customerRequestedDate.isEmpty();
                    } else {
                        currentWO.setRequestedDate(customerRequestedDate);
                        System.out.println("addWODetails successfully added updated RequestedDate: " + currentWO.getRequestedDate());

                    }

                    if (customerRequestedTime.isEmpty()) {
                        customerRequestedTime.isEmpty();
                    } else {
                        currentWO.setRequestedTime(customerRequestedTime);
                        System.out.println("addWODetails successfully added updated RequestedTime: " + currentWO.getRequestedTime());
                    }

                    //SAVE AND RETURN TO PROFILE
                    saveAndReturnToProfile(currentWorkOrder.getCustomerId());
                }
                else {
                    Toast.makeText(CreateWorkOrderActivity.this, "Could not create Shovelling Job", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        /**
        Map<String, Object> updateWorkOrder = new HashMap<>();

        //update status to "Open", price
        updateWorkOrder.put("status", status);
        updateWorkOrder.put("price", dblPrice);

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
        }
        if (sidewalkCheckBox.isChecked()) {
            itemsRequested.add("Sidewalk");
        }
        if (walkwayCheckBox.isChecked()) {
            itemsRequested.add("Walkway");
        }
        updateWorkOrder.put("itemsRequested", itemsRequested);


        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWorkOrder.getWorkOrderId());
        workOrderReference.updateChildren(updateWorkOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(CreateWorkOrderActivity.this, "Shovelling Request is now active", Toast.LENGTH_SHORT).show();

                        Intent intentCreateWO = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                        String customerId = currentWorkOrder.getCustomerId();
                        intentCreateWO.putExtra("CUSTOMER_ID", customerId);
                        startActivity(intentCreateWO);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Could not create Shovelling Job", Toast.LENGTH_SHORT).show();
                    }
                });
         **/
    }

    public void saveAndReturnToProfile(String customerId) {
        System.out.println("Saving updated WO and returning to profile using customer ID - line 458: " + customerId);
        Toast.makeText(CreateWorkOrderActivity.this, "Shovelling Request is now active", Toast.LENGTH_SHORT).show();

        Intent intentCreateWO = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
        String customerID = customerId;
        intentCreateWO.putExtra("USER_ID", customerID);
        startActivity(intentCreateWO);
    }


    //CANCEL WORK ORDER UPON CUSTOMER BUTTON CLICK - DONE
    public void cancelWorkOrder(WorkOrder workOrder) {
        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(workOrder.getWorkOrderId());
        System.out.println("Deleting Work order: " + workOrder + ". Confirm in Firebase");
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    WorkOrder currentWO = snapshot.getValue(WorkOrder.class);

                    workOrderReference.removeValue();

                    Toast.makeText(CreateWorkOrderActivity.this, "Shovelling Request Cancelled", Toast.LENGTH_SHORT).show();

                    Intent intentCancelWO = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                    String customerId = currentWO.getCustomerId();
                    intentCancelWO.putExtra("USER_ID", customerId);
                    startActivity(intentCancelWO);


                    /**
                    HashMap<String, Object> workOrderMap = (HashMap<String, Object>) snapshot.getValue();

                    //retrieve values from hashmap
                    String customerID = (String) workOrderMap.get("customerid");
                    workOrderReference.removeValue();

                    Toast.makeText(CreateWorkOrderActivity.this, "Shovelling Request Cancelled", Toast.LENGTH_SHORT).show();

                    Intent intentCancelWO = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                    String customerId = customerID;
                    intentCancelWO.putExtra("CUSTOMER_ID", customerID);
                    startActivity(intentCancelWO);
                     **/
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateWorkOrderActivity.this, "Unable to cancel shovelling request", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Validation function for Date
    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Validation function for Time
    private boolean isValidTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Validation function for UserName
    private void validateUserName(String customShovelerUsername, WorkOrder currentWorkOrder) {
        System.out.println("Validating username - line 538: " + customShovelerUsername + " for wo: " + currentWorkOrder);
        userTable = FirebaseDatabase.getInstance().getReference("users");
        userTable.orderByChild("username").equalTo(customShovelerUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                                User user = userSnapShot.getValue(User.class);

                                Toast.makeText(CreateWorkOrderActivity.this, "Username " + customShovelerUsername + " will be notified of your request if they are online.", Toast.LENGTH_SHORT).show();
                                System.out.println("Username: " + customShovelerUsername + " has been requested by customer");

                                String shovellerId = user.getUserId();
                                addCustomShovelerToWorkOrder(shovellerId, currentWorkOrder);
                            }
                        } else {
                            Toast.makeText(CreateWorkOrderActivity.this, "Username not found. Please try again or leave empty.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //handle error
                    }
                });
    }

    public void addCustomShovelerToWorkOrder(String shovellerId, WorkOrder currentWorkOrder){
        System.out.println("Adding validated customer shoveller to WO - line 566: " + shovellerId);
        workOrderReference = FirebaseDatabase.getInstance().getReference("workorder").child(currentWorkOrder.getWorkOrderId());
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    WorkOrder currentWO = snapshot.getValue(WorkOrder.class);

                    currentWO.setShovellerId(shovellerId);
                    System.out.println("Successfully added shoveller to WO in RealtimeDB - line 575: " + currentWO.getShovellerId());
                    releaseWorkOrder(currentWorkOrder);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
