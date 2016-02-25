package com.bigfat.rxjavademo.retrofit;

import com.bigfat.rxjavademo.PostDataEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface MingdaoService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("https://raw.githubusercontent.com/jbxkuang/myDemo/master/posts")
    Observable<PostDataEntity> getPostData();
}