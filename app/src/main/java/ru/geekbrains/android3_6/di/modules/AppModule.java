package ru.geekbrains.android3_6.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_6.App;

@Module
public class AppModule {

    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Provides
    public App app(){
       return app;
    }
}
