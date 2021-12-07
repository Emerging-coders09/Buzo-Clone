package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.StatusViewerActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class StatusCardViewAdapter extends RecyclerView.Adapter<StatusCardViewAdapter.StatusViewHolder> {

    final Context context;
    final ArrayList<File> modelFeedArrayList;

    public StatusCardViewAdapter(Context context, final ArrayList<File> modelFeedArrayList) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_thumbnail_media_row_item,parent,false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusViewHolder holder, int position) {

        File currentFile = modelFeedArrayList.get(position);
        String filePath = currentFile.toString();

        if (currentFile.getAbsolutePath().endsWith(".mp4")) {
            holder.cardViewIt.setVisibility(View.VISIBLE);
            Glide.with(context).load(filePath).into(holder.imageViewIt);
            holder.buttonViewPlay.setVisibility(View.VISIBLE);
//            Glide.with(context).load(R.drawable.ic_thumbnail_play).into(holder.imageViewIt);
            /*holder.cardViewIt.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), StatusViewer.class);
                intent.putExtra("video",filePath);
                context.startActivity(intent);
            });*/

        } else {
            holder.cardViewIt.setVisibility(View.VISIBLE);
            Glide.with(context).load(filePath).into(holder.imageViewIt);
            holder.buttonViewPlay.setVisibility(View.GONE);
            /*Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.imageViewIt.setImageBitmap(myBitmap);*/
        }

        holder.cardViewIt.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), StatusViewerActivity.class);
            intent.putExtra("modelFeedArrayListStatus", modelFeedArrayList);
            intent.putExtra("position",position);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewIt;
        ImageView buttonViewPlay;
        CardView cardViewIt;

        public StatusViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewIt = itemView.findViewById(R.id.imageViewIs);
            cardViewIt = itemView.findViewById(R.id.cardViewIT);
            buttonViewPlay = itemView.findViewById(R.id.playButtonView);
        }
    }
}
