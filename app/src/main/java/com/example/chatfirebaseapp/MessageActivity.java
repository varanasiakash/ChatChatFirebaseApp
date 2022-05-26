package com.example.chatfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerMessage;
    private TextView profileNameMessage;
    private ImageView imageProfile;
    private EditText textMessage;
    private ImageView sendMessage;
    private ProgressBar progressMessage;


    private MessagesAdapter messagesAdapter;
    private ArrayList<Messages> messages;

    String usernameOfTheRoommate;
    String chatRoomId;
    String email_Of_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameOfTheRoommate  = getIntent().getStringExtra("username_of_roommate");
        email_Of_user = getIntent().getStringExtra("email_of_roommate");

        imageProfile = findViewById(R.id.profile_dis);
        sendMessage = findViewById(R.id.sendMessageButton);
        textMessage = findViewById(R.id.messageText);
        profileNameMessage = findViewById(R.id.profileNameMessage);
        recyclerMessage = findViewById(R.id.recyclerMessage);
        progressMessage = findViewById(R.id.progress_message);

        messages = new ArrayList<>();

        profileNameMessage.setText(usernameOfTheRoommate);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push()
                        .setValue(new Messages(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                                ,email_Of_user,textMessage.getText().toString()));
                textMessage.setText("");
            }
        });


        messagesAdapter = new MessagesAdapter(messages,getIntent().getStringExtra("My_image"),
                getIntent().getStringExtra("image_of_roommate"),MessageActivity.this);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("image_of_roommate"))
                .placeholder(R.drawable.ic_account).error(R.drawable.ic_account).into(imageProfile);

        recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessage.setAdapter(messagesAdapter);


        setupChatRoom();
    }

    private void setupChatRoom()
    {
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String myUsername = snapshot.getValue(User.class).getUsername();
                        if (usernameOfTheRoommate.compareTo(myUsername)>0)
                        {
                            chatRoomId  = myUsername + usernameOfTheRoommate;
                        }
                        else if (usernameOfTheRoommate.compareTo(myUsername)==0)
                        {
                            chatRoomId  = myUsername + usernameOfTheRoommate;
                        }
                        else
                        {
                            chatRoomId  = usernameOfTheRoommate + myUsername;
                        }
                        attachMessageListener(chatRoomId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void attachMessageListener(String chatRoomId)
    {
        FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    //gets triggered when new messages arrive
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //array list messages
                        messages.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            messages.add(dataSnapshot.getValue(Messages.class));
                        }
                        messagesAdapter.notifyDataSetChanged();
                        recyclerMessage.scrollToPosition(messages.size() -1);
                        recyclerMessage.setVisibility(View.VISIBLE);
                        progressMessage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}