package com.example.shovelheroapp.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.User;
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

    //initialize ShovelHeroDB (Firebase)
    DatabaseReference userTable;
    private TextView usernameTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView emailTV;
    private TextView phoneTV;


    private Spinner addressSpinner;
    private Spinner linkedYouthSpinner;

    private User currentUser;
    private String userId;

    //Link youth
    private List<User> linkedYouthList;

    EditText addYouthET;


    //guardian ID
    private ImageButton btnAddIDPicture;
    private Uri guardianIdImageUri;
    private ActivityResultLauncher<Intent> iDImageLauncher;



    //buttons
    Button btnAddYouth;
    Button btnViewYouthProfile;
    Button btnViewRatings;
    Button btnViewJobs;
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
                        //readYouthProfilesFromFirebase(user);


                        if(user.getGuardianIdValidated() == false){
                            System.out.println("Please add your Picture ID to get started");
                            Toast.makeText(GuardianProfileActivity.this, "Valid Photo ID is required to add a Youth Shoveller to your profile", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            readYouthProfilesFromFirebase(user);
                        }



                        //ADD YOUTH BUTTON
                        btnAddYouth.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //if(user.getGuardianIdValidated()){
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
                    String addressNotes = (String) addressMap.get("addressNotes");
                    int drivewaySquareFootage = ((Long) addressMap.get("drivewaySquareFootage")).intValue();
                    String accessible = (String) addressMap.get("accessible");
                    String shovelAvailable = (String) addressMap.get("shovelAvailable");

                    // Create new Address object
                    Address addressObject = new Address(addressId, address, city, province, postalCode, country, addressNotes, drivewaySquareFootage, accessible, shovelAvailable);

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

        //set recipent address
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});

        //set subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        //set body
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        //start email client activity
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }


    private void linkYouthProfile(User guardian){
        String youthUsername = addYouthET.getText().toString();

        //CHECK IF USERNAME EXISTS
        userTable.orderByChild("username").equalTo(youthUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                                User youthUser = userSnapShot.getValue(User.class);

                                //CHECK THAT USERNAME = YOUTH ACCT
                                if (youthUser.getAccountType() == "Youth Shoveller") {

                                    //readYouthProfilesFromFirebase(youthUser);
                                    addGuardianToYouthAccount(youthUser, guardian);
                                    addYouthToGuardianAccount(youthUser, guardian);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void addGuardianToYouthAccount(User youthUser, User guardianUser) {
        System.out.println("Adding guardian: :" + guardianUser.getUsername() + " to" + youthUser.getUsername());

        userTable.child(youthUser.getUserId()).child("linkedusers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the addresses field in the User class
                youthUser.setAddresses(new HashMap<String, Address>());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //manually deserialize the HashMap
                    Map<String, Object> youthUserMap = (Map<String, Object>) snapshot.getValue();

                    // retrieve values from the Hashmap
                    String userId = (String) youthUserMap.get("userId");
                    String accountType = (String) youthUserMap.get("accountType");
                    String username = (String) youthUserMap.get("username");
                    String password = "Hidden";
                    String firstName = (String) youthUserMap.get("firstName");
                    String lastName = (String) youthUserMap.get("lastName");
                    String birthdate = (String) youthUserMap.get("birthdate");
                    String email = (String) youthUserMap.get("email");
                    String phoneNo = (String) youthUserMap.get("phoneNo");
                    String validatedId = (String) youthUserMap.get("guardianIdValidated");
                    String profileIMageURL = (String) youthUserMap.get("profilePictureUrl");

                    // Create new User object
                    User userObject = new User(userId, accountType, username, password, firstName, lastName, birthdate, email, phoneNo);

                    // Add the User object to the linkedUsers HashMap in User model
                    youthUser.addLinkedUser(userId, userObject);

                    DatabaseReference linkedUserReference = userTable.child(youthUser.getUserId()).child("linkedusers").child(youthUser.getUserId());
                    Map<String, Object> updateGuardianInfo = new HashMap<>();
                    updateGuardianInfo.put("guardianIdValidated", validatedId);
                    updateGuardianInfo.put("profilePictureUrl", profileIMageURL);

                    linkedUserReference.updateChildren(updateGuardianInfo)
                            .addOnSuccessListener(aVoid -> Toast.makeText(GuardianProfileActivity.this, "Guardian Validation and Profile Pic added to youth profile successfully", Toast.LENGTH_SHORT).show())
                            //.addOnFailureListener(e -> updateGuardianInfo.clear())
                            .addOnFailureListener(e -> Toast.makeText(GuardianProfileActivity.this, "Unable to add validated : " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                // Retrieve list of linkedUsers from Hashmap
                List<User> linkedUsers = new ArrayList<>(youthUser.getLinkedUsers().values());

                // Update the Spinner with linked youth(s)
                ArrayAdapter<User> linkedYouthAdapter = new ArrayAdapter<>(GuardianProfileActivity.this, android.R.layout.simple_spinner_item, linkedUsers);
                linkedYouthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                linkedYouthSpinner.setAdapter(linkedYouthAdapter);

                // Enable or disable the youth spinner button based on the presence of linked User
                linkedYouthSpinner.setEnabled(!linkedUsers.isEmpty());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void addYouthToGuardianAccount(User youthUser, User guardianUser) {
        System.out.println("Adding guardian: :" + guardianUser.getUsername() + " to" + youthUser.getUsername());

        userTable.child(guardianUser.getUserId()).child("linkedusers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the addresses field in the User class
                guardianUser.setAddresses(new HashMap<String, Address>());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //manually deserialize the HashMap
                    Map<String, Object> youthUserMap = (Map<String, Object>) snapshot.getValue();

                    // retrieve values from the Hashmap
                    String userId = (String) youthUserMap.get("userId");
                    String accountType = (String) youthUserMap.get("accountType");
                    String username = (String) youthUserMap.get("username");
                    String password = "Hidden";
                    String firstName = (String) youthUserMap.get("firstName");
                    String lastName = (String) youthUserMap.get("lastName");
                    String birthdate = (String) youthUserMap.get("birthdate");
                    String email = (String) youthUserMap.get("email");
                    String phoneNo = (String) youthUserMap.get("phoneNo");
                    String validatedId = (String) youthUserMap.get("guardianIdValidated");
                    String profileIMageURL = (String) youthUserMap.get("profilePictureUrl");

                    // Create new User object
                    User youthObject = new User(userId, accountType, username, password, firstName, lastName, birthdate, email, phoneNo);

                    // Add the User object to the linkedUsers HashMap in User model
                    guardianUser.addLinkedUser(userId, youthObject);

                    DatabaseReference linkedUserReference = userTable.child(guardianUser.getUserId()).child("linkedusers").child(youthObject.getUserId());
                    Map<String, Object> updateGuardianInfo = new HashMap<>();
                    updateGuardianInfo.put("guardianIdValidated", validatedId);
                    updateGuardianInfo.put("profilePictureUrl", profileIMageURL);

                    linkedUserReference.updateChildren(updateGuardianInfo)
                            .addOnSuccessListener(aVoid -> Toast.makeText(GuardianProfileActivity.this, "Guardian Validation and Profile Pic added to youth profile successfully", Toast.LENGTH_SHORT).show())
                            //.addOnFailureListener(e -> updateGuardianInfo.clear())
                            .addOnFailureListener(e -> Toast.makeText(GuardianProfileActivity.this, "Unable to add validated : " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                // Retrieve list of linkedUsers from Hashmap
                List<User> linkedUsers = new ArrayList<>(guardianUser.getLinkedUsers().values());

                // Update the Spinner with linked youth(s)
                ArrayAdapter<User> linkedYouthAdapter = new ArrayAdapter<>(GuardianProfileActivity.this, android.R.layout.simple_spinner_item, linkedUsers);
                linkedYouthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                linkedYouthSpinner.setAdapter(linkedYouthAdapter);

                // Enable or disable the youth spinner button based on the presence of linked User
                linkedYouthSpinner.setEnabled(!linkedUsers.isEmpty());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    //**TODO**
    private void readYouthProfilesFromFirebase(User guardian) {
        System.out.println("userid received by read database: " + guardian);

        userTable.child(guardian.getUserId()).child("linkedusers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the addresses field in the User class
                guardian.setLinkedUsers(new HashMap<String, User>());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //manually deserialize the HashMap
                    Map<String, Object> linkedUsersMap = (Map<String, Object>) snapshot.getValue();

                    // retrieve values from the Hashmap
                    String userId = (String) linkedUsersMap.get("addressId");
                    String accountType = (String) linkedUsersMap.get("address");
                    String username = (String) linkedUsersMap.get("city");
                    String password = (String) linkedUsersMap.get("province");
                    String firstName = (String) linkedUsersMap.get("postalCode");
                    String lastName = (String) linkedUsersMap.get("country");
                    String birthdate = (String) linkedUsersMap.get("addressNotes");
                    String email = (String) linkedUsersMap.get("accessible");
                    String phoneNo = (String) linkedUsersMap.get("shovelAvailable");
                    String profileIMageURL = (String) linkedUsersMap.get("profilePictureUrl");

                    // Create new Address object
                    User linkedUserObject = new User(userId, accountType, username, password, firstName, lastName, birthdate, email, phoneNo);

                    // Add the Address object to the addresses HashMap in User model
                    guardian.addLinkedUser(userId, linkedUserObject);
                }

                // Retrieve list of linkedYouths from Hashmap
                List<User> linkedUsers = new ArrayList<>(guardian.getLinkedUsers().values());

                // Update the Spinner with addresses
                ArrayAdapter<User> linkedYouthAdapter = new ArrayAdapter<>(GuardianProfileActivity.this, android.R.layout.simple_spinner_item, linkedUsers);
                linkedYouthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                linkedYouthSpinner.setAdapter(linkedYouthAdapter);

                // Enable or disable the "Create Work Order" button based on the presence of addresses
                linkedYouthSpinner.setEnabled(!linkedUsers.isEmpty());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
