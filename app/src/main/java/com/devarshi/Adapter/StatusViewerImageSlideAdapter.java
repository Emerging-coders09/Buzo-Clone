package com.devarshi.Adapter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.BuildConfig;
import com.devarshi.buzoclone.ExoPlayerManager;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.StatusSaverFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ui.PlayerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StatusViewerImageSlideAdapter extends RecyclerView.Adapter<StatusViewerImageSlideAdapter.StatusViewerSlidingViewHolder> {

    final Context context;
    final ArrayList<File> modelFeedArrayListStatusViewer;
    int pos;
    OnPagerItemSelected mListener;


    /*DoubleTapPlayerView playerView;
    player;
    YouTubeOverlay ytOverlay;*/


//    public static PlayerView playerView;

    public StatusViewerImageSlideAdapter(Context context, ArrayList<File> modelFeedArrayListStatusViewer, int pos, OnPagerItemSelected mListener) {
        this.context = context;
        this.modelFeedArrayListStatusViewer = modelFeedArrayListStatusViewer;
        this.pos = pos;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public StatusViewerSlidingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_on_click_viewer, parent, false);
        return new StatusViewerSlidingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusViewerSlidingViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ExoPlayerManager exoPlayerManager = new ExoPlayerManager(context);

//        ImageView imageViewReplay = itemView.findViewById(R.id.imageViewReplay);

        File currentFile = modelFeedArrayListStatusViewer.get(position);
        String filePath = currentFile.toString();


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
                Uri mediaUri = Uri.parse(modelFeedArrayListStatusViewer.get(position).getAbsolutePath());
                Intent whatsAppIntent = new Intent(Intent.ACTION_SEND);
                whatsAppIntent.setPackage("com.whatsapp");
                whatsAppIntent.putExtra(Intent.EXTRA_STREAM, mediaUri);
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
                    File file = new File(modelFeedArrayListStatusViewer.get(position).getAbsolutePath());
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
        } else if (filePath.endsWith(".mp4")) {
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

        /*holder.imageViewDownload.setVisibility(View.VISIBLE);
        holder.imageViewDownloadComplete.setVisibility(View.GONE);

        holder.imageViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavVideo(filePath);
                Toast.makeText(context, "Item Saved", Toast.LENGTH_SHORT).show();
            }
        });*/

        int indexExistence = filePath.lastIndexOf('/');
        String filenameExistence = filePath.substring(indexExistence);
        holder.imageViewDownload.setVisibility(View.VISIBLE);
        holder.imageViewDownloadComplete.setVisibility(View.GONE);

        File fileExistence = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + StatusSaverFragment.SAVED_FILES_LOCATION, filenameExistence);

        if (!fileExistence.exists()) {

            holder.imageViewDownload.setVisibility(View.VISIBLE);
            holder.imageViewDownloadComplete.setVisibility(View.GONE);

            holder.imageViewDownload.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onClick(View v) {
//                        Bitmap myBitMap = BitmapFactory.decodeFile(filePath);
                    downloadItem(filePath);

                    Toast.makeText(context, "Item Saved", Toast.LENGTH_SHORT).show();
                    holder.imageViewDownload.setVisibility(View.GONE);
                    holder.imageViewDownloadComplete.setVisibility(View.VISIBLE);

                    holder.imageViewDownloadComplete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Item already downloaded!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else if (fileExistence.exists()) {
            holder.imageViewDownload.setVisibility(View.GONE);
            holder.imageViewDownloadComplete.setVisibility(View.VISIBLE);

            holder.imageViewDownloadComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Item already downloaded!", Toast.LENGTH_SHORT).show();
                }
            });
        }



        /*if (fileExistence.exists()) {

            holder.imageViewDownloadComplete.setVisibility(View.VISIBLE);
            holder.imageViewDownload.setVisibility(View.GONE);

            holder.imageViewDownloadComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Item is already downloaded!", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void downloadItem(String filePath) {

        try {
            File currentFile = new File(filePath);
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Buzo-VideoStatusMakerOne");

            directory.mkdir();

            if (!directory.exists()) {
                File imageStoreDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Buzo-VideoStatusMakerOne");
                imageStoreDirectory.mkdir();
            }

            int index = filePath.lastIndexOf('/');
            String filename = filePath.substring(index);
            File newFile = new File(directory, filename);

            if (currentFile.exists()) {

                InputStream inputStream = new FileInputStream(currentFile);
                OutputStream outputStream = new FileOutputStream(newFile);

                byte[] buf = new byte[1024];
                int len;

                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }

                outputStream.flush();
                inputStream.close();
                outputStream.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            OutputStream out;
            InputStream inputStream = new FileInputStream(filename1);

            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            File createDir = new File(root + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker" + File.separator);
            if (!createDir.exists()) {
                createDir.mkdir();
            }
            File file = new File(root + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker" + File.separator + filename1);
            file.createNewFile();
            out = new FileOutputStream(file);

            byte[] data = new byte[1024];
            int len;

            while ((len = inputStream.read(data)) > 0) {
                out.write(data, 0, len);
            }
            out.write(data);
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }*/
        /*int index = filename1.lastIndexOf('/');
        String filename = filename1.substring(index);
        *//*Log.e("filesales", "" + filename);
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        filename = "fav" + timeStamp + ".jpg";*//*

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
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
    }

    /*public void addToFavVideo(String filePath) {

        try {
            File currentFile = new File(filePath);
            File loc = Environment.getExternalStorageDirectory();
            File directory = new File(loc.getAbsolutePath() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");

            *//*if (!directory.exists()) {
                File videoStoreDirectory = new File(loc.getAbsolutePath() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");
                videoStoreDirectory.mkdirs();
            }*//*

            if (!directory.exists()) {
                File videoStoreDir = new File(loc.getAbsolutePath() + "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker");
                directory.mkdirs();
            }

            int index = filePath.lastIndexOf('/');
            String filename = filePath.substring(index);
            File newFile = new File(directory, filename);

            if (currentFile.exists()) {

                InputStream inputStream = new FileInputStream(currentFile);
                OutputStream outputStream = new FileOutputStream(newFile);

                byte[] buf = new byte[1024];
                int len;

                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }

                outputStream.flush();
                inputStream.close();
                outputStream.close();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/

    /*@RequiresApi(api = Build.VERSION_CODES.Q)
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
        return modelFeedArrayListStatusViewer.size();
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


    public static class StatusViewerSlidingViewHolder extends RecyclerView.ViewHolder {

        public PlayerView playerView;
        PhotoView statusImageView;
        ImageView imageViewBack;
        ImageView imageViewSendTo;
        ImageView imageViewShare;
        ImageView imageViewDownload;
        ImageView imageViewDownloadComplete;

        public StatusViewerSlidingViewHolder(@NonNull @NotNull View itemView) {
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