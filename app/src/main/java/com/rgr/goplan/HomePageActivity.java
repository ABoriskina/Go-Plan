package com.rgr.goplan;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;

import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.rgr.goplan.entity.Events;
import com.rgr.goplan.entity.Notifications;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;

public class HomePageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    MyRecyclerViewAdapter adapter;
    int unseenNotifications = 0;
    Map<Date, String> map = new TreeMap<>();
    ArrayList<String> events = new ArrayList<>();

    public void goToAddActivity(View v) {
        Intent intent = new Intent(this, activity_to_add_event.class);
        startActivity(intent);
        finish();
    }
    public void logOut(View v){
        SharedPreferences sharedPref = this.getSharedPreferences("PERSISTANT_STORAGE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGGED", false);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView recyclerView = findViewById(R.id.eventsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView greeting = findViewById(R.id.greeting);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();

        dbRef.child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){

                            int timeFormat = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

                            if (timeFormat > 7 && timeFormat < 17)
                                greeting.setText("Добрый день, " + task.getResult().getValue().toString());
                            else if (timeFormat > 17 && timeFormat < 22)
                                greeting.setText("Добрый вечер, " + task.getResult().getValue().toString());
                            else
                                greeting.setText("Доброй ночи, " + task.getResult().getValue().toString());
                        }
                    }
                });


        Query query = dbRef.child("Notifications").orderByChild("toWho").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int countNotifications = 0;

                    List<DataSnapshot> dataSnapshotList = new ArrayList<DataSnapshot>();
                    snapshot.getChildren().forEach(dataSnapshotList::add);
                    int size = dataSnapshotList.size();

                    for (int i = size - 1; i >= 0; i--) {
                        DataSnapshot issue = dataSnapshotList.get(i);

                        countNotifications++;

                        if (issue.child("seen").getValue().toString().equals("false")) {
                            unseenNotifications++;
                            dbRef.child("Notifications").child(issue.getKey()).child("seen").setValue("true");
                        }

                        if (countNotifications > 1000) {
                            dbRef.child("Notifications").child(issue.getKey()).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        if (unseenNotifications > 0) {
            Toast toast = Toast.makeText(this, "У вас " + unseenNotifications + " новых уведомлений.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 160);
            toast.show();
        }
        unseenNotifications = 0;

        Query query1 = dbRef.child("Events").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    map.clear();
                    events.clear();
                    SimpleDateFormat format = new SimpleDateFormat();
                    format.applyPattern("dd.M.yyyy H:m");

                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Date date = new Date();

                        try {
                            String dateAndTime = issue.child("date").getValue() + " " + issue.child("time").getValue();
                            date = format.parse(dateAndTime);
                            Date currentDate = format.parse(format.format(new Date()));

                            if (date.compareTo(currentDate) < 0){

                                Notifications notification = new Notifications("Go Plan",
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        "Истекло.",
                                        new Events(
                                                issue.child("eventName").getValue().toString(),
                                                issue.child("description").getValue().toString(),
                                                issue.child("time").getValue().toString(),
                                                issue.child("date").getValue().toString()),
                                        "false"
                                );

                                DatabaseReference notificationRef = firebaseDatabase.getReference("Notifications");
                                notificationRef.child(firebaseDatabase.getReference("Notifications").push().getKey())
                                        .setValue(notification);

                                dbRef.child("Events").child(issue.getKey()).removeValue();
                                continue;
                            }
                        }

                        catch (Exception e){
                            e.printStackTrace();
                        }

                        map.put(date, (issue.child("eventName").getValue() + "\n" + issue.child("description").getValue()));
                    }

                    map.forEach((key, value) ->  events.add(format.format(key) + "\n" + value));

                    adapter = new MyRecyclerViewAdapter(HomePageActivity.this, events);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        FloatingActionButton addEvent = findViewById(R.id.addEvent);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddActivity(v);
            }
        });

    }

    public void showEvents(MenuItem item) {
        Intent intent = new Intent(this, eventManager.class);
        startActivity(intent);
        finish();

    }
    public void showNotifications(MenuItem item) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
        finish();

    }
}