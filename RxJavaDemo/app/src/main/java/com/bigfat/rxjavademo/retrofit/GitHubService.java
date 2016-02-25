package com.bigfat.rxjavademo.retrofit;

import java.util.List;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GitHubService {
    //@GET("users/{user}/repos")
    //Call<List<Repo>> listRepos(@Path("user") String user);

    @POST("users/{user}/repos")
    List<Repo> listRepos(@Query("user") String user);
}