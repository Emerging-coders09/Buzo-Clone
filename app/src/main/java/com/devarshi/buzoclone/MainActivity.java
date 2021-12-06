package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devarshi.Adapter.categoriesAdapter;
import com.devarshi.Adapter.videosAdapter;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewCategories;
    RecyclerView recyclerViewVideos;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    CardView navigationDrawer;
    CardView cardViewWhatsapp;
    EditText editTextSearch;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        navigationDrawer = findViewById(R.id.navigationDraw);
        editTextSearch = findViewById(R.id.searchEditText);
        cardViewWhatsapp = findViewById(R.id.whatsappCardView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String[] arrayCategories = {"Diwali", "Birthday", "Glow", "Love", "Magical", "Monsoon", "Reels", "Sad", "Search", "Spactrum", "Whatsapp"};
                recyclerViewCategories.setAdapter(new categoriesAdapter(arrayCategories));

                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
                recyclerViewVideos.setLayoutManager(gridLayoutManager);

                String[] arrayVideos = {"Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali"};
                recyclerViewVideos.setAdapter(new videosAdapter(arrayVideos, MainActivity.this));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        String[] arrayCategories = {"Diwali", "Birthday", "Glow", "Love", "Magical", "Monsoon", "Reels", "Sad", "Search", "Spactrum", "Whatsapp"};
        recyclerViewCategories.setAdapter(new categoriesAdapter(arrayCategories));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewVideos.setLayoutManager(gridLayoutManager);

        String[] arrayVideos = {"Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali"};
        recyclerViewVideos.setAdapter(new videosAdapter(arrayVideos, MainActivity.this));

        /*Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);*/

        navigationView = (NavigationView) findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.videos:
                        Toast.makeText(getApplicationContext(), "Videos is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.favourites:
                        Toast.makeText(getApplicationContext(), "Favourites is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.sound:
                        Toast.makeText(getApplicationContext(), "Sound is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.share:
                        Toast.makeText(getApplicationContext(), "Share app is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.rate:
                        Toast.makeText(getApplicationContext(), "Rate app is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.privacy:
                        Toast.makeText(getApplicationContext(), "Privacy policy is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Nothing is opened", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        cardViewWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WhatsAppCardView.class);
                startActivity(intent);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            this.doubleBackToExitPressedOnce = false;
        }
        else if (this.doubleBackToExitPressedOnce){
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


        /*editTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.set
            }
        });*/

        /*editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                editTextSearch.setCursorVisible(false);
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editTextSearch.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });*/
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}