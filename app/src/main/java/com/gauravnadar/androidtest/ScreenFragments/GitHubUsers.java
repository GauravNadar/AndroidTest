package com.gauravnadar.androidtest.ScreenFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gauravnadar.androidtest.Model.GitHubUser;
import com.gauravnadar.androidtest.R;
import com.gauravnadar.androidtest.RecyclerViewHelper.GitHubUsersRecyclerAdapter;
import com.gauravnadar.androidtest.RetrofitHelper.GetDataService;
import com.gauravnadar.androidtest.RetrofitHelper.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GitHubUsers extends Fragment implements GitHubUsersRecyclerAdapter.OnItemListener {


    RecyclerView recyclerView;
    GitHubUsersRecyclerAdapter adapter;
    List<GitHubUser> list;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar bar;

    public GitHubUsers() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //layout inflating and casting all ui elements
        View view = inflater.inflate(R.layout.fragment_git_hub_users, container, false);

        recyclerView = view.findViewById(R.id.github_users_recyclerview);
        bar = view.findViewById(R.id.progress);
        bar.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        //get list of github users
        getGitHubUsers(getContext());
        return view;

    }

    //method to retrieve github users since 135 (hardcoded value 135)
    //can use pagination for more set of user list
    private void getGitHubUsers(final Context context) {

        bar.setVisibility(View.VISIBLE);

        //retrofit instance
        //retrofit library used to consume REST api (github user api)
        //the api retrives list of github users since 135
        //api call is done asynchronously
        //not on main thread
        GetDataService helper = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<GitHubUser>> call = helper.getUsers( "135");

        call.enqueue(new Callback<List<GitHubUser>>() {
            @Override
            public void onResponse(Call<List<GitHubUser>> call, Response<List<GitHubUser>> response) {

                //if response received
                //response is stored in a list
                //adapter and recycler view are set and recycler view is updated
                list = response.body();
                adapter = new GitHubUsersRecyclerAdapter(list, context);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                bar.setVisibility(View.GONE);

                Log.e("list", list.toString());
                Log.e("response", response.body().toString());
                Log.e("res value", response.body().get(0).getAvatar_url());
            }

            @Override
            public void onFailure(Call<List<GitHubUser>> call, Throwable t) {

                Toast.makeText(getContext(), "please make sure youu have proper internet connection or please try after some time", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {

    }
}
