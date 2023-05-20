package com.rgr.goplan;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgr.goplan.entity.Events;
import com.rgr.goplan.entity.Users;

public class activity_to_add_event extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference events;

    public void goBack(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add_event);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        events = firebaseDatabase.getReference("Events");

        EditText eventName = findViewById(R.id.event);
        EditText eventDescription = findViewById(R.id.description);

        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(eventName.getText().toString())){
                    return;
                }
                if (TextUtils.isEmpty(eventDescription.getText().toString())){
                    return;
                }

                Events event = new Events(
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        eventName.getText().toString(),
                        eventDescription.getText().toString(),
                        timePicker.getHour() + ":" + timePicker.getMinute(),
                        datePicker.getDayOfMonth() + "." + (datePicker.getMonth() + 1)  + "." + datePicker.getYear()
                );

                events.child(firebaseDatabase.getReference("Events").push().getKey())
                        .setValue(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                goBack(v);
                                finish();
                            }
                        });

            }
        });
    }
}