package com.example.shovelheroapp.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.Enums.Status;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuardianProfileActivity extends AppCompatActivity {
    private static final String TAG = "GuardianProfileActivity";

    //initialize Firebase
    DatabaseReference userTable;


    //Pending Work Order listings
    private RecyclerView pendingWORecyclerView;
    private WorkOrderAdapterForGuardian workOrderAdapter;
    private List<WorkOrder> pendingWorkOrderList;


    private TextView usernameTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView emailTV;
    private TextView phoneTV;


    private Spinner addressSpinner;
    private Spinner linkedYouthSpinner;
    private String userId;


    EditText addYouthET;


    //guardian ID
    private ImageButton btnAddIDPicture;
    private Uri guardianIdImageUri;
    private ActivityResultLauncher<Intent> iDImageLauncher;



    //buttons
    Button btnAddYouth;
    Button btnViewYouthProfile;
    Button btnViewRatings;
    Button btnManagePaymentInfo;
    Button btnManageProfileInfo;
    Button btnAddAddress;
    Button btnEditPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_guardian);

        userTable = FirebaseDatabase.getInstance().getReference("users");

        usernameTV = findViewById(R.id.tvUsername);
        firstNameTV = findViewById(R.id.tvFirstName);
        lastNameTV = findViewById(R.id.tvLastname);
        emailTV = findViewById(R.id.tvEmail);
        phoneTV = findViewById(R.id.tvPhone);

        btnAddIDPicture = findViewById(R.id.btnAddIDPicture);
        //drag and dropped id pic triggers intent sent to iDImageLauncher
        btnAddIDPicture.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                selectGuardianIDImage();
                return false;
            }
        });

        iDImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        guardianIdImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), guardianIdImageUri);
                            //profileImageView.setImageBitmap(bitmap);
                            updateUserProfile();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(GuardianProfileActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        addressSpinner = findViewById(R.id.spinnerAddress);

        addYouthET = findViewById(R.id.etAddYouth);
        btnAddYouth = findViewById(R.id.btnAddYouth);
        linkedYouthSpinner = findViewById(R.id.spinnerYouths);

        btnViewYouthProfile = findViewById(R.id.btnViewYouthProfile);
        btnViewRatings = findViewById(R.id.btnViewRatings);

        btnManagePaymentInfo = findViewById(R.id.btnManagePaymentInfo);
        btnManageProfileInfo = findViewById(R.id.btnManageProfileInfo);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnEditPassword = findViewById(R.id.btnEditPassword);


        //get Username from registration page or or UserID from Login
        //GET USERID FROM LOGIN OR REGISTRATION
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
            if (userId != null) {
                retrieveGuardianProfile(userId);
            }
        }


        //LIST OF OPEN ORDERS FOR GUARDIAN
        //initialize recyclerview
        System.out.println("Initializing Pending Orders Recycler");
        pendingWORecyclerView = findViewById(R.id.rvPendingWorkOrders);
        pendingWORecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize Pending Work Order list and Adapter
        pendingWorkOrderList = new ArrayList<>();
        workOrderAdapter = new WorkOrderAdapterForGuardian(this, pendingWorkOrderList, userId);
        pendingWORecyclerView.setAdapter(workOrderAdapter);

        //ADD PENDING WORK ORDERS TO PROFILE
        DatabaseReference workOrderReference = FirebaseDatabase.getInstance().getReference("workorders");
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingWorkOrderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WorkOrder workOrder = snapshot.getValue(WorkOrder.class);

                    //System.out.println("userId = " + userId);
                    //System.out.println("shovellerId = " + workOrder.getShovellerId());

                    if(workOrder.getGuardianId() != null &&
                            workOrder.getGuardianId().equals(userId) &&
                            (workOrder.getStatus().equals(Status.Open.toString()) ||
                                    workOrder.getStatus().equals(Status.OpenCustom.toString()) ||
                                    workOrder.getStatus().equals(Status.PendingGuardianApproval.toString()) ||
                                    workOrder.getStatus().equals(Status.Accepted.toString()) ||
                                    workOrder.getStatus().equals(Status.Enroute.toString()) ||
                                    workOrder.getStatus().equals(Status.InProgress.toString()) ||
                                    workOrder.getStatus().equals(Status.Issue.toString()) )
                    ) {
                        pendingWorkOrderList.add(workOrder);
                    }
                    else {
                        Toast.makeText(GuardianProfileActivity.this, "No Open Jobs", Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("ListAllOpenWorkOrders", "Data size: " + pendingWorkOrderList.size());
                workOrderAdapter.notifyDataSetChanged();
                pendingWORecyclerView.setAdapter(workOrderAdapter);
                Log.d("ListAllOpenWorkOrders", "Adapter notified of data change");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ListAllOpenWorkOrders", "Error fetching data: " + error.getMessage());
                error.toException().printStackTrace(); // Print stack trace for detailed error info
            }
        });



        //Navigation Bar Activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewGuardian);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.menu_workorders) {
                startActivity(new Intent(GuardianProfileActivity.this, ListAllOpenWorkOrdersActivity.class));
                return true;
            } else if (itemId == R.id.menu_orderhistory) {
                startActivity(new Intent(GuardianProfileActivity.this, OrderHistoryActivity.class));
                return true;
            } else if (itemId == R.id.menu_logout) {
                startActivity(new Intent(GuardianProfileActivity.this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }


    private void retrieveGuardianProfile(String userId) {
        userTable.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        //display user profile info
                        usernameTV.setText("Username: " + user.getUsername());
                        firstNameTV.setText("First Name: " + user.getFirstName());
                        lastNameTV.setText(user.getLastName());
                        emailTV.setText("Email: " + user.getEmail());
                        phoneTV.setText("Phone Number: " + user.getPhoneNo());


                        // Load profile Image
                        String profileImageUrl = user.getProfilePictureUrl();
                        ImageView profileImageView = findViewById(R.id.imgProfilePicture);
                        if(profileImageUrl != null && !profileImageUrl.isEmpty()){
                            Glide.with(GuardianProfileActivity.this)
                                    .load(profileImageUrl).into(profileImageView);
                        }

                        readAddressesFromFirebase(user);

                        if(user.getGuardianIdValidated() == false){
                            System.out.println("Please add your Picture ID to get started");
                            Toast.makeText(GuardianProfileActivity.this, "Valid Photo ID is required to add a Youth Shoveller to your profile", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            readYouthProfilesFromFirebase(user);
                            retrieveLinkedYouths(user);

                        }


                        //ADD YOUTH BUTTON
                        btnAddYouth.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //if(user.getGuardianIdValidated()){
                                System.out.println("this is the guardian being sent to link the youth: " + user.getUsername());
                                    linkYouthProfile(user);
                                //} else {
                                  //  Toast.makeText(GuardianProfileActivity.this, "Please ensure your ID has been uploaded and validated", Toast.LENGTH_SHORT).show();
                                //}
                            }
                        });

                        //VIEW RATINGS BUTTON
                        btnViewRatings.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(GuardianProfileActivity.this, "Temp msg: View ratings activity under construction", Toast.LENGTH_SHORT).show();

                                /**
                                 Intent intentViewRatings = new Intent(YouthShovelerProfileActivity.this, ViewRatingsActivity.class);
                                 String guardianId = user.getUserId();
                                 intentViewRatings.putExtra("USER_ID", guardianId);
                                 startActivity(intentViewRatings);
                                 overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                 **/
                            }
                        });

                        //MANAGE PROFILE BUTTON
                        btnManageProfileInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(GuardianProfileActivity.this, "Temp msg: Manage user profile under construction", Toast.LENGTH_SHORT).show();
                                Intent intentManageYouthProfile = new Intent(GuardianProfileActivity.this, EditUserProfileActivity.class);
                                String guardianId = user.getUserId();
                                intentManageYouthProfile.putExtra("USER_ID", guardianId);
                                startActivity(intentManageYouthProfile);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            }
                        });

                        //MANAGE PAYMENT BUTTON
                        btnManagePaymentInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(GuardianProfileActivity.this, "Temp msg: Manage Payment activity under construction", Toast.LENGTH_SHORT).show();
                                 Intent intentManageYouthPayment = new Intent(GuardianProfileActivity.this, ManagePaymentActivity.class);
                                 String guardianId = user.getUserId();
                                 intentManageYouthPayment.putExtra("USER_ID", guardianId);
                                 startActivity(intentManageYouthPayment);
                                 overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            }
                        });

                        //ADD ADDRESS BUTTON
                        btnAddAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentNewAddress = new Intent(GuardianProfileActivity.this, CreateAddressActivity.class);
                                String guardianId = user.getUserId();
                                intentNewAddress.putExtra("USER_ID", guardianId);
                                startActivity(intentNewAddress);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            }
                        });

                        //EDIT PASSWORD BUTTON
                        btnEditPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentEditPassword = new Intent(GuardianProfileActivity.this, EditPasswordActivity.class);
                                String guardianId = user.getUserId();
                                intentEditPassword.putExtra("USER_ID", guardianId);
                                startActivity(intentEditPassword);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            }
                        });
                    } else {
                        //handle no user data error
                    }
                } else {
                    //handle user id does not exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle error
            }
        });
    }


    private void readAddressesFromFirebase(User user) {
        System.out.println("userid received by read database: " + user);

        userTable.child(user.getUserId()).child("addresses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the addresses field in the User class
                user.setAddresses(new HashMap<String, Address>());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //manually deserialize the HashMap
                    Map<String, Object> addressMap = (Map<String, Object>) snapshot.getValue();

                    // retrieve values from the Hashmap
                    String addressId = (String) addressMap.get("addressId");
                    String address = (String) addressMap.get("address");
                    String city = (String) addressMap.get("city");
                    String province = (String) addressMap.get("province");
                    String postalCode = (String) addressMap.get("postalCode");
                    String country = (String) addressMap.get("country");
                    int drivewaySquareFootage = ((Long) addressMap.get("drivewaySquareFootage")).intValue();

                    // Create new Address object
                    Address addressObject = new Address(addressId, address, city, province, postalCode, country, drivewaySquareFootage);

                    // Add the Address object to the addresses HashMap in User model
                    user.addAddress(addressId, addressObject);
                }

                // Retrieve list of addresses from Hashmap
                List<Address> addresses = new ArrayList<>(user.getAddresses().values());

                // Update the Spinner with addresses
                ArrayAdapter<Address> addressAdapter = new ArrayAdapter<>(GuardianProfileActivity.this, android.R.layout.simple_spinner_item, addresses);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addressSpinner.setAdapter(addressAdapter);

                // Enable or disable the "Create Work Order" button based on the presence of addresses
                //btnOrderShoveling.setEnabled(!addresses.isEmpty());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void selectGuardianIDImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        iDImageLauncher.launch(intent);
    }

    private void updateUserProfile() {
        // TODO: input field validation

        if (guardianIdImageUri != null) {
            StorageReference fileReference = FirebaseStorage.getInstance().getReference().child("profilePictures/" + userId + ".jpg");
            fileReference.putFile(guardianIdImageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                updateFirebaseUserProfile(imageUrl);
                            })
                    )
                    .addOnFailureListener(e -> Toast.makeText(GuardianProfileActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // Upload profile information if no image is selected
            updateFirebaseUserProfile(null);
        }
    }


    private void updateFirebaseUserProfile(String iDImageURL){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        Map<String, Object> updateIDImageMap = new HashMap<>();
        if (iDImageURL != null){
            updateIDImageMap.put("guardianIdUrl", iDImageURL);
        }

        userReference.updateChildren(updateIDImageMap)
                .addOnSuccessListener(aVoid -> sendIdForValidation(iDImageURL, userId))
                .addOnSuccessListener(aVoid -> Toast.makeText(GuardianProfileActivity.this, "ID uploaded to your profile successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(GuardianProfileActivity.this, "ID Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void sendIdForValidation(String guardianIDUrl, String userId){
        String guardianUrl = guardianIDUrl;

        String emailAddress = "sheshegurl.sd@gmail.com";
        String subject = "Guardian ID Validation Request";
        String body = "PLease validate ID for guardian userId: " + userId;

        //Create intent with ACTION_SEND action
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //set intent type to email
        emailIntent.setType("message/rfc822");

        //set recipient address
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});

        //set subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        //set body
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        //start email client activity
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }


    private void linkYouthProfile(User guardian){

        // Ensure Guardian had valid ID before linking youth

        if (!guardian.getGuardianIdValidated()) {
            Toast.makeText(GuardianProfileActivity.this, "ID Validation is required before linking a youth", Toast.LENGTH_SHORT).show();
            return;
        }

        String youthUsername = addYouthET.getText().toString().trim();


        // Query youth in Firebase
        userTable.orderByChild("username").equalTo(youthUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot youthSnapshot : snapshot.getChildren()) {
                        User youthUser = youthSnapshot.getValue(User.class);
                        if (youthUser != null && "Youth Shoveller".equals(youthUser.getAccountType())){
                            // Link youth to guardian account
                            addYouthToGuardianAccount(youthUser, guardian);
                            // Link guardian to Youth account
                            addGuardianToYouthAccount(youthUser, guardian);
                        } else {
                            Toast.makeText(GuardianProfileActivity.this, "Youth username not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(GuardianProfileActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GuardianProfileActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addGuardianToYouthAccount(User youthUser, User guardianUser) {
        System.out.println("Adding guardian: :" + guardianUser.getUsername() + " to" + youthUser.getUsername());

        DatabaseReference linkedYouthUserReference = userTable.child(youthUser.getUserId()).child("linkedUsers");
        Map<String, Object> guardianUserInfo = new HashMap<>();

        guardianUserInfo.put(guardianUser.getUserId(), new HashMap<String, Object>() {{
            put("userId", guardianUser.getUserId());
            put("username", guardianUser.getUsername());
            put("firstName", guardianUser.getFirstName());
            put("lastName", guardianUser.getLastName());
            put("email", guardianUser.getEmail());
            put("phoneNo", guardianUser.getPhoneNo());
            put("guardianIdValidated", guardianUser.getGuardianIdValidated());

        }});

        linkedYouthUserReference.updateChildren(guardianUserInfo).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(GuardianProfileActivity.this, "Guardian successfully linked", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GuardianProfileActivity.this, "Failed to link guardian to youth account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addYouthToGuardianAccount(User youthUser, User guardianUser) {
        System.out.println("Adding guardian: :" + guardianUser.getUsername() + " to" + youthUser.getUsername());

        DatabaseReference linkedGuardianUserReference = userTable.child(guardianUser.getUserId()).child("linkedUsers");
        Map<String, Object> youthUserInfo = new HashMap<>();
        youthUserInfo.put(youthUser.getUserId(), new HashMap<String, Object>(){{
            put("userId", youthUser.getUserId());
            put("username", youthUser.getUsername());
            put("firstName", youthUser.getFirstName());
            put("lastName", youthUser.getEmail());
            put("email", youthUser.getEmail());
            put("phoneNo", youthUser.getPhoneNo());

        }});

        linkedGuardianUserReference.updateChildren(youthUserInfo).addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               Toast.makeText(GuardianProfileActivity.this, "Youth successfully linked", Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(GuardianProfileActivity.this, "Failed to link youth to guardian account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
           }
        });
    }

    //**TODO**
    private void readYouthProfilesFromFirebase(User guardian) {
        System.out.println("userid received by read database: " + guardian);

        userTable.child(guardian.getUserId()).child("linkedUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear linked users in User Class
                guardian.setLinkedUsers(new HashMap<String, User>());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     //Deserialize user object from snapshot
                    User linkedUser = snapshot.getValue(User.class);
                    if (linkedUser != null) {
                        String userId = snapshot.getKey();
                        guardian.addLinkedUser(userId, linkedUser);
                    }
                }

                // Get list of linked users
                List<User> linkedUsers = new ArrayList<>(guardian.getLinkedUsers().values());

                // Update spinner with users
                ArrayAdapter<User> linkedYouthAdapter = new ArrayAdapter<>(GuardianProfileActivity.this, android.R.layout.simple_spinner_item, linkedUsers);
                linkedYouthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                linkedYouthSpinner.setAdapter(linkedYouthAdapter);

                // Enable or disable spinner based on linked users' presence
                linkedYouthSpinner.setEnabled(!linkedUsers.isEmpty());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e(TAG, "Error reading linked users: " + databaseError.getMessage());
            }
        });
    }
    private void retrieveLinkedYouths (User guardianUser){
        List<String> youthUsers = new ArrayList<>();
        for(User youth : guardianUser.getLinkedUsers().values()) {
            youthUsers.add(youth.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, youthUsers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        linkedYouthSpinner.setAdapter(adapter);
    }
}
