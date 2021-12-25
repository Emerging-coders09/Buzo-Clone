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
import com.devarshi.Retrofitclient.Category;
import com.devarshi.buzoclone.CategoryActivity;
import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder> {

    ArrayList<Category> dataForCatItems;
    Context mContext;

    public CategoriesAdapter(ArrayList<Category> dataForCatItems, Context mContext) {

        this.dataForCatItems = dataForCatItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_for_categories,parent,false);
        return new CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoriesHolder holder, int position) {

        Glide.with(mContext).load(dataForCatItems.get(position).getImageUrl()).into(holder.imageViewCat);

        holder.textViewCat.setText(dataForCatItems.get(position).getName());

        holder.imageViewCat.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CategoryActivity.class);
            intent.putExtra("catid",dataForCatItems.get(position).getId());
//            Log.d(TAG, "onBindViewHolder: catid " + dataForCatItems.get(position).getId());
            intent.putExtra("catname",dataForCatItems.get(position).getName());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataForCatItems.size();
    }

    public static class CategoriesHolder extends RecyclerView.ViewHolder{

        ImageView imageViewCat;
        TextView textViewCat;

        public CategoriesHolder(@NonNull View itemView) {
            super(itemView);

            imageViewCat = (ImageView) itemView.findViewById(R.id.imageView);
            textViewCat = (TextView) itemView.findViewById(R.id.textView);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),CategoryActivity.class);
            intent.putExtra("catId",)
            v.getContext().startActivity(intent);
        }*/
    }
}
