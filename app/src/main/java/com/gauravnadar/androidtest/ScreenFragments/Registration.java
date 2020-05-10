package com.gauravnadar.androidtest.ScreenFragments;


import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gauravnadar.androidtest.Dao.UserDao;
import com.gauravnadar.androidtest.Database.UserDatabase;
import com.gauravnadar.androidtest.Model.User;
import com.gauravnadar.androidtest.R;
import com.gauravnadar.androidtest.ViewModel.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.gauravnadar.androidtest.Notification.App.CHANNEL_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class Registration extends Fragment {

    EditText name, email, password, phone;
    ImageView pic;
    Button register;
    final int REQUEST_CODE_GALLERY = 11;
    String pic_uri;
    private UserViewModel userViewModel;
    UserDao dao;
    UserDatabase database;
    NotificationCompat.Builder builder;

    NotificationManagerCompat notificationManager;
    Uri uri;

    public Registration() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        name = view.findViewById(R.id.r_name);
        pic = view.findViewById(R.id.r_pic);
        email = view.findViewById(R.id.r_email);
        password = view.findViewById(R.id.r_pass);
        phone = view.findViewById(R.id.r_phone);
        register = view.findViewById(R.id.btn_register);

        //instance of User database
        database = Room.databaseBuilder(getContext(),
                UserDatabase.class, "user_data.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //instance of User Dao
        dao = database.userDao();

        notificationManager = NotificationManagerCompat.from(getContext());


        //onclick listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Register();
            }

        });

        //onclick listener on image to pick profile ic from gallery
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

        return view;
    }


    //method to register user to local sqlite db
    private void Register() {

        String user_name,user_pic, user_email, user_password;
        String user_phone;

        //get all the text field values to store in databse
        user_name = name.getText().toString();
        user_email = email.getText().toString();
        user_password = password.getText().toString();
        user_phone = phone.getText().toString();


        try{
            uri.equals("");

            Bitmap bitmap = null;
        try {
            //get Bitmap of image profile pic from image uri
        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        pic_uri = saveToInternalStorage(bitmap, email.getText().toString());

        //text field validations
        if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_password) || TextUtils.isEmpty(user_phone) )
        {
            Toast.makeText(getContext(), "please enter all details", Toast.LENGTH_SHORT).show();
        }
        else {

            //created user object to be inserted into the database
            User user = new User(user_name, pic_uri, user_email, user_password, Integer.valueOf(user_phone));
            Log.e("user", String.valueOf(user.getPhone()));
            dao.insert(user);

            Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();

            //show notification
            notification();

        }

    } catch (IOException e) {
        e.printStackTrace();

    }
        }
        catch(Exception e)
            {
                Toast.makeText(getContext(), "please select an image", Toast.LENGTH_SHORT).show();
            }





    }

    //methd to show notification
    private void notification() {

        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Registration")
                .setContentText("You have successfully registered")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }

    //permission check and required persmission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);

            }
            else
            {
                Toast.makeText(getContext(), "No Permission", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //on picking up image from gallery overriden method
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
            //get uri of image chosen
            uri = data.getData();
            Log.e("uri", String.valueOf(uri));

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

                pic.setImageBitmap(bitmap);



        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    //method to save image to internal which takes in bitmap of image and name of user to be used as image name as params
    //returns image path
    private String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(getContext());

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,""+name+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}
