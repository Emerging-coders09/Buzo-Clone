package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
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

    boolean isScrolling = false;
    boolean apiCalling = false;

    int varId = 0;
    int currentItems,totalItems,scrolledOutItems;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    /*private int visibleThreshold = 1; // trigger just one item before the end
    private int lastVisibleItem, totalItemCount;*/

    String vdoLoadedId = "";
//    boolean isScrolling = false;

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

                Log.d(TAG, "onRefresh: called");

                /*String[] arrayVideos = {"Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali", "Diwali"};
                recyclerViewVideos.setAdapter(new videosAdapter(arrayVideos, MainActivity.this));*/
//                CallRetrofitForHomeScreenVideos();
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

        recyclerViewVideos.setNestedScrollingEnabled(false);

        /*CallRetrofitForHomeScreenVideos();

        recyclerViewVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItems = gridLayoutManager.getItemCount();

                Log.d(TAG, "onScrollChanged: Total Items " + totalItems);

                int diff = (recyclerViewVideos.getBottom() - (recyclerViewVideos.getHeight() + recyclerViewVideos.getScrollY()));

                if (diff == 0 && !apiCalling) {
                    Log.d(TAG, "onScrollChanged: API fetching");
                    apiCalling = false;
                    progressBar.setVisibility(View.GONE);
                    CallRetrofitForHomeScreenVideos();
                    apiCalling = true;
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        });*/

        nScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nScrollView.getChildAt(nScrollView.getChildCount() - 1);

                int totalItems = gridLayoutManager.getItemCount();

                Log.d(TAG, "onScrollChanged: Total Items " + totalItems);

                int diff = (view.getBottom() - (nScrollView.getHeight() + nScrollView.getScrollY()));

                if (diff == 0 && !apiCalling) {
                    Log.d(TAG, "onScrollChanged: API fetching");
                    apiCalling = false;
                    progressBar.setVisibility(View.GONE);
                    CallRetrofitForHomeScreenVideos();
                    apiCalling = true;
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });



        /*nScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    progressBar.setVisibility(View.VISIBLE);
                    CallRetrofitForHomeScreenVideos();
                }
            }
        });*/
        /*CallRetrofitForHomeScreenVideos();

        recyclerViewVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

//                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                *//*int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItem > gridLayoutManager.getItemCount() - 6 && !apiCalling && !isScrolling) {

                    apiCalling = true;
                    isScrolling = true;
                    progressBar.setVisibility(View.VISIBLE);
                    CallRetrofitForHomeScreenVideos();
                    progressBar.setVisibility(View.GONE);
                    apiCalling = false;
                    isScrolling = false;
                }*//*

                isScrolling = false;

                currentItems = gridLayoutManager.getChildCount();
                scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                totalItems = gridLayoutManager.getItemCount();

                Log.d(TAG, "onScrolled: currentItems" + currentItems);
                Log.d(TAG, "onScrolled: scrolledOutItems" + scrolledOutItems);
                Log.d(TAG, "onScrolled: totalItems" + totalItems);

                if (!isScrolling && (currentItems + scrolledOutItems == totalItems)){
                    Log.d(TAG, "onScrolled: API Called");
//                    progressBar.setVisibility(View.GONE);
                    isScrolling = true;
                    fetchData();
                }

                *//*int totalItem = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                Log.d(TAG, "onScrolled: Totalitem" + totalItem);
                Log.d(TAG, "onScrolled: LastVisibleItem" + lastVisibleItem);

                if (isScrolling && lastVisibleItem == totalItem -1){
                    progressBar.setVisibility(View.GONE);
                    isScrolling = true;
                    CallRetrofitForHomeScreenVideos();
                    progressBar.setVisibility(View.VISIBLE);
                    isScrolling = false;
                }*//*
            }
        });*/

        /*recyclerViewVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    loading = true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { //check for scroll down
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    Log.d(TAG, "onScrolled: visibleItemCount" + visibleItemCount);
                    Log.d(TAG, "onScrolled: totalItemCount" + totalItemCount);
                    Log.d(TAG, "onScrolled: pastVisiblesItems" + pastVisiblesItems);


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            progressBar.setVisibility(View.GONE);
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            CallRetrofitForHomeScreenVideos();

                            loading = true;
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }


                isScrolling = false;

                currentItems = gridLayoutManager.getChildCount();
                scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                totalItems = gridLayoutManager.getItemCount();

                Log.d(TAG, "onScrolled: currentItems" + currentItems);
                Log.d(TAG, "onScrolled: scrolledOutItems" + scrolledOutItems);
                Log.d(TAG, "onScrolled: totalItems" + totalItems);

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)){
                    Log.d(TAG, "onScrolled: API Called");
                    progressBar.setVisibility(View.VISIBLE);
                    CallRetrofitForHomeScreenVideos();
                    progressBar.setVisibility(View.GONE);
                    isScrolling = true;
                }
            }
        });*/

//        fetchData(page,limit)

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

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CallRetrofitForHomeScreenVideos();
                progressBar.setVisibility(View.GONE);
            }
        },2000);
    }

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

        Log.d("CallRetrofit", "CallRetrofitForHomeScreenVideos: called");

        apiCalling = true;

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServer("newest", vdoLoadedId, "buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                apiCalling = false;

                ArrayList<Integer> videoIds = new ArrayList<>();

                /*listOfVideoUrls.clear();
                listOfVideoThumbnails.clear();
                listOfTitles.clear();*/

                /*if (!isScrolling && response.isSuccessful()){
                    CallRetrofitForHomeScreenVideos();
                    isScrolling = false;
                }*/

                /*String[] arrayVideosTitles = new String[20];
                String[] arrayVideosThumbnails = new String[arrayVideosTitles.length];*/

                for (int i = 0; i < response.body().getData().getTemplates().size(); i++) {

                    if (i < response.body().getData().getTemplates().size()) {
                        videoIds.add(response.body().getData().getTemplates().get(i).getId());
                        vdoLoadedId += String.valueOf(response.body().getData().getTemplates().get(i).getId()).concat(",");
                    }
                    /*if (i == response.body().getData().getTemplates().size() - 1) {
                        vdoLoadedId += String.valueOf(response.body().getData().getTemplates().get(i).getId());
                    }*/
                }
                Log.d(TAG, "onResponse: VideoIds " + videoIds.size());


                for (int i = 0; i < response.body().getData().getTemplates().size(); i++) {
                    listOfTitles.add(response.body().getData().getTemplates().get(i).getTitle());
                    listOfVideoThumbnails.add(response.body().getData().getTemplates().get(i).getThumbUrl());
                    listOfVideoUrls.add(response.body().getData().getTemplates().get(i).getVideoUrl());

//                    arrayVideosTitles[i] = response.body().getData().getTemplates().get(i).getTitle();
//                    arrayVideosThumbnails[i] = response.body().getData().getTemplates().get(i).getThumbUrl();
                }
                VideosAdapter videosAdapter = new VideosAdapter(listOfTitles,listOfVideoThumbnails,listOfVideoUrls,MainActivity.this);
                recyclerViewVideos.setAdapter(videosAdapter);
                videoIds.clear();
                videosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
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
        } else if (this.doubleBackToExitPressedOnce) {
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