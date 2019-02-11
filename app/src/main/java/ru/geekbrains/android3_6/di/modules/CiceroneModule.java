package ru.geekbrains.android3_6.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

import javax.inject.Singleton;

@Module
public class CiceroneModule {

    Cicerone<Router> cicerone = Cicerone.create();

    @Provides
    @Singleton
    public Cicerone<Router> cicerone() {
        return cicerone;
    }

    @Provides
    @Singleton
    public NavigatorHolder navigatorHolder() {
        return cicerone.getNavigatorHolder();
    }

    @Provides
    @Singleton
    public Router router() {
        return cicerone.getRouter();
    }

}
