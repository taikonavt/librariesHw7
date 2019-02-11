package ru.geekbrains.android3_6.di.modules;

import android.widget.ImageView;
import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_6.mvp.model.api.IDataSource;
import ru.geekbrains.android3_6.mvp.model.cache.ICache;
import ru.geekbrains.android3_6.mvp.model.cache.ImageCache;
import ru.geekbrains.android3_6.mvp.model.image.ImageLoader;
import ru.geekbrains.android3_6.mvp.model.image.android.ImageLoaderGlide;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class ImageLoaderModule {

    @Singleton
    @Provides
    public ImageLoader<ImageView> imageLoader(@Named("realm") ImageCache cache){
        return new ImageLoaderGlide(cache);
    }

}
