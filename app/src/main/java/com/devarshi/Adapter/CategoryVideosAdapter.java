package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.Retrofitclient.Template;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.VideoPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoryVideosAdapter extends RecyclerView.Adapter<CategoryVideosAdapter.CategoryVideosViewHolder> {

    ArrayList<Template> dataForTempItems;
    Context mContext;

    public CategoryVideosAdapter(ArrayList<Template> dataForTempItems, Context mContext) {
        this.dataForTempItems = dataForTempItems;
        this.mContext = mContext;
    }


    @NonNull
    @NotNull
    @Override
    public CategoryVideosViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_screen_video_layout,parent,false);
        return new CategoryVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryVideosViewHolder holder, int position) {

        Glide.with(mContext).load(dataForTempItems.get(position).getThumbUrl()).into(holder.hSVideoImageView);

        holder.hSVideoTextView.setText(dataForTempItems.get(position).getTitle());

        if (dataForTempItems.get(position).getIsHot() && dataForTempItems.get(position).getIsNew()){

            holder.classifyCardView.setVisibility(View.VISIBLE);
//                ((VideosHolder) holder).classifyTextView.setVisibility(View.VISIBLE);

            holder.classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                /*drawable.setColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                ((VideosHolder) holder).classifyCardView.setBackground(drawable);*/
            holder.classifyTextView.setText("HOT");
        }
        else if (dataForTempItems.get(position).getIsHot() && !dataForTempItems.get(position).getIsNew()){

            holder.classifyCardView.setVisibility(View.VISIBLE);
//                ((VideosHolder) holder).classifyTextView.setVisibility(View.VISIBLE);

            holder.classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                /*drawable.setColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                ((VideosHolder) holder).classifyCardView.setBackground(drawable);*/
            holder.classifyTextView.setText("HOT");
        }
        else if (!dataForTempItems.get(position).getIsHot() && dataForTempItems.get(position).getIsNew()){

            holder.classifyCardView.setVisibility(View.VISIBLE);
//                ((VideosHolder) holder).classifyTextView.setVisibility(View.VISIBLE);

            holder.classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.skyBlue));
                /*drawable.setColor(mContext.getResources().getColor(R.color.skyBlue));
                ((VideosHolder) holder).classifyCardView.setBackground(drawable);*/
            holder.classifyTextView.setText("NEW");
        }
            /*else if (!dataForTempItems.get(i).getIsHot() && !dataForTempItems.get(i).getIsNew()){
                ((VideosHolder) holder).classifyCardView.setVisibility(View.GONE);
                ((VideosHolder) holder).classifyTextView.setVisibility(View.GONE);
            }*/

        holder.hSVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayer.class);
                intent.putExtra("dataForVideoUrls", dataForTempItems.get(position).getVideoUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataForTempItems.size();
    }

    static class CategoryVideosViewHolder extends RecyclerView.ViewHolder{

        ImageView hSVideoImageView;
        TextView hSVideoTextView;

        CardView classifyCardView;
        TextView classifyTextView;

        public CategoryVideosViewHolder(View itemView) {
            super(itemView);

            hSVideoImageView = itemView.findViewById(R.id.imageViewDiwali);
            hSVideoTextView = itemView.findViewById(R.id.textViewDiwali);

            classifyCardView = itemView.findViewById(R.id.cVClassify);
            classifyTextView = itemView.findViewById(R.id.tVClassify);
        }
    }
}
