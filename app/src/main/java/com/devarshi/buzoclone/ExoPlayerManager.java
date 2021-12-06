package com.devarshi.buzoclone;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import org.jetbrains.annotations.NotNull;

public class ExoPlayerManager {
    public SimpleExoPlayer exoPlayer;
    Context mContext;
    Player.EventListener eventListener;
    public OnVideoInitializedCompleteListener videoInitializedCompleteListener;

    public ExoPlayerManager(Context mContext) {
        this.mContext = mContext;
    }

    public void playVideo() {
        if (exoPlayer != null)
            exoPlayer.setPlayWhenReady(true);
    }

    public void pauseVideo() {
        if (exoPlayer != null)
            exoPlayer.setPlayWhenReady(false);
    }

    public void resetVideo() {

        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop(true);
        }
    }

    public void releaseVideo() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop(false);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    public void initExoplayer(String fileUrl, OnVideoInitializedCompleteListener videoInitializedCompleteListener) {
        this.videoInitializedCompleteListener = videoInitializedCompleteListener;
        if (exoPlayer == null) {
            exoPlayer = (SimpleExoPlayer) new ExoPlayer.Builder(mContext).build();

            MediaItem mediaItem = MediaItem.fromUri(fileUrl);
            exoPlayer.addMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            eventListener = new Player.EventListener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_READY) {
                        videoInitializedCompleteListener.onInitialized();
                    } else if (playbackState == Player.STATE_BUFFERING) {
                    } else if (playbackState == Player.STATE_ENDED) {
                        exoPlayer.release();
                    }
                }

                @Override
                public void onPlayerError(@NotNull PlaybackException error) {
                    videoInitializedCompleteListener.onError(error);
                }

                /*@Override
                public void onPlayerError(ExoPlaybackException error) {
                    videoInitializedCompleteListener.onError(error);
                }*/
            };
            exoPlayer.addListener(eventListener);
        } else {
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(fileUrl)));
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);

        }
    }

    public interface OnVideoInitializedCompleteListener {
        void onError(Exception e);
        void onInitialized();
    }

}

