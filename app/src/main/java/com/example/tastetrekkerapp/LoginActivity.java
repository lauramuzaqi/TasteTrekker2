package com.example.tastetrekkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private TextView tvNoAccount;
    private ImageButton btnBack;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvNoAccount = findViewById(R.id.tvNoAccount);

        btnBack = findViewById(R.id.btnBack);
        btnLogin = findViewById(R.id.btnLogin);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedEmail = preferences.getString("userEmail", "");
        etEmail.setText(savedEmail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);

        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private String email = "", password = "";
    private void validateData() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
        }
        else{
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Logging in ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Save email to SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userEmail", email);
                        editor.apply(); // commit changes

                        // Redirect to the dashboard
                        startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Your email or password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void checkUser() {
        progressDialog.setMessage("Checking user...");
        //check role
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //check in db
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        //get userType
                        String userType = "" + snapshot.child("userType").getValue();
                        //check user type
                        if (userType.equals("user")){
                            //open user dashboard
                            startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                            finish();
                        } else if (userType.equals("admin")) {
                            //open admin dashboard
                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }
}