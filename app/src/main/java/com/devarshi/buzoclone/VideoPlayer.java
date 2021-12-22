package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayer extends AppCompatActivity {

    //    DoubleTapPlayerView playerView;
    ExoPlayer player;
    PlayerView playerView;
    String singleURL = null;

//    ProgressBar progressBarVideo;
//    YouTubeOverlay ytOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.previewPlayerView);

//        progressBarVideo = findViewById(R.id.videoPb);
//        ytOverlay = findViewById(R.id.ytOverlay);

        playerView.setBackgroundColor(this.getResources().getColor(android.R.color.black));

        player = new ExoPlayer.Builder(VideoPlayer.this).build();

        Intent intent = getIntent();

        singleURL = intent.getStringExtra("dataForVideoUrls");

        MediaItem mediaItem = MediaItem.fromUri(singleURL);
        player.addMediaItem(mediaItem);

        playerView.setPlayer(player);
//        ytOverlay.player(player);
        player.prepare();

        /*if (player.isLoading()){
            progressBarVideo.setVisibility(View.VISIBLE);
        }
        else {
            progressBarVideo.setVisibility(View.GONE);
        }*/


        player.play();
//        initDoubleTapPlayerView();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.release();
    }

    /*private void initDoubleTapPlayerView() {

        YouTubeOverlay.PerformListener performListener = new YouTubeOverlay.PerformListener() {
            @Override
            public void onAnimationStart() {
                playerView.setUseController(false);
                ytOverlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
                playerView.setUseController(true);
                ytOverlay.setVisibility(View.GONE);
            }
        };

        playerView.setDoubleTapDelay(800);
        playerView.controller(ytOverlay);
        playerView.setDoubleTapEnabled(true);
        ytOverlay.performListener(performListener);
    }*/

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()) {
            player.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}