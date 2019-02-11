package ru.geekbrains.android3_6.di;

import dagger.Component;
import ru.geekbrains.android3_6.UserRepoInstrumentedTest;
import ru.geekbrains.android3_6.di.modules.RepoModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {RepoModule.class})
public interface TestComponent {
    void inject(UserRepoInstrumentedTest test);
}
