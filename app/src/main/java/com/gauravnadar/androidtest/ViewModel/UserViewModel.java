package com.gauravnadar.androidtest.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gauravnadar.androidtest.Model.User;
import com.gauravnadar.androidtest.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> allUsers;
    List<User> data;

    public UserViewModel(@NonNull Application application) {
        super(application);

        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
        data = new ArrayList<>();
    }

    public void insert(User user)
    {
        repository.insert(user);
    }

    public void update(User user)
    {
        repository.update(user);
    }


  public Boolean login(String email, String password){
      //List<User> data = new ArrayList<>();
              data = repository.login(email, password);
              //data.add(new User("njn", "nknkn", "bjbj", "vhv", 234));
      try
      {
          if(!data.isEmpty()) {
              return true;
          }
          else
          {
              return false;
          }
     }

      catch (Exception e){
          return false;
      }
  }

    public LiveData<List<User>> getAllUsers()
    {
        return allUsers;
    }
}
