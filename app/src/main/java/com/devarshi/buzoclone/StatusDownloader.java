package com.devarshi.buzoclone;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.devarshi.Adapter.FragmentAdapter;
import com.devarshi.Adapter.StatusSaverImageSlideAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class StatusDownloader extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem statusTabItem, savedTabItem;
    ImageView imageViewBtwc;
    ViewPager viewPager;

    BroadcastReceiver brDelete;
    IntentFilter filter;

    // TODO: 05/01/22  Remote config firebase

    TextView rcDownTv;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private static final String RC_TEXT = "test";
    private static final String STAT_DOWN = "statdown";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_status_downloader);


        // TODO: 05/01/22  Remote config firebase

        rcDownTv = findViewById(R.id.tVRcDown);

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
                            Toast.makeText(StatusDownloader.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(StatusDownloader.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });

        tabLayout = findViewById(R.id.tabLayout);
        statusTabItem = findViewById(R.id.tabItemStatus);
        savedTabItem = findViewById(R.id.tabItemSaved);
        imageViewBtwc = findViewById(R.id.backToWhatsAppCardView);
        viewPager = findViewById(R.id.viewPager);

        imageViewBtwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filter = new IntentFilter(StatusSaverImageSlideAdapter.delete);
        //filter.addAction(StatusSaverImageSlideAdapter.delete);

        brDelete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("ArrayListSize", "" + StatusSaverFragment.modelFeedArrayListStatusSaver.size());
//                if (intent.getAction().equals("delete")){
                int position = intent.getIntExtra("position", 0);
                StatusSaverFragment.modelFeedArrayListStatusSaver.remove(position);
//                listAdapterSaved.notifyItemRemoved(position);
                StatusSaverFragment.listAdapterSaved.notifyDataSetChanged();
//                }
            }
        };
        registerReceiver(brDelete, filter);

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1) {

                    fragmentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void displayWelcomeMessage() {

        String welcomeMessage = mFirebaseRemoteConfig.getString(STAT_DOWN);

        if (mFirebaseRemoteConfig.getBoolean(RC_TEXT)) {
            rcDownTv.setAllCaps(true);
        } else {
            rcDownTv.setAllCaps(false);
        }
        rcDownTv.setText(welcomeMessage);
    }
}