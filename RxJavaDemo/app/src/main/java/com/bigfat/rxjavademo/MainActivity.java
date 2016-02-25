package com.bigfat.rxjavademo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.bigfat.rxjavademo.retrofit.GitHubService;
import com.bigfat.rxjavademo.retrofit.MingdaoService;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<PostData> mPostData = new ArrayList<>();
    private CommonRcvAdapter<PostData> commonRcvAdapter = new CommonRcvAdapter<PostData>(mPostData) {
        @NonNull
        @Override
        public AdapterItem createItem(Object o) {
            return new PostDataItem();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        //initData2();
        //test1();
        //test2();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(commonRcvAdapter);
    }

    private void initData() {
        //Observable.defer(() -> Observable.just(new GsonBuilder().create().fromJson(C.DATA, PostDataEntity.class)))
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
        MingdaoService service = retrofit.create(MingdaoService.class);
        service.getPostData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            //.flatMap(postDataEntity -> Observable.from(postDataEntity.getPosts()))
            //.filter(postData -> postData.getUser().getName().startsWith("任"))
            //.collect(() -> {
            //    PostDataEntity postDataEntity = new PostDataEntity();
            //    postDataEntity.setPosts(new ArrayList<>());
            //    return postDataEntity;
            //}, (postDataEntity, postData) -> postDataEntity.getPosts().add(postData))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(postDataEntity -> {
                Log.d("MainActivity", Thread.currentThread().getName());
                mPostData.addAll(postDataEntity.getPosts());
                commonRcvAdapter.notifyDataSetChanged();
            });
    }

    private void initData2() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
        GitHubService service = retrofit.create(GitHubService.class);
        //Call<List<Repo>> repos = service.listRepos("yueban");
        //repos.enqueue(new Callback<List<Repo>>() {
        //    @Override
        //    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        //        Log.d("ManiActivity", response.body().toString());
        //        Log.d("ManiActivity", response.toString());
        //    }
        //
        //    @Override
        //    public void onFailure(Call<List<Repo>> call, Throwable t) {
        //        Log.d("ManiActivity", t.toString());
        //    }
        //});

        service.listRepos("yueban")
            .subscribeOn(Schedulers.io())
            .flatMap(Observable::from)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(repo -> {
                Log.d("ManiActivity", repo.toString() + "\n");
            });
    }

    private void test1() {
        final int drawableRes = R.mipmap.ic_launcher;
        final ImageView imageView = (ImageView) findViewById(R.id.iv);
        Observable.OnSubscribe<Drawable> onSubscribe = subscriber -> {
            Drawable drawable = getResources().getDrawable(drawableRes);
            subscriber.onNext(drawable);
            subscriber.onCompleted();
        };
        Observable.create(onSubscribe)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Drawable>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(MainActivity.this, "error!error!error!error!error!error!error!error!error!error!",
                        Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(Drawable drawable) {
                    imageView.setImageDrawable(drawable);
                }
            });
    }

    private void test2() {
        Observable.just(1, 2, 3, 4)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map(integer -> integer + "")
            .observeOn(Schedulers.io());
    }
}
