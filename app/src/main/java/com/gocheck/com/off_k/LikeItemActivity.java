package com.gocheck.com.off_k;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.Modal.LikedAccom;
import com.gocheck.com.off_k.ViewHolder.HomeViewHolder;
import com.gocheck.com.off_k.ViewHolder.LikedItemViewHolder;
import com.gocheck.com.off_k.ViewHolder.recommendItemHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class LikeItemActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_item);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Accommodation");

        recyclerView = findViewById(R.id.liked_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //using firebase recyclerView
        FirebaseRecyclerOptions<LikedAccom> options = new FirebaseRecyclerOptions.Builder<LikedAccom>()
                .setQuery(mDatabaseReference.orderByChild("Location").startAt("N").limitToFirst(7), LikedAccom.class).build();

        FirebaseRecyclerAdapter<LikedAccom, LikedItemViewHolder> recyclerAdapter =
                new FirebaseRecyclerAdapter<LikedAccom, LikedItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LikedItemViewHolder homeViewHolder, int i, @NonNull final LikedAccom accommodation) {
                        homeViewHolder.mHomeCategory.setText(accommodation.getCategory());
                        homeViewHolder.mHomeRent.setText("N" + accommodation.getRent());
                        homeViewHolder.mHomeDescription.setText(accommodation.getDescription());
                        homeViewHolder.mLocation.setText(accommodation.getLocation());
//                        Picasso.get().load(accommodation.getImage()).into(homeViewHolder.mHomeImage);

                        homeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(LikeItemActivity.this, AccommodationDetailActivity.class);
                                intent.putExtra("pId", accommodation.getpId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public LikedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_item_list, parent, false);
                        LikedItemViewHolder holder = new LikedItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }
}
