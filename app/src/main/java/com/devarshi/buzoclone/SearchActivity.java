package com.devarshi.buzoclone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.SearchAdapter;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

public class SearchActivity extends AppCompatActivity {

    ImageView backToHomeImageView;
    SearchAdapter searchAdapter;

    RecyclerView catsRecyclerView;

    ArrayList<String> listOfCatNames = new ArrayList<>();
    ArrayList<String> listOfCatIcons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backToHomeImageView = findViewById(R.id.iVBackToHome);

        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.slide_in_bottom);
            }
        });

        catsRecyclerView = findViewById(R.id.rVCategories);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        catsRecyclerView.setLayoutManager(linearLayoutManager);

        searchAdapter = new SearchAdapter(listOfCatNames,listOfCatIcons,this);
        catsRecyclerView.setAdapter(searchAdapter);

        CallRetrofitForSa();

    }

    private void CallRetrofitForSa(){

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServer("newest","","buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Log.d(TAG, "onResponse: " + response.body().getData().getCategories());

                for (int i = 0; i < response.body().getData().getCategories().size(); i++) {
                    listOfCatNames.add(response.body().getData().getCategories().get(i).getName());
                    listOfCatIcons.add(response.body().getData().getCategories().get(i).getImageUrl());
                }

                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: ",t);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,R.anim.slide_in_bottom);
    }
}