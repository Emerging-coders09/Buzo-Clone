package com.devarshi.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.buzoclone.R;

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.categoriesHolder> {

    String data[];

    public categoriesAdapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public categoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_categories,parent,false);
        return new categoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoriesAdapter.categoriesHolder holder, int position) {

        holder.textView.setText(data[position]);

    }

    @Override
    public int getItemCount() {
        return data.length;
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
