package ru.geekbrains.android3_6.mvp.model.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.android3_6.mvp.model.entity.Repository;
import ru.geekbrains.android3_6.mvp.model.entity.User;

import java.util.List;

public interface IDataSource
{
    @GET("users/{user}")
    Single<User> getUser(@Path("user") String userName);

    @GET("users/{user}/repos")
    Single<List<Repository>> getUserRepos(@Path("user") String userName);

}
