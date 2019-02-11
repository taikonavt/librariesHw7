package ru.geekbrains.android3_6.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_6.mvp.model.api.IDataSource;
import ru.geekbrains.android3_6.mvp.model.cache.ICache;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;

import javax.inject.Named;
import javax.inject.Singleton;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {

    @Singleton
    @Provides
    public UsersRepo usersRepo(@Named("room") ICache cache, IDataSource dataSource){
        return new UsersRepo(cache, dataSource);
    }

}
