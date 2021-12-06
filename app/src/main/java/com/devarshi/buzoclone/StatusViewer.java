package com.devarshi.buzoclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.devarshi.Adapter.ImageSlideAdapter;

import java.io.File;
import java.util.ArrayList;

public class StatusViewer extends AppCompatActivity implements ImageSlideAdapter.OnPagerItemSelected {

    ViewPager2 viewPager;
    ArrayList<File> modelFeedArrayList;
    int position;
    private int startPosition = 0;
    public int currentPage = 0;
    ImageSlideAdapter.SlidingViewHolder slidingViewHolder;

    final ExoPlayerManager exoPlayerManager = new ExoPlayerManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_status_viewer);

        viewPager = findViewById(R.id.viewPager);

        Intent intent = getIntent();

        modelFeedArrayList = (ArrayList<File>) getIntent().getSerializableExtra("modelFeedArrayList");
        position = intent.getIntExtra("position", 0);
        ImageSlideAdapter imageSlideAdapter = new ImageSlideAdapter(this, modelFeedArrayList, position, this);
        viewPager.setAdapter(imageSlideAdapter);
        viewPager.setCurrentItem(position,false);
        viewPager.setOffscreenPageLimit(1);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int p, float positionOffset, int positionOffsetPixels) {
                exoPlayerManager.releaseVideo();

                RecyclerView rv = (RecyclerView) viewPager.getChildAt(0);
                slidingViewHolder = (ImageSlideAdapter.SlidingViewHolder) rv.findViewHolderForAdapterPosition(p);

                File currentFile = modelFeedArrayList.get(p);
                String filePath = currentFile.toString();

                if (filePath.endsWith(".mp4")) {
                    exoPlayerManager.initExoplayer(filePath, new ExoPlayerManager.OnVideoInitializedCompleteListener() {
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onInitialized() {
                            slidingViewHolder.playerView.setPlayer(exoPlayerManager.exoPlayer);
                            exoPlayerManager.playVideo();
                        }
                    });
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

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                *//*View v=viewPager.getChildAt(position);
                PlayerView playerView=v.findViewById(R.id.playerViewWhatsAppVideo);*//*
                if (modelFeedArrayList.get(position).getAbsolutePath().endsWith(".mp4")) {
                    ImageSlideAdapter.player.play();
                }
                *//*File currentFile = modelFeedArrayList.get(position);
                String filePath = currentFile.toString();

                if (filePath.endsWith(".jpg") || currentPage == position || modelFeedArrayList.get(currentPage).getAbsolutePath().endsWith(".jpg")){
                    currentPage = position;
                    ImageSlideAdapter.player.pause();
                }
                else if (filePath.endsWith(".mp4") || modelFeedArrayList.get(currentPage).getAbsolutePath().endsWith(".mp4")){
                    currentPage = position;
                    ImageSlideAdapter.player.play();
                }*//*

            }

            @Override
            public void onPageSelected(int position) {
//                currentPage=position;

//                Log.i("Position",""+position);
                File currentFile = modelFeedArrayList.get(position);
                String filePath = currentFile.toString();
                if (filePath.endsWith(".jpg") || currentPage == position){
                    if (ImageSlideAdapter.player != null && position != 0) {
                        currentPage = position;
                        if (modelFeedArrayList.get(position-1).getAbsolutePath().endsWith(".jpg") || modelFeedArrayList.get(position+1).getAbsolutePath().endsWith(".jpg")) {
                            ImageSlideAdapter.player.pause();
                        }
                    }
                }
                else if (filePath.endsWith(".mp4")){
                    imageSlideAdapter.getItemPosition(position);
                    if (filePath.endsWith(".jpg") || currentPage == position) {
                        currentPage = position;
                        if (modelFeedArrayList.get(position-1).getAbsolutePath().endsWith(".jpg") || modelFeedArrayList.get(position+1).getAbsolutePath().endsWith(".jpg")) {
                            ImageSlideAdapter.player.pause();
                        }
                    } else if (modelFeedArrayList.get(currentPage).getAbsolutePath().endsWith(".mp4")) {
                        if (modelFeedArrayList.get(position+1).getAbsolutePath().endsWith(".jpg")) {
                            ImageSlideAdapter.player.pause();
                        }
                        if (filePath.endsWith(".jpg")) {
                            if (modelFeedArrayList.get(position-1).getAbsolutePath().endsWith(".jpg") || modelFeedArrayList.get(position+1).getAbsolutePath().endsWith(".jpg")) {
                                ImageSlideAdapter.player.pause();
                            }
                        } else if (filePath.endsWith(".mp4")) {
                            ImageSlideAdapter.player.play();
                        }
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        /*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                ImageSlideAdapter.player.setPlayWhenReady(false);
            }

            @Override
            public void onPageSelected(int position) {
                File currentFile = modelFeedArrayList.get(position);
                String filePath = currentFile.toString();
                if (filePath.endsWith(".jpg") || currentPage == position){
                    currentPage = position;
                    ImageSlideAdapter.player.pause();
                }
                else {
                    currentPage = position;
                    ImageSlideAdapter.player.play();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state){
            }
        });*/

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageSlideAdapter.player.release();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        /*imageViewStatusImage = findViewById(R.id.imageViewStatusImage);

        playerView = findViewById(R.id.previewPlayerView);
        ytOverlay = findViewById(R.id.ytOverlay);

        intent = getIntent();

        if (intent.hasExtra("image")) {
            imageViewStatusImage.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            ytOverlay.setVisibility(View.GONE);
            Bitmap myBitmap = BitmapFactory.decodeFile(intent.getStringExtra("image"));
            imageViewStatusImage.setImageBitmap(myBitmap);
        }

        else if (intent.hasExtra("video")) {
            imageViewStatusImage.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            ytOverlay.setVisibility(View.VISIBLE);
            player = new SimpleExoPlayer.Builder(this).build();

            MediaItem mediaItem = MediaItem.fromUri(intent.getStringExtra("video"));
            player.addMediaItem(mediaItem);

            playerView.setPlayer(player);
            ytOverlay.player(player);
            player.prepare();
            player.play();
            initDoubleTapPlayerView();
            playerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        }*/

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

    /*private void setStartPosition() {
        if (startPosition >= 0) {
            if (startPosition > modelFeedArrayList.size()) {
                viewPager.setCurrentItem((modelFeedArrayList.size() - 1));
                return;
            }
            viewPager.setCurrentItem(startPosition);
        } else {
            viewPager.setCurrentItem(0);
        }
        viewPager.setOffscreenPageLimit(0);
    }*/


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

    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player!=null) {
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
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
    }*/

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
//        ImageSlideAdapter.player.pause();
    }*/
}