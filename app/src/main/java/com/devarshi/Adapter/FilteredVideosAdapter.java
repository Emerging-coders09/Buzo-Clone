package com.devarshi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.Retrofitclient.Template;
import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.SearchVideoTitleResultActivity;

import java.util.ArrayList;

public class FilteredVideosAdapter extends RecyclerView.Adapter<FilteredVideosAdapter.FilteredVideosViewHolder> {

    ArrayList<String> dataForFilteredVideos;
    ArrayList<Template> dataForTempVideos;
    Context mContext;
    Activity activity;
//    ArrayList<FilteredData> backupList = new ArrayList<>();

    public FilteredVideosAdapter(ArrayList<String> dataForFilteredVideos,ArrayList<Template> dataForTempVideos,Context mContext,Activity activity) {
        this.dataForFilteredVideos = dataForFilteredVideos;
        this.dataForTempVideos = dataForTempVideos;
        this.mContext = mContext;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FilteredVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_bar_filtered_list_item, parent, false);
        return new FilteredVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredVideosViewHolder holder, int position) {

        holder.textViewSt.setText(dataForFilteredVideos.get(position));

        holder.textViewSt.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SearchVideoTitleResultActivity.class);
            intent.putExtra("title",dataForFilteredVideos.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
            activity.finish();
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataForFilteredVideos.size();
    }

    /*public void filterList(ArrayList<FilteredData> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        dataForFilteredVideos = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }*/

    /*@Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<FilteredData> dataForFilteredList = new ArrayList<>();

            if (constraint.toString().isEmpty())
                dataForFilteredList.addAll(dataForFilteredVideos);
            else
            {
                for(FilteredData filteredData : dataForFilteredVideos){
                    if (filteredData.getTitle().contains(constraint.toString().toLowerCase())){
                        dataForFilteredList.add(filteredData);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = dataForFilteredList;
            return filterResults;
        }

        //main UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            dataForFilteredVideos.clear();
            dataForFilteredVideos.addAll((ArrayList<FilteredData>)results.values);
            notifyDataSetChanged();
        }

    };*/

    class FilteredVideosViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSt;

        public FilteredVideosViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSt = itemView.findViewById(R.id.sTTextView);

        }
    }

    public void updateList(ArrayList<String> dataForFv) {
        dataForFilteredVideos.clear();
        dataForFilteredVideos.addAll(dataForFv);
        /*dataForFilteredVideos.clear();
        dataForFilteredVideos = dataForFv;*/
        notifyDataSetChanged();
    }
}
