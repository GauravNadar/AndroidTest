package com.gauravnadar.androidtest.RetrofitHelper;

import com.gauravnadar.androidtest.Model.GitHubUser;
import com.gauravnadar.androidtest.ScreenFragments.GitHubUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    //github api endpoint takes in since param to fetch users accordingly
    @GET("users")
    Call<List<GitHubUser>> getUsers(
            @Query("since") String since

    );
}
