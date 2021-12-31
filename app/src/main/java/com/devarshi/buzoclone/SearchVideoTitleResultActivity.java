package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.SearchVideoResultAdapter;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;
import com.devarshi.Retrofitclient.Template;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVideoTitleResultActivity extends AppCompatActivity {


    ArrayList<Template> listForVideos = new ArrayList<>();
    String videoTitle = "";
//    String videoId = "";
    //    int pos;
    SearchView selectedFilVidsSv;
    ImageView selectedFilVidsBkIv;

    RecyclerView selectedFilVidsRv;
    SearchVideoResultAdapter searchVideoResultAdapter;

    private static final String TAG = "SearchVideoTitleResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video_title_result);

        selectedFilVidsSv = findViewById(R.id.sVSelectedFilteredVids);
        selectedFilVidsBkIv = findViewById(R.id.iVFilVidsBack);

        selectedFilVidsRv = findViewById(R.id.rVSelectedFilVids);

        Intent intent = getIntent();

        videoTitle = intent.getStringExtra("title");

        Log.d(TAG, "onCreate: listForVideos: " + videoTitle);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        selectedFilVidsRv.setLayoutManager(gridLayoutManager);

        searchVideoResultAdapter = new SearchVideoResultAdapter(this, listForVideos,videoTitle);
        selectedFilVidsRv.setAdapter(searchVideoResultAdapter);

        CallRetrofitForVideos();

        selectedFilVidsSv.requestFocus();

        selectedFilVidsBkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void CallRetrofitForVideos() {

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForHsVideos("newest", "", "buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                listForVideos.addAll(response.body().getData().getTemplates());

                searchVideoResultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });
    }
}