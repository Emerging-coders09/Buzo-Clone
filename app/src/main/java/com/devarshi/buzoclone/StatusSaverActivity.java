package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.devarshi.Adapter.StatusSaverImageSlideAdapter;

import java.io.File;
import java.util.ArrayList;

public class StatusSaverActivity extends AppCompatActivity implements StatusSaverImageSlideAdapter.OnPagerItemSelected {

    ViewPager2 viewPagerSaver;
    ArrayList<File> modelFeedArrayListStatusSaver;
    int position;
    StatusSaverImageSlideAdapter.StatusSaverSlidingViewHolder statusSaverSlidingViewHolder;

    final ExoPlayerManager exoPlayerManager = new ExoPlayerManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_status_saver);

        viewPagerSaver = findViewById(R.id.viewPagerSs);

        Intent intent = getIntent();

        modelFeedArrayListStatusSaver = (ArrayList<File>) getIntent().getSerializableExtra("modelFeedArrayListSaved");
        position = intent.getIntExtra("position", 0);

        StatusSaverImageSlideAdapter saverImageSlideAdapter = new StatusSaverImageSlideAdapter(StatusSaverActivity.this, modelFeedArrayListStatusSaver, position, this);
        viewPagerSaver.setAdapter(saverImageSlideAdapter);
        viewPagerSaver.setCurrentItem(position, false);
        viewPagerSaver.setOffscreenPageLimit(1);

        viewPagerSaver.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int p, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                exoPlayerManager.releaseVideo();

                RecyclerView rv = (RecyclerView) viewPagerSaver.getChildAt(0);
                statusSaverSlidingViewHolder = (StatusSaverImageSlideAdapter.StatusSaverSlidingViewHolder) rv.findViewHolderForAdapterPosition(p);

                if (modelFeedArrayListStatusSaver.size() > 0) {
                    File currentFile = modelFeedArrayListStatusSaver.get(p);
                    String filePath = currentFile.toString();

                    if (filePath.endsWith(".mp4")) {
                        exoPlayerManager.initExoplayer(filePath, new ExoPlayerManager.OnVideoInitializedCompleteListener() {
                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onInitialized() {
                                statusSaverSlidingViewHolder.playerView.setPlayer(exoPlayerManager.exoPlayer);
                                exoPlayerManager.playVideo();
                            }
                        });
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayerManager.exoPlayer != null) {
            exoPlayerManager.pauseVideo();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exoPlayerManager.exoPlayer != null) {
            exoPlayerManager.pauseVideo();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (exoPlayerManager.exoPlayer != null) {
            exoPlayerManager.playVideo();
        }
    }

    @Override
    public void pagerItemSelected() {
        finish();
        exoPlayerManager.releaseVideo();
    }
}