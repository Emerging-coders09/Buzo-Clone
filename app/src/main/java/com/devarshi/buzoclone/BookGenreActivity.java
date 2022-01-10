package com.devarshi.buzoclone;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.BookGenreAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookGenreActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    BookGenreAdapter bookGenreAdapter;
    RecyclerView bookGenresRv;

    ArrayList<String> listOfBookGenre = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_genre);

        databaseReference = FirebaseDatabase.getInstance().getReference("Book Genres");

        Log.d(TAG, "onCreate: " + databaseReference);

        bookGenresRv = findViewById(R.id.rvBookGenres);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        bookGenresRv.setLayoutManager(linearLayoutManager);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d(TAG, "onDataChange: " + snapshot.getChildrenCount());

                /*for (DataSnapshot s : snapshot.getChildren()){
                    String value = s.getValue(BookGenre.class).getGenreName();
                    listOfBookGenre.add(value);
                }*/
                for (DataSnapshot s : snapshot.getChildren()){
                    String value = String.valueOf(s.getValue());
                    listOfBookGenre.add(value);
                }

                /*JSONObject jsonObject = new JSONObject((Map) snapshot);

                try {
                    String genreName = jsonObject.getString("Genre Name");

                    Log.d(TAG, "onDataChange: genreName " + genreName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                Log.d(TAG, "onDataChange: snapdetail " + listOfBookGenre);
                bookGenreAdapter = new BookGenreAdapter(listOfBookGenre);
                bookGenresRv.setAdapter(bookGenreAdapter);
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