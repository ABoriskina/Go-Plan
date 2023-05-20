package com.rgr.goplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rgr.goplan.entity.Users;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    Button toLogInButton, toRegisterButton;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;


    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToHomePage(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    public void goToRegisterPage(View v) {
        Intent intent = new Intent(this, toRegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.INVISIBLE);
        ImageView errorSign = findViewById(R.id.errorSign);
        errorSign.setVisibility(View.INVISIBLE);

        toLogInButton = findViewById(R.id.toLogInButton);
        toRegisterButton = findViewById(R.id.toRegisterButton);

        EditText email = findViewById(R.id.userEmail);
        EditText pass = findViewById(R.id.userPass);

        SharedPreferences sharedPref = this.getSharedPreferences("PERSISTANT_STORAGE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("Users");



        if (sharedPref.getBoolean("IS_LOGGED", false)) {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }

        toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterPage(v);
                finish();
            }
        });

        toLogInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString()) &&
                        !TextUtils.isEmpty(pass.getText().toString())) {

                    auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    email.setText("");
                                    Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    errorMessage.setVisibility(View.VISIBLE);
                                    errorSign.setVisibility(View.VISIBLE);
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    editor.putBoolean("IS_LOGGED", true).commit();

                                    goToHomePage(v);
                                    finish();
                                }
                            });
                            }
                        }
                    });
                }
            }


