package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    ImageView backButtonIv, searchButtonIv;
    TextView catNameTv;
    RecyclerView catVideosRv;
    ProgressBar catVideosPb;

    boolean apiCallInProgress = false;

    String videoLoadedIds = "";
    String catLoadedIds = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        backButtonIv = findViewById(R.id.iVBack);
        searchButtonIv = findViewById(R.id.iVSearch);
        catNameTv = findViewById(R.id.tVCatName);
        catVideosRv = findViewById(R.id.rVCatVideos);

        catVideosPb = findViewById(R.id.pBCatVideos);

        backButtonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButtonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CallRetrofitForCatActivity(){

        catVideosPb.setVisibility(View.VISIBLE);

        apiCallInProgress = true;

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForCatVideos("newest",videoLoadedIds,catLoadedIds,"buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

    }
}