package com.miroslavmirkovic.bookclub.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miroslavmirkovic.bookclub.MainActivity;
import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText emailView;
    private TextInputEditText passwordView;
    private ProgressBar progressBar;
    private APIInterface apiInterface;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailView = findViewById(R.id.email_logIn);
        passwordView = findViewById(R.id.password_logIn);
        progressBar = (ProgressBar) findViewById(R.id.login_pb);
        progressBar.setVisibility(View.INVISIBLE);

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public void logIn(View view) throws IOException {
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        Call<User> user = apiInterface.getUserByEmail(email);

        if (email.equals("") || email.isEmpty() || password.equals("") || password.isEmpty()) {
            Toast.makeText(getBaseContext(), "Morate popuniti sva polja", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            user.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User userResponse = response.body();
                    if (userResponse == null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Nepostojeći email", Toast.LENGTH_SHORT).show();
                    } else if (!(new String(Base64.decode(userResponse.getPassword(), Base64.DEFAULT))).equals(password)) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Neispravna lozinka", Toast.LENGTH_SHORT).show();
                    } else {
                        rememberUser(response);
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, SingUpActivity.class);
        startActivity(intent);
    }

    private void rememberUser(Response<User> response) {
        SharedPreferences sharedPref = this.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.id_user), response.body().getIdUser());
        editor.commit();
    }
}
