package ru.geekbrains.android3_6.mvp.model.repo;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_6.NetworkStatus;

import ru.geekbrains.android3_6.mvp.model.api.IDataSource;
import ru.geekbrains.android3_6.mvp.model.cache.ICache;
import ru.geekbrains.android3_6.mvp.model.entity.Repository;
import ru.geekbrains.android3_6.mvp.model.entity.User;

import java.util.List;

public class UsersRepo {
    private ICache cache;
    private IDataSource dataSource;

    public UsersRepo(ICache cache, IDataSource dataSource) {
        this.cache = cache;
        this.dataSource = dataSource;
    }

    public Single<User> getUser(String username) {
        if (NetworkStatus.isOnline()) {
            return dataSource.getUser(username).subscribeOn(Schedulers.io())
                    .map(user -> {
                        cache.putUser(user);
                        return user;
                    });
        } else {
            return cache.getUser(username);
        }
    }

    public Single<List<Repository>> getUserRepos(User user) {
        if (NetworkStatus.isOnline()) {
            return dataSource.getUserRepos(user.getLogin()).subscribeOn(Schedulers.io())
                    .map(repos -> {
                        cache.putUserRepos(user, repos);
                        return repos;
                    });
        } else {
            return cache.getUserRepos(user);
        }
    }
}
