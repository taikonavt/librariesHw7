package ru.geekbrains.android3_6.mvp.model.cache;

import java.util.List;

import io.reactivex.Single;
import ru.geekbrains.android3_6.mvp.model.entity.User;
import ru.geekbrains.android3_6.mvp.model.entity.Repository;

public interface ICache
{
    void putUser(User user);
    Single<User> getUser(String username);

    void putUserRepos(User user, List<Repository> repositories);
    Single<List<Repository>> getUserRepos(User user);
}
