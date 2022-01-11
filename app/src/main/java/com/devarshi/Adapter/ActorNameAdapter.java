package com.devarshi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class ActorNameAdapter extends RecyclerView.Adapter<ActorNameAdapter.ActorNameViewHolder> {

    ArrayList<String> dataForActorName;

    public ActorNameAdapter(ArrayList<String> dataForActorName) {
        this.dataForActorName = dataForActorName;
    }

    @NonNull
    @Override
    public ActorNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_actor_name_list,parent,false);
        return new ActorNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorNameViewHolder holder, int position) {

        holder.actorNameTv.setText(dataForActorName.get(position));
    }

    @Override
    public int getItemCount() {
        return dataForActorName.size();
    }

    class ActorNameViewHolder extends RecyclerView.ViewHolder {

        TextView actorNameTv;

        public ActorNameViewHolder(@NonNull View itemView) {
            super(itemView);

            actorNameTv = itemView.findViewById(R.id.tVGenreName);
        }
    }
}
