package com.gauravnadar.androidtest.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gauravnadar.androidtest.Model.User;

import java.util.List;

@Dao
public interface UserDao {

    //method for user registration
    @Insert
    void insert(User user);

    //method for user profile update
    @Update
    void update(User user);

    //display all users in the local users screen
    @Query("SELECT * FROM User")
    LiveData<List<User>> display();

    //methon for login to check email and password match
    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    public List<User> check(String email, String password);
}
