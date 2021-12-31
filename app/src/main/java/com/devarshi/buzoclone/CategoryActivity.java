package com.devarshi.buzoclone;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devarshi.Adapter.CategoryVideosAdapter;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;
import com.devarshi.Retrofitclient.Template;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    ImageView backButtonIv, searchButtonIv;
    TextView catNameTv;
    RecyclerView catVideosRv;
    ProgressBar catVideosPb, videoLoadPb;

    SwipeRefreshLayout catVideosSr;

    FloatingActionButton catVideosFab;

    boolean apiCallInProgress = false;

    String videoLoadedIds = "";
    String catLoadedId = "";
    String catName = "";

    ArrayList<Template> listOfTemplateItems = new ArrayList<>();

    CategoryVideosAdapter categoryVideosAdapter;

    int scrolledOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        backButtonIv = findViewById(R.id.iVBack);
        searchButtonIv = findViewById(R.id.iVSearch);
        catNameTv = findViewById(R.id.tVCatName);
        catVideosRv = findViewById(R.id.rVCatVideos);

        catVideosPb = findViewById(R.id.pBCatVideos);

        videoLoadPb = findViewById(R.id.pbVideoLoad);

        catVideosSr = findViewById(R.id.sRCatVideos);

        catVideosFab = findViewById(R.id.fAbCatVideos);

        catVideosSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this,2,GridLayoutManager.VERTICAL,false);
                catVideosRv.setLayoutManager(gridLayoutManager);

                catVideosSr.setRefreshing(false);
            }
        });

        Intent intent = getIntent();

        catName = intent.getStringExtra("catname");
        catLoadedId = String.valueOf(intent.getIntExtra("catid",0));
        Log.d(TAG, "onCreate: catloadedid " + catLoadedId);
        catNameTv.setText(catName);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        catVideosRv.setLayoutManager(gridLayoutManager);

        categoryVideosAdapter = new CategoryVideosAdapter(listOfTemplateItems,this);
        catVideosRv.setAdapter(categoryVideosAdapter);

        CallRetrofitForCatActivity();
        
        backButtonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButtonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this,SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_top,0);
            }
        });

        catVideosRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrolledOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (scrolledOutItems < 5){
                    catVideosFab.setVisibility(View.GONE);
                }
                else if (scrolledOutItems > 5){
                    catVideosFab.setVisibility(View.VISIBLE);
                    catVideosFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            catVideosRv.smoothScrollToPosition(0);
                        }
                    });
                }
            }
        });
    }

    private void CallRetrofitForCatActivity(){

        catVideosPb.setVisibility(View.VISIBLE);

        videoLoadPb.setVisibility(View.VISIBLE);

        apiCallInProgress = true;

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForCatVideos("newest",videoLoadedIds,catLoadedId,"buzo");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                apiCallInProgress = false;

                Log.d(TAG, "onResponse: template " + response.body().getData().getTemplates().size());
                Log.d(TAG, "onResponse: videoloaded id " + videoLoadedIds);

                for (int i = 0; i < response.body().getData().getTemplates().size(); i++) {
                    videoLoadedIds += String.valueOf(response.body().getData().getTemplates().get(i).getId());
                }
                listOfTemplateItems.addAll(response.body().getData().getTemplates());

                categoryVideosAdapter.notifyDataSetChanged();

                videoLoadPb.setVisibility(View.GONE);

                catVideosPb.setVisibility(View.GONE);

                /*for (int i = 0; i < response.body().getData().getTemplates().size(); i++) {

                    if (i < 20) {

                        videoLoadedIds += String.valueOf(response.body().getData().getTemplates().get(i).getId()).concat(",");
                        listOfTemplateItems.add(response.body().getData().getTemplates().get(i));
                    }
                }

                for (int i = 0; i < response.body().getData().getCategories().size(); i++) {
                    listOfCatItems.add(response.body().getData().getCategories().get(i));
                }
                videosAdapter.notifyDataSetChanged();

                catVideosPb.setVisibility(View.GONE);*/
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}