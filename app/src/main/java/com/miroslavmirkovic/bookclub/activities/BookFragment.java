package com.miroslavmirkovic.bookclub.activities;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miroslavmirkovic.bookclub.MainActivity;
import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.adapters.CommentAdapter;
import com.miroslavmirkovic.bookclub.dto.BookDTO;
import com.miroslavmirkovic.bookclub.dto.CommentDTO;
import com.miroslavmirkovic.bookclub.entities.BookEntity;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Book;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Comment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class BookFragment extends Fragment {

    private View view;
    private TextView book_title, book_author, book_publisher, book_description, book_comment, book_categories;
    private ImageView book_thumbnail;
    private ListView listView;
    private Button button_addComment;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentDTO> commentsList;
    private APIInterface apiInterface;
    private int idBook;
    private BookDTO bookDTO;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        book_title = view.findViewById(R.id.booktitle);
        book_thumbnail = view.findViewById(R.id.bookthumbnail);
        book_author = view.findViewById(R.id.bookauthor);
        book_publisher = view.findViewById(R.id.bookpublisher);
        book_description = view.findViewById(R.id.bookdescription);
        book_comment = view.findViewById(R.id.book_comment);
        book_categories = view.findViewById(R.id.bookcategories);
        button_addComment = view.findViewById(R.id.button_addComment);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        String title = getArguments().getString("title");
        byte[] thumbnail = getArguments().getByteArray("thumbnail");
        String author = getArguments().getString("author");
        String publisher = getArguments().getString("publisher");
        String description = getArguments().getString("description");
        idBook = getArguments().getInt("idBook");

        book_title.setText(title);
        book_author.setText(author);
        book_publisher.setText(publisher);
        book_description.setText(description);
        if (thumbnail != null)
            book_thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length));

        showBookCategories();
        showAllComments();
        book_comment.setVisibility(View.INVISIBLE);
        button_addComment.setVisibility(View.INVISIBLE);
        if (getUserId() != 0) {
            button_addComment.setVisibility(View.VISIBLE);
            book_comment.setVisibility(View.VISIBLE);
            addComment();
        }
        getBook();

        return view;
    }

    private void showAllComments() {
        commentsList = new ArrayList<>();
        Call<List<Comment>> call = apiInterface.getAllComments(idBook);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                final List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setContent(comment.getContent());
                    commentDTO.setDateTime(comment.getDateTime());
                    commentDTO.setIdBook(comment.getBook().getIdBook());
                    commentDTO.setIdComment(comment.getIdComment());
                    commentDTO.setIdUser(comment.getUser().getIdUser());

                    commentsList.add(commentDTO);
                }

                commentAdapter = new CommentAdapter(getContext(), commentsList);
                listView = (ListView) view.findViewById(R.id.listview);
                listView.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d("Failure", Log.getStackTraceString(t));
                call.cancel();
            }
        });

    }

    public void addComment() {
        button_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bookcomment = book_comment.getText().toString();

                Comment comment = new Comment();
                comment.setContent(bookcomment);

                Call<Comment> call = apiInterface.addComment(comment, getUserId(), idBook);

                if (bookcomment.equals("") || bookcomment.isEmpty()) {
                    Toast.makeText(getContext(), "Morate uneti sadrzaj komentara", Toast.LENGTH_SHORT).show();
                    return;
                }

                call.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        Toast.makeText(getContext(), "Uspesan unos komentara", Toast.LENGTH_SHORT).show();
                        book_comment.setText("");
                        showAllComments();
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_favourite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_favourite_book:
                if (getUserId() != 0) {
                    if (addBookInFavourites()) {
                        Toast.makeText(getContext(), "Uspesno dodana knjiga", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Knjiga vec postoji u omiljenim", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getContext(), "Morate biti ulogovani", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private boolean addBookInFavourites() {
        BookEntity b = MainActivity.appDatabase.getBookDAO().getBookByTitle(bookDTO.getTitle());
        if (b == null) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setTitle(bookDTO.getTitle());
            bookEntity.setAuthor(bookDTO.getAuthor());
            bookEntity.setPublisher(bookDTO.getPublisher());
            bookEntity.setDescription(bookDTO.getDescription());
            bookEntity.setBookImage(bookDTO.getImage());
            MainActivity.appDatabase.getBookDAO().addBook(bookEntity);
            return true;
        } else {
            return false;
        }
    }

    private void getBook() {
        bookDTO = new BookDTO();
        Call<Book> call = apiInterface.getBookByIdBook(idBook);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                bookDTO = new BookDTO(book.getIdBook(), book.getAuthor(), book.getBookImage(), book.getDescription(), book.getPublisher(), book.getTitle(), book.getUser().getIdUser());
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void showBookCategories() {
        Call<List<String>> call = apiInterface.getBookCategories(idBook);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                for (String category : response.body()) {
                    book_categories.append(category + " ");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int idUser = sharedPreferences.getInt(getString(R.string.id_user), 0);
        return idUser;
    }

}