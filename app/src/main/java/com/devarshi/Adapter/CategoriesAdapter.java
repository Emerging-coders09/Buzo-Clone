package com.devarshi.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.categoriesHolder> {

    ArrayList<String> dataForCatTitles;
    ArrayList<String> dataForCatImages;
    Context mContext;

    public CategoriesAdapter(ArrayList<String> dataForCatTitles,ArrayList<String> dataForCatImages, Context mContext) {
        this.dataForCatTitles = dataForCatTitles;
        this.dataForCatImages = dataForCatImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public categoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_for_categories,parent,false);
        return new categoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.categoriesHolder holder, int position) {

        Glide.with(mContext).load(dataForCatImages.get(position)).into(holder.imageView);

        holder.textView.setText(dataForCatTitles.get(position));

    }

    @Override
    public int getItemCount() {
        return dataForCatTitles.size();
    }

    public class categoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public categoriesHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.i("Categories","Clicked");
        }
    }
}
