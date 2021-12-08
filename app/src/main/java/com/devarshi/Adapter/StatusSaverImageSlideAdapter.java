package com.devarshi.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.BuildConfig;
import com.devarshi.buzoclone.ExoPlayerManager;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.StatusSaverActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ui.PlayerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StatusSaverImageSlideAdapter extends RecyclerView.Adapter<StatusSaverImageSlideAdapter.StatusSaverSlidingViewHolder> {

    final Context context;
    final ArrayList<File> modelFeedArrayListStatusSaver;
    int pos;
    OnPagerItemSelected mListener;

    public StatusSaverImageSlideAdapter(Context context, ArrayList<File> modelFeedArrayListStatusSaver, int pos, OnPagerItemSelected mListener) {
        this.context = context;
        this.modelFeedArrayListStatusSaver = modelFeedArrayListStatusSaver;
        this.pos = pos;
        this.mListener = mListener;
    }

    @NonNull
    @NotNull
    @Override
    public StatusSaverSlidingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_on_click_saver,parent,false);
        return new StatusSaverSlidingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusSaverSlidingViewHolder holder, int position) {

        ExoPlayerManager exoPlayerManager = new ExoPlayerManager(context);

        File currentFile = modelFeedArrayListStatusSaver.get(position);
        String filePath = currentFile.toString();

        if (filePath.endsWith(".jpg")){
            holder.statusImageView.setVisibility(View.VISIBLE);

            Glide.with(context).load(filePath).into(holder.statusImageView);

            holder.statusImageView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        }

        else {
            holder.statusImageView.setVisibility(View.GONE);
            holder.playerView.setVisibility(View.VISIBLE);
            holder.playerView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        }
        holder.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.pagerItemSelected();
                if (exoPlayerManager.exoPlayer != null) {
                    exoPlayerManager.resetVideo();
                }
            }
        });

        holder.imageViewSendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = Uri.parse(modelFeedArrayListStatusSaver.get(position).getAbsolutePath());
                Intent whatsAppIntent = new Intent(Intent.ACTION_SEND);
                whatsAppIntent.setPackage("com.whatsapp");
                whatsAppIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                whatsAppIntent.setType("image/*");
                whatsAppIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    context.startActivity(whatsAppIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        if (filePath.endsWith(".jpg")) {
            holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkAndRequestPermissions();
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
                    File file = new File(modelFeedArrayListStatusSaver.get(position).getAbsolutePath());
                    Intent intentShare;

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                        fileOutputStream.flush();
                        fileOutputStream.close();
                        intentShare = new Intent(Intent.ACTION_SEND);
                        intentShare.setType("image/*");
                        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        intentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        throw new RuntimeException(e);

                    }
                    context.startActivity(Intent.createChooser(intentShare, "Share item using"));

                }
            });
        }
        else if (filePath.endsWith(".mp4")){
            holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //old method (which includes context, context.getPackageName())
                    /*File videoFile = new File(filePath);
                    Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                            ? FileProvider.getUriForFile(context, context.getPackageName(), videoFile)
                            : Uri.fromFile(videoFile);
                    ShareCompat.IntentBuilder.from((Activity) context)
                            .setStream(videoURI)
                            .setType("video/mp4")
                            .setChooserTitle("Share video")
                            .startChooser();*/

                    File videoFile = new File(filePath);
                    Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                            ? FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider", videoFile)
                            : Uri.fromFile(videoFile);
                    ShareCompat.IntentBuilder.from((Activity) context)
                            .setStream(videoURI)
                            .setType("video/mp4")
                            .setChooserTitle("Share video")
                            .startChooser();
                }
            });
        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                    ((StatusSaverActivity)context).finish();
                    Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelFeedArrayListStatusSaver.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private boolean checkAndRequestPermissions() {

        int Write = ContextCompat.checkSelfPermission((Activity) context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int Read = ContextCompat.checkSelfPermission((Activity) context,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (Read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    listPermissionsNeeded.toArray
                            (new String[listPermissionsNeeded.size()]), 101);
            return false;
        }
        return true;
    }

    public static class StatusSaverSlidingViewHolder extends RecyclerView.ViewHolder{

        public PlayerView playerView;
        PhotoView statusImageView;
        ImageView imageViewBack;
        ImageView imageViewSendTo;
        ImageView imageViewShare;
        ImageView imageViewDelete;

        public StatusSaverSlidingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            playerView = itemView.findViewById(R.id.playerViewWav);
            statusImageView = itemView.findViewById(R.id.statusIv);
            imageViewBack = itemView.findViewById(R.id.imageViewBs);
            imageViewSendTo = itemView.findViewById(R.id.imageViewSt);
            imageViewShare = itemView.findViewById(R.id.imageViewSh);
            imageViewDelete = itemView.findViewById(R.id.imageViewDel);
        }
    }

    public interface OnPagerItemSelected{
        void pagerItemSelected();
    }
}
