package com.miroslavmirkovic.bookclub.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.entities.BookEntity;

import java.util.ArrayList;

public class FavouriteBookAdapter extends ArrayAdapter<BookEntity> {

    TextView title, author, publisher;
    ImageView imageView;
    Activity context;
    ArrayList<BookEntity> bookEntities;

    public FavouriteBookAdapter(@NonNull Context context, ArrayList<BookEntity> bookEntities) {
        super(context, 0, bookEntities);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BookEntity bookEntity = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_favourite_book, parent, false);
        }

        title = (TextView) convertView.findViewById(R.id.text_title_book_favourite);
        author = (TextView) convertView.findViewById(R.id.text_author_book_favourite);
        publisher = (TextView) convertView.findViewById(R.id.text_publisher_book_favourite);
        imageView = convertView.findViewById(R.id.imageView3);

        title.setText(bookEntity.getTitle());
        author.setText(bookEntity.getAuthor());
        publisher.setText(bookEntity.getPublisher());
        if (bookEntity.getBookImage() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bookEntity.getBookImage(), 0, bookEntity.getBookImage().length));
        }

        return convertView;
    }
}
