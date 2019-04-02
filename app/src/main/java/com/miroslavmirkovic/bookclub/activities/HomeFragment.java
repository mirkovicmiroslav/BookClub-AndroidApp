package com.miroslavmirkovic.bookclub.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.adapters.BookAdapter;
import com.miroslavmirkovic.bookclub.dto.BookDTO;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private View v;
    private RecyclerView recyclerView;
    private ArrayList<BookDTO> booksList;
    private BookAdapter bookAdapter;
    private Toolbar toolbar;
    private APIInterface apiInterface;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        booksList = new ArrayList<>();
        apiInterface = APIClient.getClient().create(APIInterface.class);

        getAllBooks();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Pretraga po nazivu knjige");
        searchView.setOnQueryTextListener(this);
    }

    private void getAllBooks() {
        Call<List<Book>> call = apiInterface.getAllBooks();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                final List<Book> books = response.body();
                for (Book book : books) {
                    BookDTO bookDTO = new BookDTO();
                    bookDTO.setIdBook(book.getIdBook());
                    bookDTO.setIdUser(book.getUser().getIdUser());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setImage(book.getBookImage());
                    bookDTO.setDescription(book.getDescription());
                    bookDTO.setPublisher(book.getPublisher());
                    bookDTO.setAuthor(book.getAuthor());

                    booksList.add(bookDTO);
                }

                recyclerView = v.findViewById(R.id.recyclerView);
                bookAdapter = new BookAdapter(getContext(), booksList);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                recyclerView.setAdapter(bookAdapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("Failure", Log.getStackTraceString(t));
                call.cancel();
            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userInput = s.toLowerCase();
        ArrayList<BookDTO> newList = new ArrayList<>();

        for (int i = 0; i < booksList.size(); i++) {
            if (booksList.get(i).getTitle().toLowerCase().contains(userInput)) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setIdBook(booksList.get(i).getIdBook());
                bookDTO.setIdUser(booksList.get(i).getIdUser());
                bookDTO.setTitle(booksList.get(i).getTitle());
                bookDTO.setImage(booksList.get(i).getImage());
                bookDTO.setDescription(booksList.get(i).getDescription());
                bookDTO.setPublisher(booksList.get(i).getPublisher());
                bookDTO.setAuthor(booksList.get(i).getAuthor());
                newList.add(bookDTO);
            }
        }
        bookAdapter.updateList(newList);

        return true;
    }
}
