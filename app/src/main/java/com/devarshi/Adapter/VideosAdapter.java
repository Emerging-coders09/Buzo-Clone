package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.videoPlayer;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.videosHolder> {

    //    Intent intent;
    Context mContext;

    ArrayList<String> dataForTitle;
    ArrayList<String> dataForVideoThumbnails;
    ArrayList<String> dataForVideoUrls;

    public VideosAdapter(ArrayList<String> dataForTitle, ArrayList<String> dataForVideoThumbnails, ArrayList<String> dataForVideoUrls, Context mContext) {
        this.dataForTitle = dataForTitle;
        this.dataForVideoThumbnails = dataForVideoThumbnails;
        this.dataForVideoUrls = dataForVideoUrls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public videosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.home_screen_video_layout, parent, false);
        return new videosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.videosHolder holder, int i) {

        holder.diwaliTextView.setText(dataForTitle.get(i));

        /*Bitmap myBitmap = BitmapFactory.decodeFile(da);
        holder.iVThumbnail.setImageBitmap(myBitmap);*/

//        Picasso.with(con).load(dataForVideoThumbnails.get(i)).into(holder.diwaliImageView);

        Glide.with(mContext).load(dataForVideoThumbnails.get(i)).into(holder.diwaliImageView);

        holder.diwaliImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, videoPlayer.class);
                intent.putExtra("dataForVideoUrls",dataForVideoUrls.get(i));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataForTitle.size();
    }

    public class videosHolder extends RecyclerView.ViewHolder {

        ImageView diwaliImageView;
        TextView diwaliTextView;


        public videosHolder(@NonNull View itemView) {
            super(itemView);
            diwaliImageView = itemView.findViewById(R.id.imageViewDiwali);
            diwaliTextView = itemView.findViewById(R.id.textViewDiwali);
//            itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), videoPlayer.class);
            v.getContext().startActivity(intent);
        }*/
    }
}
