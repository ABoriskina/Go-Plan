package com.rgr.goplan.musorka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rgr.goplan.R;
import com.rgr.goplan.entity.Users;

public class register {
   /* private void showRegisterWindow() {
        Button toLogInButton, toRegisterButton;

        FirebaseAuth auth;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference users;
    }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.register_layout, null);
        dialog.setView(register_layout);

        MaterialEditText email = register_layout.findViewById(R.id.email);
        MaterialEditText pass = register_layout.findViewById(R.id.password);
        MaterialEditText name = register_layout.findViewById(R.id.name);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(email.getText().toString()) &&
                        !TextUtils.isEmpty(pass.getText().toString()) &&
                        !TextUtils.isEmpty(name.getText().toString())){
                    auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Users user = new Users(email.getText().toString(),
                                            pass.getText().toString(),
                                            name.getText().toString());
                                    users.child(user.getEmail())
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                }
                            });
                }
            }
        });
}*/
}
