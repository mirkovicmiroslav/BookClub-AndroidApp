package com.miroslavmirkovic.bookclub.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.miroslavmirkovic.bookclub.MainActivity;
import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.adapters.FavouriteBookAdapter;
import com.miroslavmirkovic.bookclub.dto.BookDTO;
import com.miroslavmirkovic.bookclub.entities.BookEntity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    View view;
    private ListView listView;
    private FavouriteBookAdapter favouriteBookAdapter;
    private ArrayList<BookEntity> bookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        setRetainInstance(true);

        getBooks();

        return view;
    }

    private void getBooks() {
        bookList = new ArrayList<>();
        List<BookEntity> bookArray = MainActivity.appDatabase.getBookDAO().getAllBooks();
        for (BookEntity book : bookArray) {
            System.out.print(book.getAuthor());
            if (book != null) {
                bookList.add(book);
            }
        }
        listView = view.findViewById(R.id.list_favourite);
        favouriteBookAdapter = new FavouriteBookAdapter(getContext(), bookList);
        listView.setAdapter(favouriteBookAdapter);
    }

}
