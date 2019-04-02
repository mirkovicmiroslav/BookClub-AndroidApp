package com.miroslavmirkovic.bookclub.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Book;
import com.miroslavmirkovic.bookclub.retrofit.pojo.BookCategory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddBookFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    private static final int CAMERA_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private View view;
    private TextView text_title, text_author, text_publisher, text_description;
    private Uri imageUri;
    private ImageView imageView;
    private APIInterface apiInterface;
    private Button chooseGallery, takePhoto, addBook;
    private ArrayList<String> selectedItems;
    private ListView listView;
    private Book book;
    private ArrayList<Integer> listCategoriesId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_book, container, false);
        setRetainInstance(true);

        text_title = view.findViewById(R.id.text_name_book);
        text_author = view.findViewById(R.id.text_author);
        text_publisher = view.findViewById(R.id.text_publisher);
        text_description = view.findViewById(R.id.text_description);
        imageView = view.findViewById(R.id.image_book);
        chooseGallery = view.findViewById(R.id.button_choose_gallery);
        takePhoto = view.findViewById(R.id.button_take_photo);
        addBook = view.findViewById(R.id.save_book);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        listView = view.findViewById(R.id.checkbox_list);
        selectedItems = new ArrayList<>();
        listCategoriesId = new ArrayList<>();

        setCheckbox();
        chooseGallery();
        takePhoto();
        addBook();

        return view;
    }

    public void addBook() {
        book = new Book();
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = text_title.getText().toString();
                String author = text_author.getText().toString();
                String publisher = text_publisher.getText().toString();
                String description = text_description.getText().toString();

                if (title.equals("") || title.isEmpty() || author.equals("") || author.isEmpty() || publisher.equals("") || publisher.isEmpty() || description.equals("") || description.isEmpty()) {
                    Toast.makeText(getContext(), "Morate popuniti sva polja", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageView == null) {
                    Toast.makeText(getContext(), "Morate uneti sliku", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();

                book.setTitle(title);
                book.setAuthor(author);
                book.setPublisher(publisher);
                book.setDescription(description);
                book.setBookImage(imageInByte);

                Call<Book> call = apiInterface.addBook(book, getUserId());

                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        final Book bookResponse = response.body();
                        Toast.makeText(getContext(), "Uspesno dodana knjiga", Toast.LENGTH_SHORT).show();
                        text_title.setText("");
                        text_author.setText("");
                        text_publisher.setText("");
                        text_description.setText("");
                        imageView.setImageBitmap(null);

                        for (int i = 0; i < selectedItems.size(); i++) {
                            Call<Integer> integerCall = apiInterface.getIdCategoryByName(selectedItems.get(i));
                            integerCall.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    addBookCategory(bookResponse, response.body());
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
    }

    public void chooseGallery() {
        chooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    requestStoragePermission();
                }
            }
        });
    }

    public void takePhoto() {
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_REQUEST);
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(getContext())
                    .setTitle("Dozvola je potrebna")
                    .setMessage("Ova dozvola je potrebna da bi ste dodali sliku iz vase galerije")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("otkazi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Dozvola ODOBRENA", Toast.LENGTH_SHORT).show();
                pickImage();
            } else {
                Toast.makeText(getContext(), "Dozvola ODBIJENA", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(cameraImage);
        }
    }

    private void pickImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int idUser = sharedPreferences.getInt(getString(R.string.id_user), 0);
        return idUser;
    }

    private void setCheckbox() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] categories = {"Akcija", "Drama", "Romantika", "Krimi", "Triler", "Avantura", "Horor", "Fantastika"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_checkbox, R.id.checkbox, categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem);
                } else {
                    selectedItems.add(selectedItem);
                }
            }
        });
    }


    private void addBookCategory(Book b, Integer i) {

        Call<BookCategory> call = apiInterface.addBookCategory(b, i);

        call.enqueue(new Callback<BookCategory>() {
            @Override
            public void onResponse(Call<BookCategory> call, Response<BookCategory> response) {
                BookCategory bookCategory = response.body();

            }

            @Override
            public void onFailure(Call<BookCategory> call, Throwable t) {
                Log.d("stack", t.getStackTrace().toString());
                call.cancel();
            }
        });
    }

}
