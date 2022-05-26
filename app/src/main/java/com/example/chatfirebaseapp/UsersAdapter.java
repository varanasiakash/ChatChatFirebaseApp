package com.example.chatfirebaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//adapter created to display the users and profile of them in a recycler view
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

    private ArrayList<User> users;
    private Context context;
    private OnUserClickListener onUserClickListener;

    //constructor of the adapter
    public UsersAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }


    //interface created to open the chat section
    interface OnUserClickListener{
        void OnUserClicked(int position);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_holder,parent,false);
        return new UserHolder(view);
    }


    //for profile image and text to popup
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());
        Glide.with(context).load(users.get(position).getProfile_pic()).error(R.drawable.ic_account)
                .placeholder(R.drawable.ic_account).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView imageView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            //userClickMethod added to adapter position
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.OnUserClicked(getAdapterPosition());
                }
            });

            username = itemView.findViewById(R.id.username_show);
            imageView = itemView.findViewById(R.id.profile_show);

        }
    }
}
