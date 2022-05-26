package com.example.chatfirebaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {

    private ArrayList<Messages> messages;
    private String senderImg , receiverImg;
    private Context context;

    public MessagesAdapter(ArrayList<Messages> messages, String senderImg, String receiverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.textMessage.setText(messages.get(position).getContent());

        ConstraintLayout constraintLayout = holder.ccLayout;

        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
        {
            Glide.with(context).load(senderImg).error(R.drawable.ic_account).placeholder(R.drawable.ic_account)
                    .into(holder.profImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.cardViewMessages,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txtMessageContent,ConstraintSet.LEFT);
            constraintSet.connect(R.id.cardViewMessages,ConstraintSet.RIGHT,R.id.ccLayoutConstraint,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txtMessageContent,ConstraintSet.RIGHT,R.id.cardViewMessages,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);
        }
        else
        {
            Glide.with(context).load(receiverImg).error(R.drawable.ic_account).placeholder(R.drawable.ic_account)
                    .into(holder.profImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.cardViewMessages,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txtMessageContent,ConstraintSet.RIGHT);
            constraintSet.connect(R.id.cardViewMessages,ConstraintSet.LEFT,R.id.ccLayoutConstraint,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txtMessageContent,ConstraintSet.LEFT,R.id.cardViewMessages,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{

        ConstraintLayout ccLayout;
        TextView textMessage;
        ImageView profImage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            ccLayout = itemView.findViewById(R.id.ccLayoutConstraint);
            textMessage = itemView.findViewById(R.id.txtMessageContent);
            profImage = itemView.findViewById(R.id.small_profile);
        }
    }
}
