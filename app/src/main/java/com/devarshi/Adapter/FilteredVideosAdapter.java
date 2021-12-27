package com.devarshi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Retrofitclient.Template;
import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class FilteredVideosAdapter extends RecyclerView.Adapter<FilteredVideosAdapter.FilteredVideosViewHolder> {

    ArrayList<Template> dataForFilteredVideos;

    public FilteredVideosAdapter(ArrayList<Template> dataForFilteredVideos) {
        this.dataForFilteredVideos = dataForFilteredVideos;
    }

    @NonNull
    @Override
    public FilteredVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_bar_filtered_list_item,parent,false);
        return new FilteredVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredVideosViewHolder holder, int position) {

        holder.textViewSt.setText(dataForFilteredVideos.get(position).getTitle());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataForFilteredVideos.size();
    }

    class FilteredVideosViewHolder extends RecyclerView.ViewHolder{

        TextView textViewSt;

        public FilteredVideosViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSt = itemView.findViewById(R.id.sTTextView);
        }
    }
}
