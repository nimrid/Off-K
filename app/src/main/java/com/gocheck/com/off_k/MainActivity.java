package com.gocheck.com.off_k;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gocheck.com.off_k.Modal.Users;
import com.gocheck.com.off_k.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
//    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        Button join_now = findViewById(R.id.join);
        Button Sign_in = findViewById(R.id.btn_already_have_acc);

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        join_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
//      Get user info stored locally on device
        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPassword = Paper.book().read(Prevalent.userPasswordKey);
//      login user automatically
        if (userPhoneKey != null && userPassword != null){
            if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPassword)){
                AllowAcess(userPhoneKey, userPassword);
            }
        }
    }

    private void AllowAcess(final String phoneNum, final String Password) {
        final DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child(phoneNum).exists()){
//                   Store user info in model class
                    Users userData = dataSnapshot.child("Users").child(phoneNum).getValue(Users.class);
//                   Retrieve userInfo from cloud

                    if (userData.getPhoneNumber().equals(phoneNum) ){

                        if (userData.getPassword().equals(Password)) {
//                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentUser = userData;
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "Please login", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
//                        mProgressBar.setVisibility(View.GONE);
                    }

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
