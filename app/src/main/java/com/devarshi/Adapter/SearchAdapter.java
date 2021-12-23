package com.devarshi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context mContext;

    ArrayList<String> dataForCatNames;
    ArrayList<String> dataForCatImageUrls;

    public SearchAdapter(ArrayList<String> dataForCatNames, ArrayList<String> dataForCatImageUrls, Context mContext) {

        this.mContext = mContext;
        this.dataForCatNames = dataForCatNames;
        this.dataForCatImageUrls = dataForCatImageUrls;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_categories_row_item,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {

        holder.catNameTextView.setText(dataForCatNames.get(position));

        Glide.with(mContext).load(dataForCatImageUrls.get(position)).into(holder.catIconsImageView);

    }

    @Override
    public int getItemCount() {
        return dataForCatNames.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        ImageView catIconsImageView;
        TextView catNameTextView;

        public SearchViewHolder(View itemView) {
            super(itemView);

            catIconsImageView = itemView.findViewById(R.id.iVCatIcons);
            catNameTextView = itemView.findViewById(R.id.iVCatNames);
        }
    }
}
