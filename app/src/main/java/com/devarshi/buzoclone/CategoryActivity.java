package com.devarshi.buzoclone;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devarshi.Adapter.CategoryVideosAdapter;
import com.devarshi.Retrofitclient.Example;
import com.devarshi.Retrofitclient.RetrofitRequestApi;
import com.devarshi.Retrofitclient.Retrofitclient;
import com.devarshi.Retrofitclient.Template;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

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

    // TODO: 05/01/22 Firebase remote config ends

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    TextView rCCatTextTv;

    private static final String CAT_KEY = "catkey";
    private static final String RC_TEXT = "test";

    // TODO: 06/01/22

    ConstraintLayout catActivityCl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catActivityCl = findViewById(R.id.cLCatActivity);

        catActivityCl.setBackgroundColor(getResources().getColor(R.color.white));

        // TODO: 05/01/22 Firebase remote config starts
        rCCatTextTv = findViewById(R.id.tVRcCatText);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            Toast.makeText(CategoryActivity.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CategoryActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });

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
                GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 2, GridLayoutManager.VERTICAL, false);
                catVideosRv.setLayoutManager(gridLayoutManager);

                catVideosSr.setRefreshing(false);
            }
        });

        Intent intent = getIntent();

        catName = intent.getStringExtra("catname");
        catLoadedId = String.valueOf(intent.getIntExtra("catid", 0));
        Log.d(TAG, "onCreate: catloadedid " + catLoadedId);
        catNameTv.setText(catName);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        catVideosRv.setLayoutManager(gridLayoutManager);

        categoryVideosAdapter = new CategoryVideosAdapter(listOfTemplateItems, this);
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
                startActivity(new Intent(CategoryActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_top, 0);
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

                if (scrolledOutItems < 5) {
                    catVideosFab.setVisibility(View.GONE);
                } else if (scrolledOutItems > 5) {
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

    private void displayWelcomeMessage() {
        String welcomeMessage = mFirebaseRemoteConfig.getString(CAT_KEY);

        if (mFirebaseRemoteConfig.getBoolean(RC_TEXT)) {
            rCCatTextTv.setAllCaps(true);
        } else {
            rCCatTextTv.setAllCaps(false);
        }
        rCCatTextTv.setText(welcomeMessage);
    }

    private void CallRetrofitForCatActivity() {

        catVideosPb.setVisibility(View.VISIBLE);

        videoLoadPb.setVisibility(View.VISIBLE);

        apiCallInProgress = true;

        RetrofitRequestApi retrofitRequestApi = Retrofitclient.getRetrofit().create(RetrofitRequestApi.class);

        Call<Example> call = retrofitRequestApi.PostDataIntoServerForCatVideos("newest", videoLoadedIds, catLoadedId, "buzo");

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