package com.gauravnadar.androidtest.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.gauravnadar.androidtest.Dao.UserDao;
import com.gauravnadar.androidtest.Database.UserDatabase;
import com.gauravnadar.androidtest.Model.User;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserRepository {


    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    List<User> exist;
    UserDatabase database;

    public UserRepository(Application application) {
        //UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.display();
        exist = new ArrayList<>();

    }


    public void insert(User user)
    {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(User user){
        new UpdateStudentAsyncTask(userDao).execute(user);
    }



    public List<User> login(String email, String password){

        new LoginUserAsyncTask(userDao).execute(email, password);

        return exist;

    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void>
    {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao)
        {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }


    private static class UpdateStudentAsyncTask extends AsyncTask<User, Void, Void>
    {
        private UserDao userDao;

        private UpdateStudentAsyncTask(UserDao userDao)
        {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    public class LoginUserAsyncTask extends AsyncTask<String, List<User>,List<User>>
    {
        private UserDao userDao;

        public LoginUserAsyncTask(UserDao userDao)
        {
            this.userDao = userDao;
        }

        @Override
        public List<User> doInBackground(String... users) {


                return userDao.check(users[0], users[1]);

        }

        @Override
        protected void onPostExecute(List<User> users) {

            exist = users;

        }
    }


}
