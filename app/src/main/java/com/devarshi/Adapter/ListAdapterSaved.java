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
import com.devarshi.buzoclone.StatusSaverActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class ListAdapterSaved extends RecyclerView.Adapter<ListAdapterSaved.SavedViewHolder> {

    final Context context;
    final ArrayList<File> modelFeedArrayListSaved;

    public ListAdapterSaved(Context context, ArrayList<File> modelFeedArrayListSaved) {
        this.context = context;
        this.modelFeedArrayListSaved = modelFeedArrayListSaved;
    }

    @NonNull
    @NotNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_saver_media_row_item,parent,false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SavedViewHolder holder, int position) {

        File currentFile = modelFeedArrayListSaved.get(position);

        String filePath = currentFile.toString();

        if (filePath.endsWith(".mp4")){
            Glide.with(context).load(filePath).into(holder.imageViewRounded);
            holder.imageViewPlay.setVisibility(View.VISIBLE);
        }

        else if (filePath.endsWith(".jpg")){
            Glide.with(context).load(filePath).into(holder.imageViewRounded);
            holder.imageViewPlay.setVisibility(View.GONE);
        }



        holder.imageViewRounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusSaverActivity.class);
                intent.putExtra("modelFeedArrayListSaved",modelFeedArrayListSaved);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

        /*Intent intent1 = new Intent();
        intent1.putExtra("position",position);
        intent1.setAction(delete);
        context.sendBroadcast(intent1);
        ((StatusSaverFragment) context).finish();*/
    }

    @Override
    public int getItemCount() {
        return modelFeedArrayListSaved.size();
    }

    /*public interface RefreshAdapter{
        void refresh(int p);
    }*/



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class SavedViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imageViewRounded;
        ImageView imageViewPlay;

        public SavedViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewRounded = itemView.findViewById(R.id.imageViewSaved);
            imageViewPlay = itemView.findViewById(R.id.playImageViewSs);
        }
    }
}
