package com.miroslavmirkovic.bookclub;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miroslavmirkovic.bookclub.activities.CategoryFragment;
import com.miroslavmirkovic.bookclub.activities.HomeFragment;
import com.miroslavmirkovic.bookclub.activities.LogInActivity;
import com.miroslavmirkovic.bookclub.activities.ProfileFragment;
import com.miroslavmirkovic.bookclub.activities.AddBookFragment;
import com.miroslavmirkovic.bookclub.activities.FavouriteFragment;
import com.miroslavmirkovic.bookclub.activities.SingUpActivity;
import com.miroslavmirkovic.bookclub.dto.BookDTO;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ImageView image;
    private TextView text_fullName, text_email;
    private ArrayList<BookDTO> books;
    private APIInterface apiInterface;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static AppDatabase appDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        books = new ArrayList<>();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);

        image = headView.findViewById(R.id.nav_image);
        text_fullName = headView.findViewById(R.id.nav_fullName);
        text_email = headView.findViewById(R.id.nav_email);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        if (getUserId() != 0)
            setNavHeader();

        hideItem();
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "userdb").allowMainThreadQueries()
                .build();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_add_book:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddBookFragment()).commit();
                break;
            case R.id.nav_categories:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_favourites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavouriteFragment()).commit();
                break;
            case R.id.nav_log_out:
                logOut();
                break;
            case R.id.nav_log_in:
                startActivity(new Intent(getBaseContext(), LogInActivity.class));
                break;
            case R.id.nav_sign_up:
                startActivity(new Intent(getBaseContext(), SingUpActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void hideItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if (getUserId() != 0) {
            nav_Menu.findItem(R.id.nav_log_in).setVisible(false);
            nav_Menu.findItem(R.id.nav_sign_up).setVisible(false);
        } else {
            nav_Menu.findItem(R.id.nav_log_out).setVisible(false);
            nav_Menu.findItem(R.id.nav_add_book).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_favourites).setVisible(false);
            text_fullName.setText("");
            text_email.setText("");
        }
    }

    private void logOut() {
        SharedPreferences sharedPref = this.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.id_user), 0);
        editor.commit();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int idUser = sharedPreferences.getInt(getString(R.string.id_user), 0);
        return idUser;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void setNavHeader() {
        Call<User> call = apiInterface.getUserById(getUserId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    if (user.getUserImage() != null) {
                        image.setImageBitmap(BitmapFactory.decodeByteArray(user.getUserImage(), 0, user.getUserImage().length));
                    }
                    text_fullName.setText(user.getName() + user.getLastName());
                    text_email.setText(user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }
        });
    }

}
