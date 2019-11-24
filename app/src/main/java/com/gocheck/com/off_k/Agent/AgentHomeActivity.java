package com.gocheck.com.off_k.Agent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gocheck.com.off_k.Admin.ApproveHomeActivity;
import com.gocheck.com.off_k.MainActivity;
import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.R;
import com.gocheck.com.off_k.ViewHolder.HomeViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AgentHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DatabaseReference unverifiedHomeRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_home);

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        recyclerView = findViewById(R.id.agent_home_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        unverifiedHomeRef = FirebaseDatabase.getInstance().getReference().child("Accommodation");
        navView.setOnNavigationItemSelectedListener(selectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            return false;

            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    return true;

                case R.id.navigation_add:
                    Intent addIntent = new Intent(AgentHomeActivity.this, AgentAddCategoryActivity.class);
                    startActivity(addIntent);
                    return true;

                case R.id.navigation_logout:
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();

                    Intent intent = new Intent(AgentHomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

            }

            return false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Accommodation> options = new FirebaseRecyclerOptions.Builder<Accommodation>()
                .setQuery(unverifiedHomeRef.orderByChild("AgentId")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Accommodation.class)
                .build();


        FirebaseRecyclerAdapter<Accommodation, HomeViewHolder> adapter = new FirebaseRecyclerAdapter<Accommodation, HomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i, @NonNull Accommodation accommodation) {
                homeViewHolder.mHomeName.setText(accommodation.getCategory());
                homeViewHolder.mHomeDescription.setText(accommodation.getDescription());
                homeViewHolder.mHomeRent.setText("N" + accommodation.getRent());
                homeViewHolder.mLocation.setText(accommodation.getLocation());
                homeViewHolder.mVerify.setVisibility(View.VISIBLE);
                homeViewHolder.mVerify.setText(accommodation.getProductStatus());


                Picasso.get().load(accommodation.getImageUri()).into(homeViewHolder.mHomeImage);

                final Accommodation itemClick = accommodation;

                homeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String itemId = itemClick.getpId();
                        CharSequence[] charSequence = new CharSequence[]{"Yes", "No"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(AgentHomeActivity.this);
                        builder.setTitle("Do you want to Delete this Home?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                yes
                                if (which == 0){
                                    deleteHome(itemId);
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

    private void deleteHome(String itemId) {
        unverifiedHomeRef.child(itemId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AgentHomeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
