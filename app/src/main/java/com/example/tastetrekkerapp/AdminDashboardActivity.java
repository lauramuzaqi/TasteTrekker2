package com.example.tastetrekkerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboardActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImageButton btnLogOut;
    private TextView tvHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnLogOut = findViewById(R.id.btnLogOut);
        tvHello = findViewById(R.id.tvHello);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        checkUser();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            // Set a loading message or keep it empty while fetching the user's name
            tvHello.setText("Loading...");
            String uid = firebaseUser.getUid(); // Get the current user's UID

            // Reference to the user's node in Realtime Database
            databaseReference.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue(String.class);
                    if (name != null && !name.isEmpty()) {
                        tvHello.setText("Hello " + name);
                    } else {
                        tvHello.setText("Hello User");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    tvHello.setText("Hello User");
                }
            });
        }
    }

    /*private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{
            String email = firebaseUser.getEmail();
            tvHello.setText("Hello" + email);
        }
    }*/
}