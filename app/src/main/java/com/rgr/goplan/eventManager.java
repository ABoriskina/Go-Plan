package com.rgr.goplan;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.rgr.goplan.entity.Events;
import com.rgr.goplan.entity.Notifications;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class eventManager extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    Map<Date, ArrayList<String>> map = new TreeMap<>();
    @Override
    public void onItemClick(View v, int position) {
        editEvent(v, position);
    }

    public void editEvent(View v, int position) {
        PopupMenu popup = new PopupMenu(eventManager.this, v);
        popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("Удалить")){
                    String dateAndTime = adapter.getItem(position);
                    dateAndTime = Arrays.asList(dateAndTime.split("\n")).get(0);

                    SimpleDateFormat format = new SimpleDateFormat();
                    format.applyPattern("dd.M.yyyy H:m");
                    Date dateForDelete = new Date();

                    try{
                        dateForDelete = format.parse(dateAndTime);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    dbRef.child("Events").child(map.get(dateForDelete).get(1)).removeValue();
                    map.remove(dateForDelete);

                    ArrayList<String> events = new ArrayList<>();
                    map.forEach((key, value) -> events.add(format.format(key) + "\n" + value.get(0)));

                    adapter = new MyRecyclerViewAdapter(eventManager.this, events);
                    adapter.setClickListener(eventManager.this);
                    recyclerView.setAdapter(adapter);

                }
                if (item.getTitle().equals("Поделиться")){


                    final EditText input = new EditText(eventManager.this);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(eventManager.this,
                            R.style.MaterialAlertDialog_Rounded);


                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setTitle("Поделиться");

                    builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Query query = dbRef.child("Users")
                                                .orderByChild("email")
                                                .equalTo(input.getText().toString());

                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    if (!snapshot.exists()){
                                        Toast toast = Toast.makeText(getBaseContext(), "Пользователь не найден.", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP, 0, 160);
                                        toast.show();
                                    }

                                    for (DataSnapshot issue : snapshot.getChildren()) {

                                        List<String> data = Arrays.asList(adapter.getItem(position).split("\n"));
                                        String date = Arrays.asList(data.get(0).split(" ")).get(0);
                                        String time = Arrays.asList(data.get(0).split(" ")).get(1);
                                        String event = Arrays.asList(data.get(1)).get(0);
                                        String description = Arrays.asList(data.get(2)).get(0);

                                        Notifications notification = new Notifications(
                                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                                issue.getKey(),
                                                "Приглашаю Вас на событие!",
                                                new Events(event, description, time, date), "false");

                                        DatabaseReference notificationRef = firebaseDatabase.getReference("Notifications");
                                        notificationRef.child(firebaseDatabase.getReference("Notifications").push().getKey())
                                                .setValue(notification);

                                        Toast toast = Toast.makeText(eventManager.this, "Успешно отправлено!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP, 0, 160);
                                        toast.show();
                                        return;

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                    });

                    builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                return true;
            }
        });
        popup.show();
    }

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

    public void goHome(MenuItem item) {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_manager);

        recyclerView = (RecyclerView) findViewById(R.id.eventsList1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> events = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();

        Query query = dbRef.child("Events").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    map.clear();
                    events.clear();

                    SimpleDateFormat format = new SimpleDateFormat();
                    format.applyPattern("dd.M.yyyy H:m");

                    for (DataSnapshot issue : snapshot.getChildren()) {

                        ArrayList<String> eventName = new ArrayList<String>();

                        Date date = new Date();
                        try {
                            String dateAndTime = issue.child("date").getValue().toString() + " " + issue.child("time").getValue().toString();
                            date = format.parse(dateAndTime);

                            eventName.add(issue.child("eventName").getValue() + "\n" + issue.child("description").getValue());
                            eventName.add(issue.getKey());

                            map.put(date, eventName);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        if (map.isEmpty())
                            events.add("Событий не найдено.");

                        else map.forEach((key, value) -> events.add(format.format(key) + "\n" + value.get(0)));
                    } catch (Exception e) {
                        events.add("Событий не найдено.");
                    }

                    adapter = new MyRecyclerViewAdapter(eventManager.this, events);
                    adapter.setClickListener(eventManager.this);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        FloatingActionButton addEvent = findViewById(R.id.addEvent1);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddActivity(v);
            }
        });

    }
    public void showNotificationsFromEvent(MenuItem item) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
        finish();

    }
}