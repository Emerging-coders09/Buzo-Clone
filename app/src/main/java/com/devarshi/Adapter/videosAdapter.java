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

import com.devarshi.buzoclone.R;
import com.devarshi.buzoclone.videoPlayer;

public class videosAdapter extends RecyclerView.Adapter<videosAdapter.videosHolder> {

    Intent intent;
    Context con;

    String data[];

    public videosAdapter(String[] data,Context con) {
        this.data = data;
        this.con = con;
    }

    @NonNull
    @Override
    public videosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_videos, parent, false);
        return new videosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videosAdapter.videosHolder holder, int i) {

        holder.diwaliTextView.setText(data[i]);

        holder.diwaliImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(con, videoPlayer.class);
                con.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class videosHolder extends RecyclerView.ViewHolder{

        ImageView diwaliImageView;
        TextView diwaliTextView;


        public videosHolder(@NonNull View itemView) {
            super(itemView);
            diwaliImageView = (ImageView) itemView.findViewById(R.id.imageViewDiwali);
            diwaliTextView = (TextView) itemView.findViewById(R.id.textViewDiwali);
//            itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), videoPlayer.class);
            v.getContext().startActivity(intent);
        }*/
    }
}
