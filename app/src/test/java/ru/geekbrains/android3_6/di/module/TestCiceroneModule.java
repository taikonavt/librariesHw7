package ru.geekbrains.android3_6.di.module;

import dagger.Module;
import dagger.Provides;
import org.mockito.Mockito;

import ru.terrakok.cicerone.Router;

@Module
public class TestCiceroneModule {

    @Provides
    public Router usersRepo(){
        return Mockito.mock(Router.class);
    }
}
