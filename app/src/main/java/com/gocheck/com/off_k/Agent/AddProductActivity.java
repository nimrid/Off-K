package com.gocheck.com.off_k.Agent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gocheck.com.off_k.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mAddButton;
    private EditText mPropertyName, mLocation, mDescription, mRent, mContact;
    private ProgressDialog loadingDialog;
//    add posted by

    private static int pick_image = 1;
    private Uri imageUri;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference, mAgentRef;

    private String productIdKey, downloadImageUri;
    String propertyName, location, price, contact, description, categoryName;
    String saveCurrentDate, saveCurrentTime;

//    Agent variable
    private String agentName, agentNumber, agentAddress, agentEmail, agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        mStorageReference = FirebaseStorage.getInstance().getReference().child("Accommodation image");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Accommodation");
        mAgentRef = FirebaseDatabase.getInstance().getReference().child("Agent");

        categoryName = getIntent().getStringExtra("Category");

        mImageView = findViewById(R.id.property_image);
        mPropertyName = findViewById(R.id.property_name);
        mLocation = findViewById(R.id.location);
        mDescription = findViewById(R.id.description);
        mRent = findViewById(R.id.price);
        mContact = findViewById(R.id.contact);
        mAddButton = findViewById(R.id.add_home);
        loadingDialog = new ProgressDialog(this);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

//        Retrieve Agent Info
        mAgentRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            agentName = dataSnapshot.child("Name").getValue().toString();
                            agentEmail = dataSnapshot.child("Email").getValue().toString();
                            agentNumber = dataSnapshot.child("PhoneNumber").getValue().toString();
                            agentAddress = dataSnapshot.child("Address").getValue().toString();
                            agentId = dataSnapshot.child("agentId").getValue().toString();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public static Intent intent(Context context, String category){
        Intent newIntent = new Intent(context, AddProductActivity.class);
        newIntent.putExtra("Category", category);
        return newIntent;
    }

    private void openGallery() {
        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, pick_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pick_image && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            mImageView.setImageURI(imageUri);
        }
    }


    private void validateData() {
        propertyName = mPropertyName.getText().toString();
        location = mLocation.getText().toString().toUpperCase();
        price = mRent.getText().toString();
        contact = mContact.getText().toString();
        description = mDescription.getText().toString();

        if (imageUri == null){
            Toast.makeText(this, "Image required", Toast.LENGTH_SHORT).show();
        }

        if (propertyName.isEmpty()){
            Toast.makeText(this, "Property Name needed", Toast.LENGTH_SHORT).show();
        }

        if (location.isEmpty()){
            Toast.makeText(this, "location needed", Toast.LENGTH_SHORT).show();
        }

        if (price.isEmpty()){
            Toast.makeText(this, "Price needed", Toast.LENGTH_SHORT).show();
        }

        if (contact.isEmpty()){
            Toast.makeText(this, "Contact needed", Toast.LENGTH_SHORT).show();
        }
        if (description.isEmpty()){
            Toast.makeText(this, "Description needed", Toast.LENGTH_SHORT).show();
        }

        storeInfo();
    }

    private void storeInfo() {

        loadingDialog.setTitle("Adding new Home");
        loadingDialog.setMessage("Please wait, Info saving...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productIdKey = saveCurrentDate + saveCurrentTime;
//      store image into firebase storage
        final StorageReference filePath = mStorageReference.child(imageUri.getLastPathSegment() + productIdKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Toast.makeText(AddProductActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductActivity.this, "Property image uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl(); //ERROR
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            downloadImageUri = task.getResult().toString();
                            Toast.makeText(AddProductActivity.this, "got product image", Toast.LENGTH_SHORT).show();
                            saveInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void saveInfoToDatabase() {
        HashMap<String, Object> Accommodation = new HashMap<>();
        Accommodation.put("pId", productIdKey);
        Accommodation.put("Category", categoryName);
        Accommodation.put("Date", saveCurrentDate);
        Accommodation.put("Time", saveCurrentTime);
        Accommodation.put("PropertyName", propertyName);
        Accommodation.put("Description", description);
        Accommodation.put("ImageUri", downloadImageUri);
        Accommodation.put("Location", location);
        Accommodation.put("Rent", price);
        Accommodation.put("Contact", contact);


//      Store agent info with accommodation
        Accommodation.put("AgentName", agentName);
        Accommodation.put("AgentEmail", agentEmail);
        Accommodation.put("AgentPhoneNumber", agentNumber);
        Accommodation.put("AgentAddress", agentAddress);
        Accommodation.put("AgentId", agentId);
        Accommodation.put("ProductStatus", "Not Approved");

        mDatabaseReference.child(productIdKey).updateChildren(Accommodation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                          if successful go to category screen
                            Intent intent = new Intent(AddProductActivity.this, AgentHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            loadingDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Home Uploaded", Toast.LENGTH_SHORT).show();
                        }else {
                            loadingDialog.dismiss();
                            String errorMsg = task.getException().toString();
                            Toast.makeText(AddProductActivity.this, "Error: "+ errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
