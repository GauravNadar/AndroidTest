package com.gauravnadar.androidtest;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.gauravnadar.androidtest.Dao.UserDao;
import com.gauravnadar.androidtest.Database.UserDatabase;
import com.gauravnadar.androidtest.Model.User;
import com.gauravnadar.androidtest.ScreenFragments.GitHubUsers;
import com.gauravnadar.androidtest.ScreenFragments.LocalUsers;
import com.gauravnadar.androidtest.ScreenFragments.Registration;
import com.gauravnadar.androidtest.ScreenFragments.UserProfile;
import com.gauravnadar.androidtest.SessionManagement.SessionManagement;
import com.gauravnadar.androidtest.ViewModel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.gauravnadar.androidtest.Notification.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout container;
    EditText email, password;
    Button login, register;
    UserViewModel userViewModel;
    UserDao dao;
    UserDatabase database;
    List<User> list;
    User user;
    public static final int GALLERY = 5;
    NotificationManagerCompat notificationManager;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        container = findViewById(R.id.container);
        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_pass);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);

        list = new ArrayList<>();

        //getting a database instance with database name user_data.db
        //allowed to run on main thread
        database = Room.databaseBuilder(this,
                UserDatabase.class, "user_data.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //instance of UseaDao class which contains all database query methods
        dao = database.userDao();

        //instance of notification manager to show notification on login
        notificationManager = NotificationManagerCompat.from(this);

        //session/shared prefernce instance to store user details for session management
        session = new SessionManagement(getApplicationContext());

        //check if user is already logged In using sharedPreference
        if(session.checkLogin())
        {   //if already logged in show profile screen
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserProfile()).commit();
        }

        //hide keyboard popup on screen start
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //check and request for runtime permission to read external storage for picking up user profile photo
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY);

            }
            else{

                }
            }
        catch (Exception e)
                {
                    e.printStackTrace();
                }



        //click listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Registration()).addToBackStack(null).commit();

            }
        });

        //click litener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = email.getText().toString();
                String user_password = password.getText().toString();

            //check method from userdao is used for login
                //returns a list of User objects
                //if failed then list would be empty
           list = dao.check(user_email, user_password);
                if(!list.isEmpty())
                {

                    //retreiving and adding all user details to bundle to show the details on profile screen
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", list.get(0).getId());
                    bundle.putString("name", list.get(0).getName());
                    bundle.putString("email", list.get(0).getEmail());
                    bundle.putString("password", list.get(0).getPassword());
                    bundle.putString("imageUri", list.get(0).getPic());
                    bundle.putInt("phone", list.get(0).getPhone());

                    UserProfile userProfile = new UserProfile();
                    userProfile.setArguments(bundle);

                    //adding user data to sharedpreference, session management
                    session.createLoginSession(String.valueOf(list.get(0).getId()), list.get(0).getName(), list.get(0).getEmail(), list.get(0).getPassword(), String.valueOf(list.get(0).getPhone()), list.get(0).getPic());

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, userProfile).commit();



                    Toast.makeText(MainActivity.this, "LoggedIn successfully", Toast.LENGTH_SHORT).show();

                    //method call to display notification with user name
                    showNotification(list.get(0).getName());
                }
                else
                {
                    Toast.makeText(MainActivity.this, "failed please enter correcr email-id and passowrd", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void showNotification(String name) {

        //method to show notification
        //single channel id is used
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Hi "+name+"")
                .setContentText(""+name+" You have successfully LoggedIn")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {


        }  else if (id == R.id.nav_local_users) {


            getSupportFragmentManager().beginTransaction().replace(R.id.container, new LocalUsers()).addToBackStack(null).commit();

        } else if (id == R.id.nav_github_users) {

            getSupportFragmentManager().beginTransaction().replace(R.id.container, new GitHubUsers()).addToBackStack(null).commit();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
