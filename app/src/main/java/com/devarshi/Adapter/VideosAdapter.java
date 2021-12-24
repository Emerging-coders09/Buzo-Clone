package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.Retrofitclient.Category;
import com.devarshi.Retrofitclient.Template;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.VideoPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    Intent intent;

    public static final int TYPE_CAT_RECYCLER_VIEW = 0;
    public static final int TYPE_VID_RECYCLER_VIEW = 1;

    Context mContext;

    /*ArrayList<String> dataForTitle;
    ArrayList<String> dataForVideoThumbnails;
    ArrayList<String> dataForVideoUrls;

    ArrayList<String> dataForCategoriesTitles;
    ArrayList<String> dataForCategoriesThumbnails;*/

//    ArrayList<Data> dataForTemplateItems;

    ArrayList<Category> dataForCatItems;
    ArrayList<Template> dataForTempItems;

    int lastPosition = -1;


    public VideosAdapter(ArrayList<Template> dataForTempItems, ArrayList<Category> dataForCatItems, Context mContext) {
        /*this.dataForTitle = dataForTitle;
        this.dataForVideoThumbnails = dataForVideoThumbnails;
        this.dataForVideoUrls = dataForVideoUrls;*/
        this.dataForTempItems = dataForTempItems;
        this.dataForCatItems = dataForCatItems;
        this.mContext = mContext;
        /*this.dataForCategoriesTitles = dataForCategoriesTitles;
        this.dataForCategoriesThumbnails = dataForCategoriesThumbnails;*/
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_CAT_RECYCLER_VIEW) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.categories_recycler_view_layout, parent, false);
            return new CategoriesHolder(view);
        } else if (viewType == TYPE_VID_RECYCLER_VIEW) {
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
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(((CategoriesHolder) holder).recyclerViewCat.getContext(), 1, GridLayoutManager.HORIZONTAL,false);

            if (holder.getAdapterPosition()>lastPosition){
                Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_left);
                ((CategoriesHolder)holder).recyclerViewCat.setAnimation(animation);
                lastPosition = holder.getAdapterPosition();
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((CategoriesHolder)holder).recyclerViewCat.getContext(),LinearLayoutManager.HORIZONTAL,false);
            ((CategoriesHolder)holder).recyclerViewCat.setLayoutManager(linearLayoutManager);

            /*GridLayoutManager gridLayoutManager = new GridLayoutManager(((CategoriesHolder) holder).recyclerViewCat.getContext(),2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == TYPE_CAT_RECYCLER_VIEW){
                        return 1;
                    }
                    else {
                        return 2;
                    }
                }
            });*/
            ((CategoriesHolder) holder).recyclerViewCat.setLayoutManager(linearLayoutManager);

            if (((CategoriesHolder) holder).recyclerViewCat.getAdapter() == null) { //only create the adapter the first time. the following times update the values
                CategoriesAdapter adapter = new CategoriesAdapter(dataForCatItems,mContext);
                ((CategoriesHolder) holder).recyclerViewCat.setAdapter(adapter);
            } else {
                ((CategoriesHolder) holder).recyclerViewCat.getAdapter().notifyDataSetChanged();
            }
        }
        else if (holder instanceof VideosHolder) {

            Glide.with(mContext).load(dataForTempItems.get(i).getThumbUrl()).into(((VideosHolder)holder).hSVideoImageView);

            ((VideosHolder)holder).hSVideoTextView.setText(dataForTempItems.get(i).getTitle());

//            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.tags_rounded_corners).mutate();

            if (dataForTempItems.get(i).getIsHot() && dataForTempItems.get(i).getIsNew()){

                ((VideosHolder) holder).classifyCardView.setVisibility(View.VISIBLE);
//                ((VideosHolder) holder).classifyTextView.setVisibility(View.VISIBLE);

                ((VideosHolder) holder).classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                /*drawable.setColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                ((VideosHolder) holder).classifyCardView.setBackground(drawable);*/
                ((VideosHolder) holder).classifyTextView.setText("HOT");
            }
            else if (dataForTempItems.get(i).getIsHot() && !dataForTempItems.get(i).getIsNew()){

                ((VideosHolder) holder).classifyCardView.setVisibility(View.VISIBLE);
//                ((VideosHolder) holder).classifyTextView.setVisibility(View.VISIBLE);

                ((VideosHolder) holder).classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                /*drawable.setColor(mContext.getResources().getColor(R.color.classifyCardViewColor));
                ((VideosHolder) holder).classifyCardView.setBackground(drawable);*/
                ((VideosHolder) holder).classifyTextView.setText("HOT");
            }
            else if (!dataForTempItems.get(i).getIsHot() && dataForTempItems.get(i).getIsNew()){

                ((VideosHolder) holder).classifyCardView.setVisibility(View.VISIBLE);
//                ((VideosHolder) holder).classifyTextView.setVisibility(View.VISIBLE);

                ((VideosHolder) holder).classifyCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.skyBlue));
                /*drawable.setColor(mContext.getResources().getColor(R.color.skyBlue));
                ((VideosHolder) holder).classifyCardView.setBackground(drawable);*/
                ((VideosHolder) holder).classifyTextView.setText("NEW");
            }
            /*else if (!dataForTempItems.get(i).getIsHot() && !dataForTempItems.get(i).getIsNew()){
                ((VideosHolder) holder).classifyCardView.setVisibility(View.GONE);
                ((VideosHolder) holder).classifyTextView.setVisibility(View.GONE);
            }*/

            ((VideosHolder)holder).hSVideoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoPlayer.class);
                    intent.putExtra("dataForVideoUrls", dataForTempItems.get(i).getVideoUrl());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_CAT_RECYCLER_VIEW : TYPE_VID_RECYCLER_VIEW;
    }

    @Override
    public int getItemCount() {
        return dataForTempItems.size();
    }

    public static class VideosHolder extends RecyclerView.ViewHolder {

        ImageView hSVideoImageView;
        TextView hSVideoTextView;

        CardView classifyCardView;
        TextView classifyTextView;


        public VideosHolder(@NonNull View itemView) {
            super(itemView);
            hSVideoImageView = itemView.findViewById(R.id.imageViewDiwali);
            hSVideoTextView = itemView.findViewById(R.id.textViewDiwali);

            classifyCardView = itemView.findViewById(R.id.cVClassify);
            classifyTextView = itemView.findViewById(R.id.tVClassify);
//            itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), videoPlayer.class);
            v.getContext().startActivity(intent);
        }*/
    }

    public static class CategoriesHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerViewCat;

        public CategoriesHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            recyclerViewCat = itemView.findViewById(R.id.recyclerViewCategories);
        }
    }
}


