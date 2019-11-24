package com.gocheck.com.off_k.Agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gocheck.com.off_k.R;

public class AgentAddCategoryActivity extends AppCompatActivity {

    private TextView mBungalow, mContainerHouse, mDuplex, mFlat, mTownHouse, mWoodenhouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_category);

        mBungalow = findViewById(R.id.bungalow);
        mContainerHouse = findViewById(R.id.container_house);
        mDuplex = findViewById(R.id.duplex);
        mFlat = findViewById(R.id.flat);
        mTownHouse = findViewById(R.id.town_house);
        mWoodenhouse = findViewById(R.id.wooden_house);

        final String bungalow = mBungalow.getText().toString();
        final String containerHouse = mContainerHouse.getText().toString();
        final String duplex = mDuplex.getText().toString();
        final String flat = mFlat.getText().toString();
        final String townHall = mTownHouse.getText().toString();
        final String woodenHouse = mWoodenhouse.getText().toString();

        mBungalow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = AddProductActivity.intent(AgentAddCategoryActivity.this, bungalow);
            startActivity(intent);
            }
        });

        mContainerHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AgentAddCategoryActivity.this, containerHouse);
                startActivity(intent);
            }
        });

        mDuplex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AgentAddCategoryActivity.this, duplex);
                startActivity(intent);
            }
        });

        mFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AgentAddCategoryActivity.this, flat);
                startActivity(intent);
            }
        });

        mTownHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AgentAddCategoryActivity.this, townHall);
                startActivity(intent);
            }
        });

        mWoodenhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddProductActivity.intent(AgentAddCategoryActivity.this, woodenHouse);
                startActivity(intent);
            }
        });
    }
}
