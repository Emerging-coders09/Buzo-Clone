package com.devarshi.buzoclone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.FilteredVideosAdapter;
import com.devarshi.Adapter.SearchVideoResultAdapter;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.ExampleTitleList;
import com.devarshi.Retrofitclient.FilteredData;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;
import com.devarshi.Retrofitclient.SearchAPIData;
import com.devarshi.Retrofitclient.Template;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVideoTitleResultActivity extends AppCompatActivity {

    ArrayList<FilteredData> listForVideos = new ArrayList<>();

    ArrayList<Template> listOfVidsItems = new ArrayList<>();
    public ArrayList<String> listOfFilteredVideos = new ArrayList<>();

    String videoTitle = "";
    SearchView selectedFilVidsSv;
    ImageView selectedFilVidsBkIv;

    RecyclerView selectedFilVidsRv, filteredVidsRv;
    SearchVideoResultAdapter searchVideoResultAdapter;

    FilteredVideosAdapter filteredVideosAdapter;

    FloatingActionButton filVideosFab;

    ProgressBar filVideosPb;

    private static final String TAG = "SearchVideoTitleResult";

    int scrolledOutItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video_title_result);

        selectedFilVidsSv = findViewById(R.id.sVSelectedFilteredVids);
        selectedFilVidsBkIv = findViewById(R.id.iVFilVidsBack);

        selectedFilVidsRv = findViewById(R.id.rVSelectedFilVids);
        filteredVidsRv = findViewById(R.id.recyclerVFilteredVideos);

        filVideosFab = findViewById(R.id.fAbFilVideos);
        filVideosPb = findViewById(R.id.pBFilVids);

        CallRetrofitForSa();

        Intent intent = getIntent();

        videoTitle = intent.getStringExtra("title");

        Log.d(TAG, "onCreate: videoTitle: " + videoTitle);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        selectedFilVidsRv.setLayoutManager(gridLayoutManager);

        searchVideoResultAdapter = new SearchVideoResultAdapter(this, listForVideos,videoTitle);
        selectedFilVidsRv.setAdapter(searchVideoResultAdapter);

        CallRetrofitForSearchRes();

        selectedFilVidsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (scrolledOutItems > 5){

                    filVideosFab.setVisibility(View.VISIBLE);
                    filVideosFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedFilVidsRv.smoothScrollToPosition(0);
                        }
                    });
                }
                else if (scrolledOutItems < 5){

                    filVideosFab.setVisibility(View.GONE);
                }
            }
        });

        selectedFilVidsSv.setQuery(videoTitle,false);
        selectedFilVidsSv.requestFocus();

        LinearLayoutManager lLmForFilteredVideos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        filteredVidsRv.setLayoutManager(lLmForFilteredVideos);

        filteredVideosAdapter = new FilteredVideosAdapter(listOfFilteredVideos,listOfVidsItems,this,SearchVideoTitleResultActivity.this);
        filteredVidsRv.setAdapter(filteredVideosAdapter);

        selectedFilVidsSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() > 0) {

                    CallRetrofitForSr();
                    resetList();

                    Log.d(TAG, "onQueryTextSubmit: called");

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0){
                    selectedFilVidsRv.setVisibility(View.GONE);
                    filteredVidsRv.setVisibility(View.VISIBLE);
                }
                else {
                    selectedFilVidsRv.setVisibility(View.VISIBLE);
                    filteredVidsRv.setVisibility(View.GONE);
                }

                filter(newText);


                return false;
            }
        });

        selectedFilVidsBkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void filter(String s) {

        ArrayList<String> temp = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("listOfFilteredVideos", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("arrayList", null);

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        listOfFilteredVideos = gson.fromJson(json, type);

        /*if (listOfFilteredVideos == null){
            listOfFilteredVideos = new ArrayList<>();
        }*/
        for (String fd : listOfFilteredVideos) {
            if (fd.toLowerCase(Locale.getDefault()).contains(s.toLowerCase(Locale.getDefault()))) {
                temp.add(fd);
            }
        }
        filteredVideosAdapter.updateList(temp);
    }

    public void resetList() {

        listOfFilteredVideos.clear();
        filteredVideosAdapter.notifyDataSetChanged();
    }

    private void CallRetrofitForSearchRes(){

        filVideosPb.setVisibility(View.VISIBLE);

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<SearchAPIData> call = retrofitRequestApi.PostDataIntoServerForFilVids("buzo",videoTitle,"");

        call.enqueue(new Callback<SearchAPIData>() {
            @Override
            public void onResponse(Call<SearchAPIData> call, Response<SearchAPIData> response) {

                for (int i = 0; i < response.body().getData().size(); i++) {

//                    Log.d(TAG, "onResponse: title " + response.body().getData().get(i).getTitle());

                    listForVideos.add(response.body().getData().get(i));

                    searchVideoResultAdapter.notifyDataSetChanged();

                }

                filVideosPb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SearchAPIData> call, Throwable t) {

                Log.d(TAG, "onFailure: ",t);
            }

        });
    }

    private void CallRetrofitForSr() {

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofitForTitles().create(RetrofitRequestApi.class);

        Call<ExampleTitleList> call = retrofitRequestApi.PostDataIntoServerForSearchRes();

        call.enqueue(new Callback<ExampleTitleList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ExampleTitleList> call, Response<ExampleTitleList> response) {

                /*page++;

                searchApiCalling = false;

                assert response.body() != null;
                listOfFilteredVideos.addAll(response.body().getData());*/

//                Log.d(TAG, "onResponse: called " + response);

                listOfFilteredVideos.addAll(response.body().getTemplates());
                filteredVideosAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getSharedPreferences("listOfFilteredVideos", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String json = gson.toJson(listOfFilteredVideos);
                editor.putString("arrayList", json);
                editor.apply();

            }

            @Override
            public void onFailure(Call<ExampleTitleList> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });

    }

    private void CallRetrofitForSa() {

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForHsVideos("newest", "", "buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Log.d(TAG, "onResponse: " + response.body().getData().getCategories());

                listOfVidsItems.addAll(response.body().getData().getTemplates());
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: ", t);

            }
        });

    }
}