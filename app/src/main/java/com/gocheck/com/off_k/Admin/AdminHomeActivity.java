package com.gocheck.com.off_k.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gocheck.com.off_k.Agent.AddProductActivity;
import com.gocheck.com.off_k.MainActivity;
import com.gocheck.com.off_k.R;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView mBungalow, mContainerHouse, mDuplex, mFlat, mTownHouse, mWoodenhouse;
    private Button mLogout, mApproveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mBungalow = findViewById(R.id.bungalow);
        mContainerHouse = findViewById(R.id.container_house);
        mDuplex = findViewById(R.id.duplex);
        mFlat = findViewById(R.id.flat);
        mTownHouse = findViewById(R.id.town_house);
        mWoodenhouse = findViewById(R.id.wooden_house);
        mLogout = findViewById(R.id.logout_admin);
        mApproveButton = findViewById(R.id.approve_item);

        final String bungalow = mBungalow.getText().toString();
        final String containerHouse = mContainerHouse.getText().toString();
        final String duplex = mDuplex.getText().toString();
        final String flat = mFlat.getText().toString();
        final String townHall = mTownHouse.getText().toString();
        final String woodenHouse = mWoodenhouse.getText().toString();

        mBungalow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = AddProductActivity.intent(AdminHomeActivity.this, bungalow);
            startActivity(intent);
            }
        });

        mContainerHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AdminHomeActivity.this, containerHouse);
                startActivity(intent);
            }
        });

        mDuplex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AdminHomeActivity.this, duplex);
                startActivity(intent);
            }
        });

        mFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AdminHomeActivity.this, flat);
                startActivity(intent);
            }
        });

        mTownHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AdminHomeActivity.this, townHall);
                startActivity(intent);
            }
        });

        mWoodenhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AdminHomeActivity.this, woodenHouse);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        mApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, ApproveHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
