package com.gocheck.com.off_k;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AccommodationDetailActivity extends AppCompatActivity {
    private TextView mCategory, mLocation, mDate, mContact, mDescription, mRent;
    private Button mSave;
    private ImageView mDetailAccomPic;
    private RatingBar ratingBar;

    private String accommodationId = "";
//    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_detail);
        accommodationId = getIntent().getStringExtra("pId");

        mDetailAccomPic = findViewById(R.id.detail_accommodation_pic);
        mCategory = findViewById(R.id.detail_category);
        mLocation = findViewById(R.id.detail_location);
        mDate = findViewById(R.id.detail_date);
        mContact = findViewById(R.id.detail_contact);
        mDescription = findViewById(R.id.detail_description);
        mRent = findViewById(R.id.detail_rent);
        mSave = findViewById(R.id.detail_save);
        ratingBar = findViewById(R.id.detail_rating);

        getAccommodationDetails(accommodationId);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewLater();
            }
        });

    }

    private void getAccommodationDetails(String accommodationId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Accommodation");

        databaseReference.child(accommodationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Accommodation accommodation = dataSnapshot.getValue(Accommodation.class);
                    mCategory.setText(accommodation.getCategory());
                    mLocation.setText(accommodation.getLocation());
                    mDate.setText("Date posted: "+ accommodation.getDate());
                    mDescription.setText(accommodation.getDescription());
                    mRent.setText(accommodation.getRent());
                    mContact.setText(accommodation.getContact());
                    Picasso.get().load(accommodation.getImageUri()).into(mDetailAccomPic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ViewLater() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference saveForLater = FirebaseDatabase.getInstance().getReference().child("LikedAccommodation");

        final HashMap<String, Object> saveAccommodation = new HashMap<>();
        saveAccommodation.put("pId", accommodationId);
        saveAccommodation.put("Category", mCategory.getText());
        saveAccommodation.put("Date", saveCurrentDate);
        saveAccommodation.put("Time", saveCurrentTime);
        saveAccommodation.put("Description", mDescription.getText().toString());
        saveAccommodation.put("Location", mLocation.getText().toString());
        saveAccommodation.put("Rent", mRent.getText().toString());
        saveAccommodation.put("Contact", mContact.getText().toString());
        saveAccommodation.put("Rated", ratingBar.getRating());


        saveForLater.updateChildren(saveAccommodation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AccommodationDetailActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

/**
        saveForLater.child("User Viewed").child(Prevalent.currentUser.getPhoneNumber()).child("Accommodation").child(accommodationId)
                .updateChildren(saveAccommodation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            // for admin to be able to view the specific home a user likes
                            saveForLater.child("Admin View").child(Prevalent.currentUser.getPhoneNumber())
                                    .child("Accommodation").child(accommodationId)
                                    .updateChildren(saveAccommodation)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(AccommodationDetailActivity.this, "Liked", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                    }
                });
 */


    }
}
