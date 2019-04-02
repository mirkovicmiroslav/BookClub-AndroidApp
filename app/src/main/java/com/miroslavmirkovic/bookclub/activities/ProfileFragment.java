package com.miroslavmirkovic.bookclub.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.User;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final int PICK_IMAGE = 100;
    private static final int CAMERA_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;
    private View view;
    private TextInputEditText editUserName, editLastName, editEmail, editPassword, editCheckPassword;
    private ImageView userImage;
    private Uri imageUri;
    private Button chooseGalery, takePhoto, changeProfile, confirmChangeProfile, cancelChangeProfile;
    private APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        editUserName = view.findViewById(R.id.name_profile);
        editLastName = view.findViewById(R.id.lastName_profile);
        editEmail = view.findViewById(R.id.email_profile);
        editPassword = view.findViewById(R.id.password_profile);
        editCheckPassword = view.findViewById(R.id.passwordConfirm_profile);

        userImage = view.findViewById(R.id.image_profile);

        chooseGalery = view.findViewById(R.id.button_choose_gallery_profile);
        takePhoto = view.findViewById(R.id.button_take_photo_profile);
        changeProfile = view.findViewById(R.id.button_change_profile);
        confirmChangeProfile = view.findViewById(R.id.button_confirm_change);
        cancelChangeProfile = view.findViewById(R.id.cancel_change_profile);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        visibleInvisibleFields();
        showProfile();
        chooseGallery();
        takePhoto();
        changeProfile();

        return view;
    }

    private void showProfile() {
        Call<User> call = apiInterface.getUserById(getUserId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                editUserName.setText(user.getName());
                editLastName.setText(user.getLastName());
                editEmail.setText(user.getEmail());
                if (user.getUserImage() != null)
                    userImage.setImageBitmap(BitmapFactory.decodeByteArray(user.getUserImage(), 0, user.getUserImage().length));
                editCheckPassword.setText("");
                editPassword.setText("");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void changeProfile() {
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserName.setFocusableInTouchMode(true);
                editLastName.setFocusableInTouchMode(true);
                editEmail.setFocusableInTouchMode(true);
                editPassword.setVisibility(View.VISIBLE);
                editCheckPassword.setVisibility(View.VISIBLE);

                changeProfile.setVisibility(View.INVISIBLE);
                confirmChangeProfile.setVisibility(View.VISIBLE);
                cancelChangeProfile.setVisibility(View.VISIBLE);

                confirmChangeProfile();
                cancelChangeProfile();
            }
        });
    }

    private void confirmChangeProfile() {
        confirmChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
                showProfile();
            }
        });
    }

    private void cancelChangeProfile() {
        cancelChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile();
                visibleInvisibleFields();
            }
        });
    }

    private void updateUser() {
        String name = editUserName.getText().toString();
        String lastName = editLastName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String checkPassword = editCheckPassword.getText().toString();

        if (name.equals("") || name.isEmpty() || lastName.equals("") || lastName.isEmpty() || email.equals("") || email.isEmpty() || password.equals("") || password.isEmpty() || checkPassword.equals("") || checkPassword.isEmpty()) {
            Toast.makeText(getContext(), "Morate popuniti sva polja", Toast.LENGTH_SHORT).show();
        } else if (!checkPassword(password, checkPassword)) {
            Toast.makeText(getContext(), "Unete lozinke nisu jednake", Toast.LENGTH_SHORT).show();
        } else {
            byte[] passEncode = Base64.encode(password.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = ((BitmapDrawable) userImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageInByte = baos.toByteArray();

            User user = new User();
            user.setIdUser(getUserId());
            user.setName(name);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(new String(passEncode));
            user.setUserImage(imageInByte);
            Call<User> call = apiInterface.updateUser(user, getUserId());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(getContext(), "Uspesne promene profila!", Toast.LENGTH_SHORT).show();
                    visibleInvisibleFields();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    public void chooseGallery() {
        chooseGalery.setOnClickListener(new View.OnClickListener() {
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
            userImage.setImageURI(imageUri);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            userImage.setImageBitmap(cameraImage);
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

    private boolean checkPassword(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    private void visibleInvisibleFields() {
        cancelChangeProfile.setVisibility(View.INVISIBLE);
        confirmChangeProfile.setVisibility(View.INVISIBLE);
        changeProfile.setVisibility(View.VISIBLE);

        editUserName.setVisibility(View.VISIBLE);
        editUserName.setFocusable(false);
        editLastName.setVisibility(View.VISIBLE);
        editLastName.setFocusable(false);
        editEmail.setVisibility(View.VISIBLE);
        editEmail.setFocusable(false);
        editPassword.setVisibility(View.GONE);
        editCheckPassword.setVisibility(View.GONE);
    }

}
