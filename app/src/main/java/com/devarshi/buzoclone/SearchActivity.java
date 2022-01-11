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
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

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


    // TODO: 08/01/22 Rewarded Ad (self)
    /*private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    public RewardedAd rewardedAd;
    boolean isLoading;*/


//    int currentItems, scrolledOutItems, totalItems;

//    int page = 0;

    ConstraintLayout cLSearchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cLSearchActivity = findViewById(R.id.searchActivityCl);

        cLSearchActivity.setBackground(getDrawable(R.color.white));

        // TODO: 08/01/22 Rewarded Ad (self)
        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadRewardedAd();*/

        // TODO: 08/01/22 Rewarded Ad Google guided lab


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

        filteredVideosAdapter = new FilteredVideosAdapter(listOfFilteredVideos, listOfVidsItems, this, SearchActivity.this);
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


//        startGame();

        // TODO: 08/01/22 Rewarded Ad (self)
//        showRewardedVideo();


        /*if (rewardedAd != null) {
            Activity activityContext = SearchActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }*/

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


    // TODO: 08/01/22 Rewarded Ad (self)
    /*private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;
                            SearchActivity.this.isLoading = false;
                            Toast.makeText(SearchActivity.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            SearchActivity.this.rewardedAd = rewardedAd;
                            Log.d(TAG, "onAdLoaded");
                            SearchActivity.this.isLoading = false;
                            Toast.makeText(SearchActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });

            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                }
            });
        }
    }

    private void startGame() {

        if (rewardedAd != null && !isLoading) {
            loadRewardedAd();
        }
    }

    private void showRewardedVideo() {

        if (rewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }
        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "onAdShowedFullScreenContent");
                        Toast.makeText(SearchActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d(TAG, "onAdFailedToShowFullScreenContent");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Toast.makeText(
                                SearchActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                        Toast.makeText(SearchActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                        // Preload the next rewarded ad.
                        SearchActivity.this.loadRewardedAd();
                    }
                });
        Activity activityContext = SearchActivity.this;
        rewardedAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                    }
                });
    }*/

    public void filter(String s) {

        ArrayList<String> temp = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("listOfFilteredVideos", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("arrayList", null);

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        listOfFilteredVideos = gson.fromJson(json, type);

        if (listOfFilteredVideos == null){
            listOfFilteredVideos = new ArrayList<>();
        }
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