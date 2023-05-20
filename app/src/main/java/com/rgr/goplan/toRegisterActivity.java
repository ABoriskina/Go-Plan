package com.rgr.goplan;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgr.goplan.entity.Users;
import org.jetbrains.annotations.NotNull;

public class toRegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_to_register);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("Users");

        EditText signUpPhone = findViewById(R.id.signUpPhone);
        EditText signUpEmail = findViewById(R.id.signUpEmail);
        EditText signUpPassword = findViewById(R.id.signUpPassword);
        EditText signUpName = findViewById(R.id.signUpName);

        Button regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(signUpPhone.getText().toString())){
                    return;
                }
                if(TextUtils.isEmpty(signUpEmail.getText().toString())){
                    return;
                }
                if(TextUtils.isEmpty(signUpPassword.getText().toString())){
                    return;
                }
                if(TextUtils.isEmpty(signUpName.getText().toString())){
                    return;
                }

                auth.createUserWithEmailAndPassword(signUpEmail.getText().toString(), signUpPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Users user = new Users(
                                        signUpPhone.getText().toString(),
                                        signUpEmail.getText().toString(),
                                        signUpPassword.getText().toString(),
                                        signUpName.getText().toString()
                                );
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                goBack(v);
                                                finish();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }
}