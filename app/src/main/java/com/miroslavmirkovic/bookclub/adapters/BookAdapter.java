package com.miroslavmirkovic.bookclub.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miroslavmirkovic.bookclub.R;

import com.miroslavmirkovic.bookclub.activities.BookFragment;
import com.miroslavmirkovic.bookclub.dto.BookDTO;

import java.util.ArrayList;
import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context context;
    private List<BookDTO> bookDTOList;

    public BookAdapter(Context context, List<BookDTO> bookDTOList) {
        this.context = context;
        this.bookDTOList = bookDTOList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        final View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_book, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.titleBook.setText(bookDTOList.get(i).getTitle());

        if (bookDTOList.get(i).getImage() != null) {
            myViewHolder.imageBook.setImageBitmap(BitmapFactory.decodeByteArray(bookDTOList.get(i).getImage(), 0, bookDTOList.get(i).getImage().length));
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                BookFragment bookFragment = new BookFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idBook", bookDTOList.get(i).getIdBook());
                bundle.putString("title", bookDTOList.get(i).getTitle());
                bundle.putString("author", bookDTOList.get(i).getAuthor());
                bundle.putString("publisher", bookDTOList.get(i).getPublisher());
                bundle.putString("description", bookDTOList.get(i).getDescription());
                bundle.putByteArray("thumbnail", bookDTOList.get(i).getImage());

                bookFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, bookFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookDTOList.size();
    }

    public void updateList(List<BookDTO> newList) {
        bookDTOList = new ArrayList<>();
        bookDTOList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleBook;
        ImageView imageBook;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleBook = itemView.findViewById(R.id.book_title);
            imageBook = itemView.findViewById(R.id.book_img);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }

}
