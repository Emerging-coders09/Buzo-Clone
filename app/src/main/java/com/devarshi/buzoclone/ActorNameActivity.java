package com.devarshi.buzoclone;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.ActorNameAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActorNameActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    ActorNameAdapter actorNameAdapter;
    RecyclerView actorNameRv;

    ArrayList<String> listOfActorName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_name);

        databaseReference = FirebaseDatabase.getInstance().getReference("Actors");

        Log.d(TAG, "onCreate: " + databaseReference);

        actorNameRv = findViewById(R.id.rVActorName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        actorNameRv.setLayoutManager(linearLayoutManager);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d(TAG, "onDataChange: " + snapshot.getChildrenCount());

                /*for (DataSnapshot s : snapshot.getChildren()){
                    String value = s.getValue(BookGenre.class).getGenreName();
                    listOfActorName.add(value);
                }*/
                for (DataSnapshot s : snapshot.getChildren()){
                    String value = String.valueOf(s.getValue());
                    listOfActorName.add(value);
                }

                /*JSONObject jsonObject = new JSONObject((Map) snapshot);

                try {
                    String genreName = jsonObject.getString("Genre Name");

                    Log.d(TAG, "onDataChange: genreName " + genreName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                Log.d(TAG, "onDataChange: snapdetail " + listOfActorName);
                actorNameAdapter = new ActorNameAdapter(listOfActorName);
                actorNameRv.setAdapter(actorNameAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*FirebaseRecyclerOptions<BookGenre> options
                = new FirebaseRecyclerOptions.Builder<BookGenre>()
                .setQuery(databaseReference, BookGenre.class)
                .build();*/



        /*FirebaseRecyclerOptions<person> options
                = new FirebaseRecyclerOptions.Builder<person>()
                .setQuery(mbase, person.class)
                .build();*/
    }

    /*@Override protected void onStart()
    {
        super.onStart();
        bookGenreAdapter.startListening();
    }

    @Override protected void onStop()
    {
        super.onStop();
        bookGenreAdapter.stopListening();
    }*/

    /*private void getData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}