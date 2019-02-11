package ru.geekbrains.android3_6.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_6.mvp.model.cache.ICache;
import ru.geekbrains.android3_6.mvp.model.cache.ImageCache;
import ru.geekbrains.android3_6.mvp.model.cache.RoomCache;

import javax.inject.Named;

@Module
public class CacheModule {

    @Named("room")
    @Provides
    public ICache roomCache(){
        return new RoomCache();
    }

    @Named("realm")
    @Provides
    public ICache cache(){
        return new RoomCache();
    }

    @Named("realm")
    @Provides
    public ImageCache imageCache(){
        return new ImageCache();
    }
}
