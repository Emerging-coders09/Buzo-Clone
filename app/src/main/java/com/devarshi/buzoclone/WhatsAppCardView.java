package com.devarshi.buzoclone;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.StatusCardViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.File;
import java.util.ArrayList;

public class WhatsAppCardView extends AppCompatActivity {

    RecyclerView statusRecyclerView;

    TextView textViewSeeAll;
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    ImageView backImageView;
    String WHATSAPP_STATUSES_LOCATION = Build.VERSION.SDK_INT <= 28 ? "/WhatsApp/Media/.Statuses" : (Build.VERSION.SDK_INT <= 30 ? "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses" : "/WhatsApp/Media/.Statuses");
//    String WHATSAPP_STATUSES_LOCATION = Build.VERSION.SDK_INT <= 30 ? "WhatsApp/Media/.Statuses" : "Android/media/com.whatsapp/WhatsApp/Media/.Statuses";

//    String WHATSAPP_STATUSES_LOCATION = "Android/media/com.whatsapp/WhatsApp/Media/.Statuses";

    Button testCrashButton;

    TextView wacvTv;

    private static final String WACV_TV = "wacvTv";
    private static final String RCTEXT_VALUE_2 = "test2";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_whats_app_card_view);

        wacvTv = findViewById(R.id.tVWacv);

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
                            Toast.makeText(WhatsAppCardView.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(WhatsAppCardView.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });

        textViewSeeAll = findViewById(R.id.seeAllTextView);
        statusRecyclerView = findViewById(R.id.recyclerViewStatus);

        backImageView = findViewById(R.id.imageViewBack);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        testCrashButton = findViewById(R.id.bTTestCrash);

        testCrashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("Test Crash");
            }
        });

        statusRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        StatusCardViewAdapter statusRecyclerAdapter = new StatusCardViewAdapter(this, this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION)));
        statusRecyclerView.setAdapter(statusRecyclerAdapter);

        textViewSeeAll.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WhatsAppCardView.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                Intent intent = new Intent(getApplicationContext(), StatusDownloader.class);
                startActivity(intent);
            }
        });
    }

    private void displayWelcomeMessage() {
        // [START get_config_values]
        String welcomeMessage = mFirebaseRemoteConfig.getString(WACV_TV);
        // [END get_config_values]
        if (mFirebaseRemoteConfig.getBoolean(RCTEXT_VALUE_2)) {
            wacvTv.setAllCaps(true);
        } else {
            wacvTv.setAllCaps(false);
        }
        wacvTv.setText(welcomeMessage);
    }

    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".gif") || file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file)) {
                        inFiles.add(file);
                    }
                }
                Log.i("FileExistence", file.getName());
            }
        }
        return inFiles;
    }
}