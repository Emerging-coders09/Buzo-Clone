package com.devarshi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devarshi.buzoclone.R;

import java.util.ArrayList;

public class BookGenreAdapter extends RecyclerView.Adapter<BookGenreAdapter.BookGenreViewHolder> {

    ArrayList<String> dataForBookGenre;

    public BookGenreAdapter(ArrayList<String> dataForBookGenre) {
        this.dataForBookGenre = dataForBookGenre;
    }

    @NonNull
    @Override
    public BookGenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_book_genre_list,parent,false);
        return new BookGenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookGenreViewHolder holder, int position) {

        holder.genreNameTv.setText(dataForBookGenre.get(position));
    }

    @Override
    public int getItemCount() {
        return dataForBookGenre.size();
    }

    class BookGenreViewHolder extends RecyclerView.ViewHolder {

        TextView genreNameTv;

        public BookGenreViewHolder(@NonNull View itemView) {
            super(itemView);

            genreNameTv = itemView.findViewById(R.id.tVGenreName);
        }
    }
}
