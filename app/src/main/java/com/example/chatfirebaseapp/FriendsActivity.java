package com.example.chatfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private UsersAdapter usersAdapter;
    UsersAdapter.OnUserClickListener onUserClickListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    String myImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView = findViewById(R.id.RecyclerView);
        progressBar = findViewById(R.id.progress_bar);

        swipeRefreshLayout = findViewById(R.id.swiper);

        users = new ArrayList<>();

        //when swipes and refreshes
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //to open the chat area
        onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void OnUserClicked(int position) {
               startActivity(new Intent(FriendsActivity.this,MessageActivity.class)
                       .putExtra("username_of_roommate",users.get(position).getUsername())
                       .putExtra("email_of_roommate",users.get(position).getEmailID())
                       .putExtra("image_of_roommate",users.get(position).getProfile_pic())
                       .putExtra("My_image",myImageUrl));
            }
        };

        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.profile_button)
        {
            startActivity(new Intent(FriendsActivity.this,ProfilePage.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUsers()
    {
        //get the details of friends list and show
        users.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    users.add(dataSnapshot.getValue(User.class));
                }
                usersAdapter = new UsersAdapter(users,FriendsActivity.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                recyclerView.setAdapter(usersAdapter);

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);


                //for checking whether it is the host user himself or not
                for (User user : users)
                {
                    if (user.getEmailID().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    {
                        myImageUrl = user.getProfile_pic();
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}