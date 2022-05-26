package com.example.chatfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText username, password, emailID;
    Button signIn;
    TextView AskLogin;

    //this is used for signIn page and login page directions
    //not necessary in Xpense
    private boolean SignCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        emailID = findViewById(R.id.emailID);

        signIn = findViewById(R.id.SignIn);

        AskLogin = findViewById(R.id.AskLogin);

        //Used when already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(MainActivity.this,FriendsActivity.class));
            finish();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Determine whether all the details are filled or not
                if (emailID.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                {
                    if (SignCode && username.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (SignCode)
                {
                    handleLogIn();
                }else
                {
                    handleSignIn();
                }
            }
        });

        AskLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SignCode)
                {
                    SignCode = false;
                    signIn.setText("Log In");
                    username.setVisibility(View.GONE);
                    AskLogin.setText("Already have an account? SignUp");

                }else
                {
                    SignCode = true;
                    signIn.setText("Sign Up");
                    username.setVisibility(View.VISIBLE);
                    AskLogin.setText("Already have an Account?LogIn");
                }

            }
        });
    }

    private void handleLogIn()
    {
        //login through email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailID.getText().toString(),password.getText().toString())
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    //adding username, emailID and profile pic in the database(firebase)
                    FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(username.getText().toString(),emailID.getText().toString(),""));

                    startActivity(new Intent(MainActivity.this,FriendsActivity.class));
                    Toast.makeText(MainActivity.this, " SignIn Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleSignIn()
    {
        //sign in code through email and password
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailID.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(MainActivity.this,FriendsActivity.class));
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}