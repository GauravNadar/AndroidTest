package com.gauravnadar.androidtest.ScreenFragments;


import android.app.Notification;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravnadar.androidtest.Dao.UserDao;
import com.gauravnadar.androidtest.Database.UserDatabase;
import com.gauravnadar.androidtest.Model.User;
import com.gauravnadar.androidtest.R;
import com.gauravnadar.androidtest.SessionManagement.SessionManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.gauravnadar.androidtest.Notification.App.CHANNEL_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {

    EditText name, email, password, phone;
    ImageView pic;
    TextView info;
    int Profile_id;
    Button update, logout;
    String pic_uri_in_string;
    UserDatabase database;
    private UserDao dao;
    public static final int REQUEST_CODE_GALLERY = 2;
    Uri updated_uri = null;
    String updated_pic_path = null;
    Uri existing_uri = null;
    SessionManagement session;
    NotificationManagerCompat notificationManager;

    //empty constructor required
    public UserProfile() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate layout
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //cast all the screen views
        name = view.findViewById(R.id.p_name);
        email = view.findViewById(R.id.p_email);
        password = view.findViewById(R.id.p_pass);
        pic = view.findViewById(R.id.p_pic);
        phone = view.findViewById(R.id.p_phone);
        update = view.findViewById(R.id.btn_update);
        logout = view.findViewById(R.id.btn_logout);
        info = view.findViewById(R.id.info);

        //instance of session management
        session = new SessionManagement(getContext());

        //database instance
        database = Room.databaseBuilder(getContext(),
                UserDatabase.class, "user_data.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //UserDao instance with all required methods
        dao = database.userDao();

        //notification manager instance
        notificationManager = NotificationManagerCompat.from(getContext());

        //receive bundle from login screen
        //user already not looged in
        Bundle bundle = this.getArguments();
        if(bundle != null){
            Log.e("name", bundle.getString("name"));
            Log.e("email", bundle.getString("email"));
            Log.e("pass", bundle.getString("password"));

            //displaying all the user details in profile screen
            name.setText(bundle.getString("name"));
            email.setText(bundle.getString("email"));
            password.setText(bundle.getString("password"));
            phone.setText(String.valueOf(bundle.getInt("phone")));
            existing_uri = Uri.parse(bundle.getString("imageUri"));

            //temporarily initializing updated_uri with existing_uri
            updated_uri = existing_uri;

            //profile pic uri stored as string from bundle received
            pic_uri_in_string = bundle.getString("imageUri");

            //method to load image from path/uri
            //method defined below
            loadImageFromStorage(pic_uri_in_string, bundle.getString("email"));

            //profile id is the user id, used to update a profile baeed on its id
            Profile_id = bundle.getInt("id");

            //click listener to pick image from gallery to update profile pick
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);

                }
            });


        }
        else{


            //retrieve user data from session if user is already logged in
            HashMap<String, String> user = session.getUserDetails();

                         name.setText(user.get("name"));
                         email.setText(user.get("email"));
                         password.setText(user.get("password"));
                         phone.setText(user.get("phone"));
                         Profile_id = Integer.valueOf(user.get("id"));
                         existing_uri = Uri.parse(user.get("pic"));
            updated_uri = existing_uri;
            pic_uri_in_string = String.valueOf(Uri.parse(user.get("pic")));
                         loadImageFromStorage(user.get("pic"), user.get("email"));

        }

        //clicl listener for logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //clear session
                session.logoutUser();

                //send logout notification
                sendNotification();

                //remove fragment fom fragment stack
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(new UserProfile());
                transaction.commit();
                manager.popBackStack();

                getActivity().onBackPressed();

            }
        });


        //click listener for update button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get values from text field and store in variables to be updated later
                String updated_name = name.getText().toString();
                String updated_email = email.getText().toString();
                String updated_pssword = password.getText().toString();
                String updated_phone = phone.getText().toString();

                //if new rofile pic is elected below code is executed
                if(!updated_uri.equals(existing_uri)){


                    //convert the image from the uri provided into bitmap
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), updated_uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //method to save bitmap form of image into internal storage and return the path
                    updated_pic_path = saveToInternalStorage(bitmap, updated_email);

                    //create User object to be udated
                    User updated_user = new User(updated_name, updated_pic_path, updated_email, updated_pssword, Integer.valueOf(updated_phone) );
                    updated_user.setId(Profile_id);

                    //call the UserDao update method
                    dao.update(updated_user);

                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                    info.setText("Logout and Login again to see your changes, if Password updated make sure to login with updated password");

                }
                else{

                }
                //condition when image is not updated

                User updated_user = new User(updated_name, pic_uri_in_string, updated_email, updated_pssword, Integer.valueOf(updated_phone) );
                updated_user.setId(Profile_id);

                dao.update(updated_user);

                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                info.setText("Logout and Login again to see your changes, if Password updated make sure to login with updated password");
            }
        });

        return view;
    }

    private void sendNotification() {

        //method for send notification when logged out
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Logout")
                .setContentText("You have successfully Logged Out")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }


    //method to load profile pic and display on image view on profile screen
    private void loadImageFromStorage(String path, String email)
    {

        try {
            File f=new File(path, ""+email+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            pic.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    //overriden method to get image uri of image picked from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
            updated_uri = data.getData();

            Log.e("uri", String.valueOf(updated_uri));

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), updated_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //show selected image on imageview
            pic.setImageURI(updated_uri);

        }


        super.onActivityResult(requestCode, resultCode, data);
    }



    //method to save image to internal store and return the image path
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
