package com.miroslavmirkovic.bookclub.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.miroslavmirkovic.bookclub.MainActivity;
import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.User;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingUpActivity extends AppCompatActivity {

    private TextInputEditText nameView;
    private TextInputEditText lastNameView;
    private TextInputEditText emailView;
    private TextInputEditText passwordView;
    private TextInputEditText passwordConfirmView;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        nameView = (TextInputEditText) findViewById(R.id.name_signUp);
        lastNameView = findViewById(R.id.lastName_signUp);
        emailView = findViewById(R.id.email_signUp);
        passwordView = findViewById(R.id.password_signUp);
        passwordConfirmView = findViewById(R.id.passwordConfirm_signUp);

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public void register(View view) throws UnsupportedEncodingException {
        final String name = nameView.getText().toString();
        final String lastName = lastNameView.getText().toString();
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();
        final String passwordConfirm = passwordConfirmView.getText().toString();

        byte[] passEncode = Base64.encode(password.getBytes(), Base64.DEFAULT);

        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(new String(passEncode));

        Call<User> call = apiInterface.register(user);

        if (name.equals("") || name.isEmpty() || lastName.equals("") ||  lastName.isEmpty() || email.equals("") || email.isEmpty() || password.equals("") || password.isEmpty() || passwordConfirm.equals("") || passwordConfirm.isEmpty()) {
            Toast.makeText(getBaseContext(), "Morate popuniti sva polja", Toast.LENGTH_SHORT).show();
        } else if (!checkPassword(password, passwordConfirm)) {
            Toast.makeText(getBaseContext(), "Unete lozinke nisu jednake", Toast.LENGTH_SHORT).show();
        } else {
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(getBaseContext(), "Uspesna registracija", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), LogInActivity.class));
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    public void logIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private boolean checkPassword(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}
