package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.videoPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    Intent intent;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_POST = 1;

    Context mContext;

    ArrayList<String> dataForTitle;
    ArrayList<String> dataForVideoThumbnails;
    ArrayList<String> dataForVideoUrls;

    ArrayList<String> listData;

    public VideosAdapter(ArrayList<String> dataForTitle, ArrayList<String> dataForVideoThumbnails, ArrayList<String> dataForVideoUrls, ArrayList<String> listData, Context mContext) {
        this.dataForTitle = dataForTitle;
        this.dataForVideoThumbnails = dataForVideoThumbnails;
        this.dataForVideoUrls = dataForVideoUrls;
        this.mContext = mContext;
        this.listData = listData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.categories_recycler_view_layout, parent, false);
            return new CategoriesHolder(view);
        } else if (viewType == TYPE_POST) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.home_screen_video_layout, parent, false);
            return new VideosHolder(view);
        }

        throw new RuntimeException("Don't know this type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof CategoriesHolder) {
            //set adapter for the horizontal recycler view
            GridLayoutManager gridLayoutManager = new GridLayoutManager(((CategoriesHolder) holder).recyclerViewCat.getContext(), 1, GridLayoutManager.HORIZONTAL,false);
            ((CategoriesHolder) holder).recyclerViewCat.setLayoutManager(gridLayoutManager);

            if (((CategoriesHolder) holder).recyclerViewCat.getAdapter() == null) { //only create the adapter the first time. the following times update the values
                CategoriesAdapter adapter = new CategoriesAdapter(listData);
                ((CategoriesHolder) holder).recyclerViewCat.setAdapter(adapter);
            } else {
                ((CategoriesHolder) holder).recyclerViewCat.getAdapter().notifyDataSetChanged();
            }
        }
        else if (holder instanceof VideosHolder) {
            Glide.with(mContext).load(dataForVideoThumbnails.get(i)).into(((VideosHolder)holder).diwaliImageView);

            ((VideosHolder)holder).diwaliImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, videoPlayer.class);
                    intent.putExtra("dataForVideoUrls", dataForVideoUrls.get(i));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_POST;
    }

    @Override
    public int getItemCount() {
        return dataForTitle.size();
    }

    public class VideosHolder extends RecyclerView.ViewHolder {

        ImageView diwaliImageView;
        TextView diwaliTextView;


        public VideosHolder(@NonNull View itemView) {
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

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerViewCat;

        public CategoriesHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            recyclerViewCat = itemView.findViewById(R.id.recyclerViewCategories);
        }
    }
}


