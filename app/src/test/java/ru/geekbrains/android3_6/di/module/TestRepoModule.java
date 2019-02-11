package ru.geekbrains.android3_6.di.module;

import dagger.Module;
import dagger.Provides;
import org.mockito.Mockito;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;

@Module
public class TestRepoModule {

    @Provides
    public UsersRepo usersRepo(){
        return Mockito.mock(UsersRepo.class);
    }
}
