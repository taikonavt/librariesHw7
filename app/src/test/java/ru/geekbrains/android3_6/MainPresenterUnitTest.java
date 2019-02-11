package ru.geekbrains.android3_6;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.geekbrains.android3_6.di.DaggerTestComponent;
import ru.geekbrains.android3_6.di.TestComponent;
import ru.geekbrains.android3_6.di.module.TestRepoModule;
import ru.geekbrains.android3_6.mvp.model.entity.User;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;
import ru.geekbrains.android3_6.mvp.presenter.MainPresenter;
import ru.geekbrains.android3_6.mvp.view.MainView;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainPresenterUnitTest {

    private MainPresenter presenter;
    private TestScheduler testScheduler;

    @Mock
    MainView mainView;

    @BeforeClass
    public static void setupClass(){
        Timber.plant(new Timber.DebugTree());
        Timber.d("setup class");
    }

    @AfterClass
    public static void tearDownClass(){
        Timber.d("tear down class");
    }

    @Before
    public void setup(){
        Timber.d("setup");
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new MainPresenter(testScheduler));
    }

    @After
    public void tearDown(){
        Timber.d("tearDown");
    }

    @Test
    public void loadInfoSuccess(){
        Timber.d("loadInfoSuccess()");

        User user = new User("googlesamples", "avatar_url", "repos_url");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule(){
                    @Override
                    public UsersRepo usersRepo() {
                        UsersRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser(user.getLogin())).thenReturn(Single.just(user));
                        Mockito.when(repo.getUserRepos(user)).thenReturn(Single.just(new ArrayList<>()));
                        return repo;
                    }
                }).build();



        component.inject(presenter);
        presenter.attachView(mainView);
        Mockito.verify(presenter).loadInfo("googlesamples");
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).showAvatar(user.getAvatarUrl());
        Mockito.verify(mainView).setUsername(user.getLogin());
        Mockito.verify(mainView).hideLoading();
        Mockito.verify(mainView).updateRepoList();
    }


    @Test
    public void loadInfoFailure(){
        Timber.d("loadInfoFailure");

        User user = new User("googlesamples", "avatar_url", "repos_url");
        Throwable throwable = new RuntimeException("No such user in cache");

        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule(){

                    @Override
                    public UsersRepo usersRepo() {
                        UsersRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser(user.getLogin())).thenReturn(Single.create(emitter -> {
                            emitter.onError(throwable);
                        }));
                        Mockito.when(repo.getUserRepos(user)).thenReturn(Single.create(emitter -> {
                            emitter.onError(throwable);
                        }));
                        return repo;
                    }
        }).build();

        component.inject(presenter);
        presenter.attachView(mainView);
        Mockito.verify(presenter).loadInfo("googlesamples");
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).showLoading();
        Mockito.verify(mainView).hideLoading();
        Mockito.verify(mainView).showError(throwable.getMessage());
    }

}
