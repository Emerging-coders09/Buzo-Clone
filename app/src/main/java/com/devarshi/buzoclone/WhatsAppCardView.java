package com.devarshi.buzoclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Adapter.statusAdapter;

import java.io.File;
import java.util.ArrayList;

public class WhatsAppCardView extends AppCompatActivity {

    RecyclerView statusRecyclerView;

    TextView textViewSeeAll;
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    ImageView backImageView;
    String WHATSAPP_STATUSES_LOCATION = Build.VERSION.SDK_INT <=28 ? "/WhatsApp/Media/.Statuses" : (Build.VERSION.SDK_INT == 29 ? "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses" : "/WhatsApp/Media/.Statuses");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_whats_app_card_view);

        textViewSeeAll = findViewById(R.id.seeAllTextView);
        statusRecyclerView = findViewById(R.id.recyclerViewStatus);

        backImageView = findViewById(R.id.imageViewBack);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        statusRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        statusAdapter statusRecyclerAdapter = new statusAdapter(this,this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION)));
        statusRecyclerView.setAdapter(statusRecyclerAdapter);

        textViewSeeAll.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WhatsAppCardView.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                Intent intent = new Intent(getApplicationContext(), StatusSaver.class);
                startActivity(intent);
            }
    });
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