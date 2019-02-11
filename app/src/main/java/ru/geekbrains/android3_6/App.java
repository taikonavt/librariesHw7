package ru.geekbrains.android3_6;

import android.app.Application;
import io.paperdb.Paper;
import io.realm.Realm;
import ru.geekbrains.android3_6.di.AppComponent;
import ru.geekbrains.android3_6.di.DaggerAppComponent;
import ru.geekbrains.android3_6.di.modules.AppModule;
import ru.geekbrains.android3_6.mvp.model.entity.room.db.UserDatabase;
import timber.log.Timber;

public class App extends Application
{
    private static App instance;

    private AppComponent appComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        Paper.init(this);
        Realm.init(this);
        UserDatabase.create(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App getInstance()
    {
        return instance;
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
