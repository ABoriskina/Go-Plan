package com.rgr.goplan;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.rgr.goplan.entity.Events;
import com.rgr.goplan.entity.Notifications;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;


public class NotificationsActivity extends AppCompatActivity{

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> notifications = new ArrayList<>();
    ArrayList<String> notificationPos = new ArrayList<>();

    public void goToAddActivity(View v) {
        Intent intent = new Intent(this, activity_to_add_event.class);
        startActivity(intent);
        finish();
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.eventsList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();

        Query query = dbRef.child("Notifications").orderByChild("toWho").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Map<String, Events> map = new TreeMap<>();

                    SimpleDateFormat format = new SimpleDateFormat();
                    format.applyPattern("dd.M.yyyy H:m");

                    List<DataSnapshot> dataSnapshotList = new ArrayList<DataSnapshot>();
                    snapshot.getChildren().forEach(dataSnapshotList::add);

                    for (int i = dataSnapshotList.size() - 1; i >= 0; i--) {
                        DataSnapshot issue = dataSnapshotList.get(i);

                        Date date = new Date();

                        try {
                            String dateAndTime = issue.child("event").child("date").getValue() + " " + issue.child("event").child("time").getValue();
                            date = format.parse(dateAndTime);
                        }

                        catch (Exception e){
                            e.printStackTrace();
                        }
                        notifications.add(
                                "От: " + issue.child("fromWho").getValue().toString() + "\n" +
                                        "Сообщение: " + issue.child("message").getValue().toString() + "\n" +
                                        "Связанное событие: " + "\n" + issue.child("event").child("date").getValue()
                                        + " " + issue.child("event").child("time").getValue() + "\n" +
                                        issue.child("event").child("eventName").getValue().toString() + "\n" +
                                        issue.child("event").child("description").getValue()

                        );
                        notificationPos.add(issue.getKey());
                    }
                    adapter = new MyRecyclerViewAdapter(NotificationsActivity.this, notifications);
                    recyclerView.setAdapter(adapter);
                    adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            PopupMenu popup = new PopupMenu(NotificationsActivity.this, view);
                            popup.getMenuInflater().inflate(R.menu.add_to_events_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (item.getTitle().equals("Добавить событие к себе")){
                                        List<String> data = Arrays.asList(adapter.getItem(position).split("\n"));
                                        String date = Arrays.asList(data.get(3).split(" ")).get(0);
                                        String time = Arrays.asList(data.get(3).split(" ")).get(1);
                                        String event = Arrays.asList(data.get(4)).get(0);
                                        String description = Arrays.asList(data.get(5)).get(0);

                                        dbRef.child("Events").child(firebaseDatabase.getReference("Events").push().getKey())
                                                .setValue(new Events(FirebaseAuth.getInstance().getUid(),
                                                        event,
                                                        description,
                                                        time,
                                                        date));

                                    }

                                    return true;
                                }
                            });
                            popup.show();
                        }
                    });

                }

                else notifications.add("Вы не получали уведомлений.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        FloatingActionButton addEvent = findViewById(R.id.addEvent2);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddActivity(v);
            }
        });

    }
    public void showEventsFromNotif(MenuItem item) {
        Intent intent = new Intent(this, eventManager.class);
        startActivity(intent);
        finish();

    }
    public void goHomeFromNotif(MenuItem item) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();

    }

}