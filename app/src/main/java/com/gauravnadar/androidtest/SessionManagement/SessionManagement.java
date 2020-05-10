package com.gauravnadar.androidtest.SessionManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gauravnadar.androidtest.R;
import com.gauravnadar.androidtest.ScreenFragments.Registration;

import java.util.HashMap;

public class SessionManagement {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Pref";

    //Shared Preferences Key
    private static final String IS_LOGIN = "IsLoggedIn";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String id, String name, String email, String passsword, String phone, String pic){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", passsword);
        editor.putString("phone", phone);
        editor.putString("pic", pic);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public Boolean checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){

            return false;
        }
        return true;

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user id
        user.put("id", pref.getString("id", null));

        // user details
        user.put("name", pref.getString("name", null));
        user.put("email", pref.getString("email", null));
        user.put("password", pref.getString("password", null));
        user.put("phone", pref.getString("phone", null));
        user.put("pic", pref.getString("pic", null));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public Boolean logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        return true;
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
