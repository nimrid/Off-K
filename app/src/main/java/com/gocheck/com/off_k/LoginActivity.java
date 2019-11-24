package com.gocheck.com.off_k;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gocheck.com.off_k.Admin.AdminHomeActivity;
import com.gocheck.com.off_k.Modal.Users;
import com.gocheck.com.off_k.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recombee.api_client.bindings.User;
import com.rey.material.widget.CheckBox;

import java.net.PasswordAuthentication;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private ProgressBar mProgressBar;
    private EditText mPhoneNumber, mPassword;
    private CheckBox chkBox;
    private TextView mNotAdmin, loginAdmin;
    private ImageView imageView;

    private String parentDbName = "Users";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = findViewById(R.id.login_btn);
        mProgressBar = findViewById(R.id.login_progress_bar);
        mPhoneNumber = findViewById(R.id.login_phone_num);
        mPassword = findViewById(R.id.login_password);
        chkBox = findViewById(R.id.remember_me);
        loginAdmin = findViewById(R.id.login_admin);
        mNotAdmin = findViewById(R.id.not_admin);
        imageView = findViewById(R.id.admin_image_btn);

//        getUser = mAuth.getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();


//      used for local storage
        Paper.init(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin.setVisibility(View.VISIBLE);
                mPhoneNumber.setHint("Phone number");
//                mAdmin.setVisibility(View.INVISIBLE);
                mNotAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admin";
            }
        });

        mNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin.setVisibility(View.GONE);
                mPhoneNumber.setHint("Email");
//                mAdmin.setVisibility(View.VISIBLE);
                mNotAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void loginAdmin() {

        final String inputPhoneNumber = mPhoneNumber.getText().toString();
        final String inputPassword = mPassword.getText().toString();
        mProgressBar.setVisibility(View.VISIBLE);

        if (inputPhoneNumber.isEmpty()) {
            mPhoneNumber.setError("Phone Number required");
            mPhoneNumber.requestFocus();
        }

        if (inputPassword.isEmpty()) {
            mPassword.setError("Password required");
            mPassword.requestFocus();
        }

        final DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(inputPhoneNumber).exists()) {

                    Users admin = dataSnapshot.child(inputPhoneNumber).getValue(Users.class);
                    if (admin.getPhoneNumber().equals(inputPhoneNumber) && admin.getPassword().equals(inputPassword)) {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Phone number or password wrong", Toast.LENGTH_SHORT).show();
//
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void LoginUser() {
        String inputPhoneNumber = mPhoneNumber.getText().toString();
        String inputPassword = mPassword.getText().toString();

        if (inputPhoneNumber.isEmpty()) {
            mPhoneNumber.setError("Email required");
            mPhoneNumber.requestFocus();
        }

        if (inputPassword.isEmpty()) {
            mPassword.setError("Password required");
            mPassword.requestFocus();
        }

        validateInfo(inputPhoneNumber, inputPassword);
    }

    private void validateInfo(final String phoneNum, final String Password) {

        if (chkBox.isChecked()) {
            Paper.book().write(Prevalent.userPhoneKey, phoneNum);
            Paper.book().write(Prevalent.userPasswordKey, Password);
        }

        mProgressBar.setVisibility(View.VISIBLE);

        if (parentDbName.equals("Users")) {
            mAuth.signInWithEmailAndPassword(phoneNum, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                Prevalent.currentUser = userData;
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
        }


    }
}