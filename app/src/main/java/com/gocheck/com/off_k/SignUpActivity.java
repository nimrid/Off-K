package com.gocheck.com.off_k;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gocheck.com.off_k.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mName, mPhoneNumber, mPassword, mConfirmPassword, mEmail;
    private ProgressBar mProgressBar;



//    try out
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//  try out
        mAuth = FirebaseAuth.getInstance();

        mRegister = findViewById(R.id.sign_up_btn);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.signup_email);
        mPhoneNumber = findViewById(R.id.sign_up_phone_num);
        mPassword = findViewById(R.id.sign_up_password);
        mConfirmPassword = findViewById(R.id.sign_up_confirm_password);
        mProgressBar = findViewById(R.id.sign_up_progress_bar);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount(){
        String inputName = mName.getText().toString();
        String inputPhoneNum = mPhoneNumber.getText().toString();
        String inputPassword = mPassword.getText().toString();
        String inputConfirmPassword = mConfirmPassword.getText().toString();
        String inputEmail = mEmail.getText().toString();

        if (inputName.isEmpty()){
            mName.setError("Name required");
            mName.requestFocus();
        }

        if (inputPhoneNum.isEmpty()){
            mPhoneNumber.setError("Phone Number required");
            mPhoneNumber.requestFocus();
        }

        if (inputPassword.isEmpty()){
            mPassword.setError("Password required");
            mPassword.requestFocus();
        }

        if (!inputPassword.equals(inputConfirmPassword)){
            mConfirmPassword.setError("Password not match");
            mConfirmPassword.requestFocus();
        }

        if (inputEmail.isEmpty()){
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
            mEmail.setError("Enter Valid Email");
            mEmail.requestFocus();
            return;
        }

//        validateInfo(inputName,inputPhoneNum,inputPassword);
        validateInfo(inputEmail, inputPassword, inputName, inputPhoneNum);

    }

    private void validateInfo(final String email, final String Password, final String Name, final String phoneNumber){

        mProgressBar.setVisibility(View.VISIBLE);
        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() ){

                    String userId = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userId", userId);
                    userInfo.put("Name", Name);
                    userInfo.put("Password", Password);
                    userInfo.put("PhoneNumber", phoneNumber);
                    userInfo.put("Email", email);

                    mDatabaseReference.child(userId).updateChildren(userInfo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "Account Created successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(SignUpActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                }else {
                    Toast.makeText(SignUpActivity.this, "This " + phoneNumber + " already exist", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


















//        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!(dataSnapshot.child("Users").child(phoneNumber).exists()) ){
//
//                    HashMap<String, Object> userInfo = new HashMap<>();
//                    userInfo.put("Name", Name);
//                    userInfo.put("Password", Password);
//                    userInfo.put("PhoneNumber", phoneNumber);
//
//                    mDatabaseReference.child("Users").child(phoneNumber).updateChildren(userInfo)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        mProgressBar.setVisibility(View.GONE);
//                                        Toast.makeText(SignUpActivity.this, "Account Created successfully", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                    }else {
//                                        Toast.makeText(SignUpActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//                                        mProgressBar.setVisibility(View.GONE);
//                                    }
//                                }
//                            });
//
//                }else {
//                    Toast.makeText(SignUpActivity.this, "This " + phoneNumber + " already exist", Toast.LENGTH_SHORT).show();
//                    mProgressBar.setVisibility(View.GONE);
//                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
    }
}
