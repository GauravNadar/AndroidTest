package com.gauravnadar.androidtest.ScreenFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gauravnadar.androidtest.Dao.UserDao;
import com.gauravnadar.androidtest.Database.UserDatabase;
import com.gauravnadar.androidtest.Model.User;
import com.gauravnadar.androidtest.R;
import com.gauravnadar.androidtest.RecyclerViewHelper.GitHubUsersRecyclerAdapter;
import com.gauravnadar.androidtest.RecyclerViewHelper.LocalUsersRecyclerAdapter;
import com.gauravnadar.androidtest.ViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalUsers extends Fragment {

    private UserViewModel userViewModel;
    UserDao dao;
    UserDatabase database;
    RecyclerView recyclerView;
    LocalUsersRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<User> list;

    public LocalUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflated layout to be used in fragment
        View view = inflater.inflate(R.layout.fragment_local_users, container, false);

        //casting all views in the layout
        recyclerView = view.findViewById(R.id.local_users_recycler);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        //instance of user database
        database = Room.databaseBuilder(getContext(),
                UserDatabase.class, "user_data.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //instance of user dao which has all methods queries
        dao = database.userDao();

        //fetch all local users
        getLocalUsers(getContext());


        return view;
    }

    //method to fetch all locak users and present them in a list format
    private void getLocalUsers(final Context context) {

        //display methods returns a list of live user objects which are in the db and is listening for any changes observed
        //changes may include addition of new users or deletion of existing user
        dao.display().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                Log.e("all users", users.toString());

                //setting up the Recycler view Adapter and passsing in the list of local users
                //recycler adapter and view holder class are implemented in a separate class organized in separate package
                list = users;
                adapter = new LocalUsersRecyclerAdapter(list, context);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        });

    }

}
