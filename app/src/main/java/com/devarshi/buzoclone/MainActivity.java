package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devarshi.Adapter.VideosAdapter;
import com.devarshi.Adapter.categoriesAdapter;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewCategories;
    RecyclerView recyclerViewVideos;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    CardView navigationDrawer;
    CardView cardViewWhatsapp;
    EditText editTextSearch;

    ProgressBar progressBar;

    NestedScrollView nScrollView;

    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<String> listOfTitles = new ArrayList<>();
    ArrayList<String> listOfVideoThumbnails = new ArrayList<>();
    ArrayList<String> listOfVideoUrls = new ArrayList<>();

    int page = 1, limit = 20;

    Boolean isScrolling = false;
    int currentItems,totalItems,scrolledOutItems;
    /*private int visibleThreshold = 1; // trigger just one item before the end
    private int lastVisibleItem, totalItemCount;*/

    String vdoLoadedId = "";

//    VideosAdapter videosAdapter;

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

        progressBar = findViewById(R.id.progress);

        nScrollView = findViewById(R.id.nestedScrollView);

//        videosAdapter = new VideosAdapter(listOfTitles,listOfVideoThumbnails,listOfVideoUrls,MainActivity.this);

        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String[] arrayCategories = {"Diwali", "Birthday", "Glow", "Love", "Magical", "Monsoon", "Reels", "Sad", "Search", "Spactrum", "Whatsapp"};
                recyclerViewCategories.setAdapter(new categoriesAdapter(arrayCategories));

                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
                recyclerViewVideos.setLayoutManager(gridLayoutManager);

                /*String[] arrayVideos = {"Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali"};
                recyclerViewVideos.setAdapter(new videosAdapter(arrayVideos, MainActivity.this));*/
                CallRetrofitForHomeScreenVideos();
                /*recyclerViewVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                            isScrolling = true;
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        currentItems = gridLayoutManager.getChildCount();
                        totalItems = gridLayoutManager.getItemCount();
                        scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                        if (isScrolling && (currentItems + scrolledOutItems == totalItems)){
                            isScrolling = false;
                            fetchData();
                        }
                    }
                });*/
                swipeRefreshLayout.setRefreshing(false);

                /*recyclerViewVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                            isScrolling = true;
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        currentItems = gridLayoutManager.getChildCount();
                        totalItems = gridLayoutManager.getItemCount();
                        scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                        if (isScrolling && (currentItems + scrolledOutItems == totalItems)){
                            isScrolling = false;
                            fetchData();
                        }
                    }
                });*/
            }
        });

        String[] arrayCategories = {"Diwali", "Birthday", "Glow", "Love", "Magical", "Monsoon", "Reels", "Sad", "Search", "Spactrum", "Whatsapp"};
        recyclerViewCategories.setAdapter(new categoriesAdapter(arrayCategories));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewVideos.setLayoutManager(gridLayoutManager);

        CallRetrofitForHomeScreenVideos();

//        fetchData(page,limit);

        nScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredWidth()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    CallRetrofitForHomeScreenVideos();
                }
            }
        });

        /*recyclerViewVideos.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            }
        });*/
        /*recyclerViewVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d(TAG, "onScrolled: called");

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                Log.d(TAG, "onScrolled: Current item" + currentItems);
                Log.d(TAG, "onScrolled: Scroll out item" + scrolledOutItems);
                Log.d(TAG, "onScrolled: Total item" + totalItems);

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)){
                    progressBar.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    CallRetrofitForHomeScreenVideos();
                    totalItems += 20;
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/


//        String[] arrayVideos = {"Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali"};
//        recyclerViewVideos.setAdapter(new videosAdapter(arrayVideos, MainActivity.this));

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

    /*private void fetchData(int page,int limit){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=page; i<limit; i++){
                    CallRetrofitForHomeScreenVideos();
                }
            }
        },5000);
    }*/

    /*private void fetchData(){
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<20;i++) {
                    CallRetrofitForHomeScreenVideos();
//                    videosAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }
        },5000);
    }*/

    private void CallRetrofitForHomeScreenVideos() {

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServer("newest",vdoLoadedId, "buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                vdoLoadedId = "";
                listOfVideoUrls.clear();
                listOfVideoThumbnails.clear();
                listOfTitles.clear();

                /*String[] arrayVideosTitles = new String[20];
                String[] arrayVideosThumbnails = new String[arrayVideosTitles.length];*/

                for (int i = 0; i < response.body().getData().getTemplates().size(); i++) {

                    if (i < response.body().getData().getTemplates().size()-2){
                        vdoLoadedId += String.valueOf(response.body().getData().getTemplates().get(i).getId()).concat(",");
                    }
                    if (i == response.body().getData().getTemplates().size()-1){
                        vdoLoadedId += String.valueOf(response.body().getData().getTemplates().get(i).getId());
                    }
                }

                for (int i = 0; i < response.body().getData().getTemplates().size(); i++) {
                    listOfTitles.add(response.body().getData().getTemplates().get(i).getTitle());
                    listOfVideoThumbnails.add(response.body().getData().getTemplates().get(i).getThumbUrl());
                    listOfVideoUrls.add(response.body().getData().getTemplates().get(i).getVideoUrl());
//                    arrayVideosTitles[i] = response.body().getData().getTemplates().get(i).getTitle();
                    Log.d(TAG, "onResponse: " + response.body().getData().getTemplates().get(i).getThumbUrl());
//                    arrayVideosThumbnails[i] = response.body().getData().getTemplates().get(i).getThumbUrl();
                }
                recyclerViewVideos.setAdapter(new VideosAdapter(listOfTitles,listOfVideoThumbnails,listOfVideoUrls,MainActivity.this));
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onFailure: ",t);
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

}