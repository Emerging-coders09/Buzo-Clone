package com.devarshi.buzoclone;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.FilteredVideosAdapter;
import com.devarshi.Adapter.SearchAdapter;
import com.devarshi.Retrofitclient.Category;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.ExampleTitleList;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;
import com.devarshi.Retrofitclient.Template;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.FinishSearchOnClick {

    ImageView backToHomeImageView;
    SearchAdapter searchAdapter;
    FilteredVideosAdapter filteredVideosAdapter;

    RecyclerView catsRecyclerView, filteredVideosRv;
    SearchView searchView;
//    EditText searchEt;

    ConstraintLayout catsConstraintLayout, videoTitleListCL;

    ArrayList<Category> listOfCatItems = new ArrayList<>();
    ArrayList<Template> listOfVidsItems = new ArrayList<>();
    public ArrayList<String> listOfFilteredVideos = new ArrayList<>();

    boolean searchApiCalling = false;

//    int currentItems, scrolledOutItems, totalItems;

//    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backToHomeImageView = findViewById(R.id.iVBackToHome);

        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.slide_in_bottom);
            }
        });

        catsRecyclerView = findViewById(R.id.rVCategories);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        catsRecyclerView.setLayoutManager(linearLayoutManager);

        searchAdapter = new SearchAdapter(listOfCatItems, this, this);
        catsRecyclerView.setAdapter(searchAdapter);

        CallRetrofitForSa();

//        searchEt = findViewById(R.id.eTSearch);
        searchView = findViewById(R.id.searchViewRt);
        catsConstraintLayout = findViewById(R.id.cLCats);
        videoTitleListCL = findViewById(R.id.cLVideoTitleList);

        filteredVideosRv = findViewById(R.id.rVFilteredVideos);

        LinearLayoutManager lLmForFilteredVideos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        filteredVideosRv.setLayoutManager(lLmForFilteredVideos);

        filteredVideosAdapter = new FilteredVideosAdapter(listOfFilteredVideos,listOfVidsItems,this,SearchActivity.this);
        filteredVideosRv.setAdapter(filteredVideosAdapter);

//        searchEt.requestFocus();

        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                if (newText.length() == 0) {
                    catsConstraintLayout.setVisibility(View.VISIBLE);
                    videoTitleListCL.setVisibility(View.GONE);
                } else {

                    catsConstraintLayout.setVisibility(View.GONE);
                    videoTitleListCL.setVisibility(View.VISIBLE);

                    filter(newText);

                }

//                listOfFilteredVideos.clear();

                return false;
            }
        });

        /*searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()) {
                    catsConstraintLayout.setVisibility(View.VISIBLE);
                    videoTitleListCL.setVisibility(View.GONE);
                } else {
                    catsConstraintLayout.setVisibility(View.GONE);
                    videoTitleListCL.setVisibility(View.VISIBLE);

                    CallRetrofitForSr(s.toString());


//                    filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                filter(s.toString());


            }

        });*/
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

    private void CallRetrofitForSa() {

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForHsVideos("newest", "", "buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Log.d(TAG, "onResponse: " + response.body().getData().getCategories());

                listOfCatItems.addAll(response.body().getData().getCategories());

                listOfVidsItems.addAll(response.body().getData().getTemplates());

                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: ", t);

            }
        });

    }

    private void CallRetrofitForSr() {

        searchApiCalling = true;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.slide_in_bottom);
    }

    @Override
    public void finishSearchActivity() {
        finish();
    }
}