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
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.ViewHolder.HomeViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private EditText searchText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.search_accomm);
        searchButton = findViewById(R.id.search_btn);
        recyclerView = findViewById(R.id.search_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            searchInput = searchText.getText().toString();
            onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Accommodation");
        FirebaseRecyclerOptions<Accommodation> options = new FirebaseRecyclerOptions.Builder<Accommodation>()
                .setQuery(mDatabaseReference.orderByChild("PropertyName").startAt(searchInput), Accommodation.class)
                .build();

        FirebaseRecyclerAdapter<Accommodation, HomeViewHolder> recyclerAdapter =
                new FirebaseRecyclerAdapter<Accommodation, HomeViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i, @NonNull final Accommodation accommodation) {
                        homeViewHolder.mHomeName.setText(accommodation.getPropertyName());
                        homeViewHolder.mHomeDescription.setText(accommodation.getDescription());
                        homeViewHolder.mHomeRent.setText("Price: " + accommodation.getRent());
                        homeViewHolder.mLocation.setText("Located: "+accommodation.getLocation());
                        Picasso.get().load(accommodation.getImageUri()).into(homeViewHolder.mHomeImage);

                        homeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchActivity.this, AccommodationDetailActivity.class);
                                intent.putExtra("pId", accommodation.getpId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_list, parent, false);
                        HomeViewHolder holder = new HomeViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }
}
