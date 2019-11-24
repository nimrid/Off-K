package com.gocheck.com.off_k.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gocheck.com.off_k.Interface.itemClickListener;
import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.R;
import com.gocheck.com.off_k.ViewHolder.HomeViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ApproveHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DatabaseReference verifyHomeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_home);

        recyclerView = findViewById(R.id.reclycler_approve);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        verifyHomeRef = FirebaseDatabase.getInstance().getReference().child("Accommodation");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Accommodation> options = new FirebaseRecyclerOptions.Builder<Accommodation>()
                .setQuery(verifyHomeRef.orderByChild("ProductStatus").equalTo("Not Approved"), Accommodation.class)
                .build();


        FirebaseRecyclerAdapter<Accommodation, HomeViewHolder> adapter = new FirebaseRecyclerAdapter<Accommodation, HomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i, @NonNull Accommodation accommodation) {
                homeViewHolder.mHomeName.setText(accommodation.getCategory());
                homeViewHolder.mHomeDescription.setText(accommodation.getDescription());
                homeViewHolder.mHomeRent.setText("N" + accommodation.getRent());
                homeViewHolder.mLocation.setText(accommodation.getLocation());
                Picasso.get().load(accommodation.getImageUri()).into(homeViewHolder.mHomeImage);

                final Accommodation itemClick = accommodation;

                homeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String itemId = itemClick.getpId();
                        CharSequence[] charSequence = new CharSequence[]{"Yes", "No"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(ApproveHomeActivity.this);
                        builder.setTitle("Want to approve this Home?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                yes
                                if (which == 0){
                                    changeItemStatus(itemId);
                                }
//                                No
                                if (which == 1){

                                }
                            }
                        });
                        builder.show();
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

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeItemStatus(String itemId) {

        verifyHomeRef.child(itemId).child("ProductStatus").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ApproveHomeActivity.this, "Home Approved", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
