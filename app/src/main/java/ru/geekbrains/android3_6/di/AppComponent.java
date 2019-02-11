package ru.geekbrains.android3_6.di;

import dagger.Component;
import ru.geekbrains.android3_6.di.modules.*;
import ru.geekbrains.android3_6.mvp.presenter.MainPresenter;
import ru.geekbrains.android3_6.ui.activity.MainActivity;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class,
        RepoModule.class,
        CiceroneModule.class
})
public interface AppComponent {
    void inject(MainPresenter mainPresenter);
    void inject(MainActivity mainActivity);
}
