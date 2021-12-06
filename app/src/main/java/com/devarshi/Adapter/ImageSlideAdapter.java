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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.ExoPlayerManager;
import com.devarshi.buzoclone.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ui.PlayerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ImageSlideAdapter extends RecyclerView.Adapter<ImageSlideAdapter.SlidingViewHolder> {

    final Context context;
    final ArrayList<File> modelFeedArrayList;
    int pos;
    OnPagerItemSelected mListener;

    File currentFile;
    String filePath;
    /*DoubleTapPlayerView playerView;
    player;
    YouTubeOverlay ytOverlay;*/

    File newFile;


//    public static PlayerView playerView;

    public ImageSlideAdapter(Context context, ArrayList<File> modelFeedArrayList, int pos, OnPagerItemSelected mListener) {
        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        this.pos = pos;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public SlidingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_on_click_viewer, parent, false);
        return new SlidingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SlidingViewHolder holder, int position) {

        ExoPlayerManager exoPlayerManager = new ExoPlayerManager(context);

//        ImageView imageViewReplay = itemView.findViewById(R.id.imageViewReplay);

        currentFile = modelFeedArrayList.get(position);
        filePath = currentFile.toString();


        if (filePath.endsWith(".jpg")) {
            Log.d("PositionItem", "" + filePath);

            holder.statusImageView.setVisibility(View.VISIBLE);

            Glide.with(context).load(filePath).into(holder.statusImageView);

            holder.statusImageView.setBackgroundColor(context.getResources().getColor(android.R.color.black));

//            exoPlayerManager.releaseVideo();

        } else {
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
                Uri imageUri = Uri.parse(modelFeedArrayList.get(position).getAbsolutePath());
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

        holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestPermissions();
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
                File file = new File(modelFeedArrayList.get(position).getAbsolutePath());
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
                context.startActivity(Intent.createChooser(intentShare, "Share image using"));

            }
        });

        holder.imageViewDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {

                /*if (filePath.endsWith(".jpg")) {
                    Bitmap myBitMap = BitmapFactory.decodeFile(filePath);
                    addToFavImage(myBitMap, filePath);
                }
                else if (filePath.endsWith(".mp4")){
                    Bitmap myBitMap = BitmapFactory.decodeFile(filePath);
                    addToFavVideo(myBitMap,filePath);
                }*/
                try {
                    copyFile(currentFile,new File(String.valueOf(newFile)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Item Saved", Toast.LENGTH_SHORT).show();
                holder.imageViewDownload.setVisibility(View.GONE);
                holder.imageViewDownloadComplete.setVisibility(View.VISIBLE);
            }
        });

        holder.imageViewDownloadComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item is already downloaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    /*private void createDirectoryAndSaveFile(Bitmap imageToSave,String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Buzo-Video Status MakerOne/Status Downloader-Buzo Video Status MakerOne");

        if (!direct.exists()) {
            File directCreate = new File("/Buzo-Video Status MakerOne/Status Downloader-Buzo Video Status MakerOne");
            directCreate.mkdirs();
        }

        File file = new File("/Buzo-Video Status MakerOne/Status Downloader-Buzo Video Status MakerOne", fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.Q)
    public void addToFavImage(Bitmap bitmap, String filename) {
        *//*int index=filename.lastIndexOf('/');
        String files = filename.substring(0,index);*//*
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        filename = "fav" + timeStamp + ".JPG";

//        UUID uuid = UUID.randomUUID();


        File direct = new File(Environment.getExternalStorageDirectory() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");

        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker", filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "description");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put("_data", file.getAbsolutePath());
        ContentResolver cr = context.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void addToFavVideo(Bitmap bitmap, String filename){
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        filename = "fav" + timeStamp + ".MP4";

        File direct = new File(Environment.getExternalStorageDirectory() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");

        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker", filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "title");
        values.put(MediaStore.Video.Media.DESCRIPTION, "description");
        values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Video.VideoColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/*");
        values.put("_data", file.getAbsolutePath());
        ContentResolver cr = context.getContentResolver();
        cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

    }*/


    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
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


    public static class SlidingViewHolder extends RecyclerView.ViewHolder {

        public PlayerView playerView;
        PhotoView statusImageView;
        ImageView imageViewBack;
        ImageView imageViewSendTo;
        ImageView imageViewShare;
        ImageView imageViewDownload;
        ImageView imageViewDownloadComplete;

        public SlidingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            playerView = itemView.findViewById(R.id.playerViewWhatsAppVideo);
            statusImageView = itemView.findViewById(R.id.statusImageView);
            imageViewBack = itemView.findViewById(R.id.imageViewB);
            imageViewSendTo = itemView.findViewById(R.id.imageViewSendTo);
            imageViewShare = itemView.findViewById(R.id.imageViewShare);
            imageViewDownload = itemView.findViewById(R.id.imageViewDownload);
            imageViewDownloadComplete = itemView.findViewById(R.id.imageViewDownloadComplete);
        }
    }

    public interface OnPagerItemSelected {
        void pagerItemSelected();
    }

}
