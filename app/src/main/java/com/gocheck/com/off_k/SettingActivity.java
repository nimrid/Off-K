package com.gocheck.com.off_k;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gocheck.com.off_k.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView mCloseBtn, mUpdateBtn, changeProfileImage;
    private EditText changeUserName, changePhoneNumber, mAddAdress;
    private EditText changePassword;

    private Uri profileImageUri;
    private String myUri = "";
    private StorageReference mStorageReference;
    private StorageTask storageTask;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile Image");

        profileImage = findViewById(R.id.profile_image);
        changeUserName = findViewById(R.id.change_user_name);
        changePhoneNumber = findViewById(R.id.change_phone_num);
//        changePassword = findViewById(R.id.change_password);
        changeProfileImage = findViewById(R.id.change_profile_image);
        mAddAdress = findViewById(R.id.change_address);
        mCloseBtn = findViewById(R.id.close);
        mUpdateBtn = findViewById(R.id.update_setting);

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              if users clicked on this execute, saveUserInfo
                checker = "clicked";
//  start cropping activity
                CropImage.activity(profileImageUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    saveUserInfo();
                }else {
                    updateUserInfo();
                }
            }
        });


//changePassword,
        changeInfo(profileImage, changeUserName, changePhoneNumber, mAddAdress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            profileImageUri = result.getUri();

            profileImage.setImageURI(profileImageUri);
        }else {
            Toast.makeText(SettingActivity.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }
    }

    private void updateUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("Name", changeUserName.getText().toString());
        userInfo.put("Password", changePassword.getText().toString());
        userInfo.put("PhoneNumber", changePhoneNumber.getText().toString());
        userInfo.put("Address", mAddAdress.getText().toString());

        reference.child(Prevalent.currentUser.getPhoneNumber()).updateChildren(userInfo);

        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
        Toast.makeText(SettingActivity.this, "Info Uploaded", Toast.LENGTH_SHORT).show();
        finish();

    }
// save image to cloud storage
    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Image Upload");
        progressDialog.setMessage("Uploading profile image");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (profileImageUri != null){
            final StorageReference storageReference = mStorageReference.child(Prevalent.currentUser.getPhoneNumber() + ".jpg");
            storageTask = storageReference.putFile(profileImageUri);

            storageTask.continueWith(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()){
                        throw task.getException();

                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        myUri = uri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Name", changeUserName.getText().toString());
                        userInfo.put("Password", changePassword.getText().toString());
                        userInfo.put("PhoneNumber", changePhoneNumber.getText().toString());
                        userInfo.put("ProfileImage", myUri);
                        userInfo.put("Address", mAddAdress.getText().toString());

                        reference.child(Prevalent.currentUser.getPhoneNumber()).updateChildren(userInfo);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        Toast.makeText(SettingActivity.this, "Info Uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "Info not Uploaded, Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(SettingActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveUserInfo() {
        if (TextUtils.isEmpty(changeUserName.getText().toString())){
            Toast.makeText(SettingActivity.this, "Name mandatory", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(changePhoneNumber.getText().toString())){
            Toast.makeText(SettingActivity.this, "Number mandatory", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(changePassword.getText().toString())){
            Toast.makeText(SettingActivity.this, "password mandatory", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(mAddAdress.getText().toString())){
            Toast.makeText(SettingActivity.this, "Address mandatory", Toast.LENGTH_SHORT).show();
        }

        if (checker.equals("clicked")){
            uploadImage();
        }
    }

//final EditText changePassword, Prevalent.currentUser.getPhoneNumber()
    private void changeInfo(final CircleImageView changeProfileImage, final EditText changeUserName,
                            final EditText changePhoneNumber, final EditText addAddress) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.userPhoneKey);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("profileImage").exists()){
                        String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                        String name = dataSnapshot.child("Name").getValue().toString();
//                        String password = dataSnapshot.child("Password").getValue().toString();
                        String phone = dataSnapshot.child("PhoneNumber").getValue().toString();
                        String address = dataSnapshot.child("Address").getValue().toString();

                        Picasso.get().load(profileImage).into(changeProfileImage);
                        changeUserName.setText(name);
                        changePhoneNumber.setText(phone);
//                        changePassword.setText(password);
                        addAddress.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
