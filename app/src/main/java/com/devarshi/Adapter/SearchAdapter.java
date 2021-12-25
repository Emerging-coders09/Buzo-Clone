package com.devarshi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devarshi.Retrofitclient.Category;
import com.devarshi.buzoclone.CategoryActivity;
import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context mContext;

    ArrayList<Category> dataForCatItems;
    FinishSearchOnClick finishSearchOnClick;

    public SearchAdapter(ArrayList<Category> dataForCatItems,FinishSearchOnClick finishSearchOnClick, Context mContext) {

        this.dataForCatItems = dataForCatItems;
        this.finishSearchOnClick = finishSearchOnClick;
        this.mContext = mContext;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_categories_row_item,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {

        holder.catNameTextView.setText(dataForCatItems.get(position).getName());

        Glide.with(mContext).load(dataForCatItems.get(position).getImageUrl()).into(holder.catIconsImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra("catid",dataForCatItems.get(position).getId());
                intent.putExtra("catname",dataForCatItems.get(position).getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                finishSearchOnClick.finishSearchActivity();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataForCatItems.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView catIconsImageView;
        TextView catNameTextView;

        public SearchViewHolder(View itemView) {
            super(itemView);

            catIconsImageView = itemView.findViewById(R.id.iVCatIcons);
            catNameTextView = itemView.findViewById(R.id.iVCatNames);
        }
    }

    public interface FinishSearchOnClick{
        void finishSearchActivity();
    }
}
