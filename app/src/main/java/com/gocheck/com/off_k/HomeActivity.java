package com.gocheck.com.off_k;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gocheck.com.off_k.Agent.AgentRegisterActivity;
import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.Modal.LikedAccom;
import com.gocheck.com.off_k.Modal.Users;
import com.gocheck.com.off_k.ViewHolder.HomeViewHolder;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.gocheck.com.off_k.ViewHolder.recommendItemHolder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView usernameHeader;

    private RecyclerView recommend_recycler;
    private RecyclerView.LayoutManager recLayoutManager;
    private RecyclerView.Adapter adapter;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference savedItemDataRef;
    private DatabaseReference userDatabaseReference;

//    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Accommodation");
        savedItemDataRef = FirebaseDatabase.getInstance().getReference().child("Saved Accommodation");

        //        View headerView = navigationView.getHeaderView(0);
        usernameHeader = findViewById(R.id.user_profile_name);
        final CircleImageView circleImageView = findViewById(R.id.profile_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

//      initial recommender recyler
        recommend_recycler = findViewById(R.id.recycler_recommend);
        recommend_recycler.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recommend_recycler.setLayoutManager(recLayoutManager);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.bottom_nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        try out
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(firebaseUser.getUid());

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Users users = dataSnapshot.getValue(Users.class);
//                    usernameHeader.setText( users.getName() );
//                    Picasso.get().load(users.getImage()).into(circleImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //using firebase recyclerView, get homes
        FirebaseRecyclerOptions<Accommodation> options = new FirebaseRecyclerOptions.Builder<Accommodation>()
                .setQuery(mDatabaseReference.orderByChild("ProductStatus").equalTo("Approved"), Accommodation.class).build();

        FirebaseRecyclerAdapter<Accommodation, HomeViewHolder> recyclerAdapter =
                new FirebaseRecyclerAdapter<Accommodation, HomeViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i, @NonNull final Accommodation accommodation) {
                        homeViewHolder.mHomeName.setText(accommodation.getCategory());
                        homeViewHolder.mHomeDescription.setText(accommodation.getDescription());
                        homeViewHolder.mHomeRent.setText("N" + accommodation.getRent());
                        homeViewHolder.mLocation.setText(accommodation.getLocation());
                        Picasso.get().load(accommodation.getImageUri()).into(homeViewHolder.mHomeImage);

                        homeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, AccommodationDetailActivity.class);
                                intent.putExtra("pId", accommodation.getpId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_list, parent, false);
                        return new HomeViewHolder(view);
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();

//        recommender();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_recommend){
            Intent intent = new Intent(HomeActivity.this, LikeItemActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home){

        }else if (id == R.id.nav_seller){
            Intent intent = new Intent(HomeActivity.this, AgentRegisterActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_search){
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_setting){
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_logout){
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void recommender(){


        FirebaseRecyclerOptions<LikedAccom> options = new FirebaseRecyclerOptions.Builder<LikedAccom>()
                .setQuery(mDatabaseReference.orderByChild("ProductStatus").equalTo("Approved"), LikedAccom.class).build();

        FirebaseRecyclerAdapter<LikedAccom, recommendItemHolder> recyclerAdapter =
            new FirebaseRecyclerAdapter<LikedAccom, recommendItemHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull recommendItemHolder homeViewHolder, int i, @NonNull final LikedAccom accommodation) {
                    homeViewHolder.mHomeName.setText(accommodation.getCategory());
                    homeViewHolder.mHomeRent.setText("N" + accommodation.getRent());
                    homeViewHolder.mLocation.setText(accommodation.getLocation());
                    Picasso.get().load(accommodation.getImage()).into(homeViewHolder.mHomeImage);

                    homeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeActivity.this, AccommodationDetailActivity.class);
                            intent.putExtra("pId", accommodation.getpId());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public recommendItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_list, parent, false);
                    recommendItemHolder holder = new recommendItemHolder(view);
                    return holder;
                }
            };
        recommend_recycler.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();

//        String myDb = mDatabaseReference.getKey();
//        final String[] itemId = new String[1];
//        String userId = userDatabaseReference.getKey();
//
//        List<Accommodation> mTourList = new ArrayList<>();
//        adapter = new recommendItemHolder.Adapter(getApplicationContext(), mTourList);
//        recommend_recycler.setAdapter(adapter);
//
//        mDatabaseReference.orderByChild("ProductStatus").equalTo("Approved")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        itemId[0] = dataSnapshot.child("pid").getKey();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//        RecombeeClient client = new RecombeeClient(myDb, "fuyHMg9pqRU5yJHwR90ytfbWb6P4QYAHpr8PBZ3f0wm2nhZo47JOUTqz7ukiYDdO");
//        try {
//            client.send(new AddDetailView(userId, itemId[0]));
//
//
//            RecommendationResponse recommended = client.send(new RecommendItemsToUser(userId, 5));
//            for(Recommendation r: recommended)
//            {
//                r.getId();
//
//            }
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }



    }



}
