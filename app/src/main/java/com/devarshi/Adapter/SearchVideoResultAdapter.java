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
import com.devarshi.Retrofitclient.FilteredData;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.VideoPlayer;

import java.util.ArrayList;

public class SearchVideoResultAdapter extends RecyclerView.Adapter<SearchVideoResultAdapter.SearchVideoResultViewHolder> {

    Context mContext;
    ArrayList<FilteredData> dataForTempItems;
    String videoTitle;

    public SearchVideoResultAdapter(Context mContext, ArrayList<FilteredData> dataForTempItems, String videoTitle) {
        this.mContext = mContext;
        this.dataForTempItems = dataForTempItems;
        this.videoTitle = videoTitle;
    }

    @NonNull
    @Override
    public SearchVideoResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_screen_video_layout, parent, false);
        return new SearchVideoResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVideoResultViewHolder holder, int position) {


        Glide.with(mContext).load(dataForTempItems.get(position).getThumbUrl()).into(holder.hSVideoImageView);

        holder.hSVideoTextView.setText(dataForTempItems.get(position).getTitle());

        if (dataForTempItems.get(position).getIsHot() && dataForTempItems.get(position).getIsNew()) {

            holder.classifyCardView.setVisibility(View.VISIBLE);

            holder.classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.classifyCardViewColor));

            holder.classifyTextView.setText("HOT");
        } else if (dataForTempItems.get(position).getIsHot() && !dataForTempItems.get(position).getIsNew()) {

            holder.classifyCardView.setVisibility(View.VISIBLE);

            holder.classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.classifyCardViewColor));

            holder.classifyTextView.setText("HOT");
        } else if (!dataForTempItems.get(position).getIsHot() && dataForTempItems.get(position).getIsNew()) {

            holder.classifyCardView.setVisibility(View.VISIBLE);

            holder.classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.skyBlue));

            holder.classifyTextView.setText("NEW");
        }

        holder.hSVideoImageView.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, VideoPlayer.class);
            intent.putExtra("dataForVideoUrls",dataForTempItems.get(position).getVideoUrl());
            mContext.startActivity(intent);
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

    public class SearchVideoResultViewHolder extends RecyclerView.ViewHolder {

        ImageView hSVideoImageView;
        TextView hSVideoTextView;

        CardView classifyCardView;
        TextView classifyTextView;

        public SearchVideoResultViewHolder(@NonNull View itemView) {
            super(itemView);

            hSVideoImageView = itemView.findViewById(R.id.imageViewDiwali);
            hSVideoTextView = itemView.findViewById(R.id.textViewDiwali);

            classifyCardView = itemView.findViewById(R.id.cVClassify);
            classifyTextView = itemView.findViewById(R.id.tVClassify);
        }
    }
}
