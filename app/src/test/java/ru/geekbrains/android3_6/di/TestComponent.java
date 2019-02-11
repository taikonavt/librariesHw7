package ru.geekbrains.android3_6.di;

import dagger.Component;
import ru.geekbrains.android3_6.di.module.TestCiceroneModule;
import ru.geekbrains.android3_6.di.module.TestRepoModule;
import ru.geekbrains.android3_6.mvp.presenter.MainPresenter;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        TestRepoModule.class,
        TestCiceroneModule.class
})
public interface TestComponent {
    void inject(MainPresenter presenter);
}
