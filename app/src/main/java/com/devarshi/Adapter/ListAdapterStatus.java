package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.StatusViewerActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class ListAdapterStatus extends RecyclerView.Adapter<ListAdapterStatus.StatusViewHolder> {

    final Context context;
    final ArrayList<File> modelFeedArrayListStatus;

    public ListAdapterStatus(Context context, final ArrayList<File> modelFeedArrayList) {

        this.context = context;
        this.modelFeedArrayListStatus = modelFeedArrayList;
    }

    @NotNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_downloader_media_row_item, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusViewHolder holder, int position) {

        File currentFile = modelFeedArrayListStatus.get(position);

        String filePath = currentFile.toString();

        if (currentFile.getAbsolutePath().endsWith(".mp4")) {

            /*Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.iVThumbnail.setImageBitmap(myBitmap);*/
            Glide.with(context).load(filePath).into(holder.imageView);
            holder.playImageView.setVisibility(View.VISIBLE);

            /*holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StatusViewer.class);
                    intent.putExtra("video", filePath);
                    context.startActivity(intent);
                }
            });*/
            /*holder.videoViewStatus.setVideoURI(Uri.parse(filePath));

            holder.videoViewStatus.pause();

            holder.videoViewStatus.seekTo(1);
            Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.iVThumbnail.setImageBitmap(myBitmap);
            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bmThumbnail);
//            holder.videoViewStatus.setBackgroundDrawable(bitmapDrawable);*/
        }
        else {
            /*Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.imageView.setImageBitmap(myBitmap);*/

            Glide.with(context).load(filePath).into(holder.imageView);
            holder.playImageView.setVisibility(View.GONE);

            /*holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StatusViewer.class);
                    intent.putExtra("image", filePath);
                    context.startActivity(intent);
                }
            });*/
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusViewerActivity.class);
                intent.putExtra("modelFeedArrayListStatus", modelFeedArrayListStatus);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelFeedArrayListStatus.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        ImageView playImageView;
//        ImageView imageViewThumbnail;
//        VideoView videoViewStatus;
//        ImageView imageViewVideo;

//        RoundedImageView imageViewImageMedia;
//        RoundedImageView imageViewThumbnail;
        /*VideoView videoView;
        CardView cardViewVideo;*/

        public StatusViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewStatus);
            playImageView = itemView.findViewById(R.id.playImageView);
//            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
//            videoViewStatus = itemView.findViewById(R.id.videoViewStatus);
//            iVThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
//            imageViewVideo = itemView.findViewById(R.id.imageViewThumbnail);
//            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            /*videoView = itemView.findViewById(R.id.videoView);
            cardViewVideo = itemView.findViewById(R.id.cardViewVideoMedia);*/
//            playView = itemView.findViewById(R.id.playView);
        }
    }
}
