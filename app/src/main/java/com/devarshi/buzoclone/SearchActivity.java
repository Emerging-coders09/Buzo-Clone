package com.devarshi.buzoclone;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.FilteredVideosAdapter;
import com.devarshi.Adapter.SearchAdapter;
import com.devarshi.Retrofitclient.Category;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.ExampleFilteredData;
import com.devarshi.Retrofitclient.FilteredData;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.FinishSearchOnClick {

    ImageView backToHomeImageView;
    SearchAdapter searchAdapter;
    FilteredVideosAdapter filteredVideosAdapter;

    RecyclerView catsRecyclerView, filteredVideosRv;
//        SearchView searchView;
    EditText searchEt;

    ConstraintLayout catsConstraintLayout, videoTitleListCL;

    ArrayList<Category> listOfCatItems = new ArrayList<>();
    public static ArrayList<FilteredData> listOfFilteredVideos = new ArrayList<>();

    boolean searchApiCalling = false;

//    int currentItems,scrolledOutItems,totalItems;

    int page = 0;

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

        searchEt = findViewById(R.id.eTSearch);
//        searchView = findViewById(R.id.searchViewRt);
        catsConstraintLayout = findViewById(R.id.cLCats);
        videoTitleListCL = findViewById(R.id.cLVideoTitleList);

        filteredVideosRv = findViewById(R.id.rVFilteredVideos);

        LinearLayoutManager lLmForFilteredVideos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        filteredVideosRv.setLayoutManager(lLmForFilteredVideos);

        filteredVideosAdapter = new FilteredVideosAdapter(listOfFilteredVideos);
        filteredVideosRv.setAdapter(filteredVideosAdapter);

        searchEt.requestFocus();

        /*searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

                    CallRetrofitForSr(searchView.toString());
                }
                return false;
            }
        });*/

        searchEt.addTextChangedListener(new TextWatcher() {
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

        });
    }

    /*public void filter(String s) {

        ArrayList<FilteredData> temp = new ArrayList<>();

        for (FilteredData fd : listOfFilteredVideos) {
            if (fd.getTitle().toLowerCase(Locale.getDefault()).contains(s)) {
                temp.add(fd);
            }
        }
        filteredVideosAdapter.updateList(temp);
    }*/

    private void CallRetrofitForSa() {

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForHsVideos("newest", "", "buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Log.d(TAG, "onResponse: " + response.body().getData().getCategories());

                listOfCatItems.addAll(response.body().getData().getCategories());

                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: ", t);

            }
        });

    }

    private void CallRetrofitForSr(String s) {

        searchApiCalling = true;

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<ExampleFilteredData> call = retrofitRequestApi.PostDataIntoServerForSearchRes("buzo", s, String.valueOf(page));

        call.enqueue(new Callback<ExampleFilteredData>() {
            @Override
            public void onResponse(Call<ExampleFilteredData> call, Response<ExampleFilteredData> response) {

                page++;

                searchApiCalling = false;

                for (int i = 0; i < response.body().getData().size(); i++) {

                    listOfFilteredVideos.add(response.body().getData().get(i));

                }

                filteredVideosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ExampleFilteredData> call, Throwable t) {
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