package ru.geekbrains.android3_6;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import ru.geekbrains.android3_6.di.DaggerTestComponent;
import ru.geekbrains.android3_6.di.TestComponent;
import ru.geekbrains.android3_6.di.modules.ApiModule;
import ru.geekbrains.android3_6.di.modules.CacheModule;
import ru.geekbrains.android3_6.mvp.model.cache.ICache;
import ru.geekbrains.android3_6.mvp.model.cache.RoomCache;
import ru.geekbrains.android3_6.mvp.model.entity.Repository;
import ru.geekbrains.android3_6.mvp.model.entity.User;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;

import static junit.framework.TestCase.assertEquals;

public class UserRepoInstrumentedTest {

    @Inject
    UsersRepo usersRepo;

    private static MockWebServer mockWebServer;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void setup(){
        TestComponent component = DaggerTestComponent
                .builder()
                .apiModule(new ApiModule(){
                    @Override
                    public String baseUrlProduction() {
                        return mockWebServer.url("/").toString();
                    }
                })
                .cacheModule(new CacheModule(){
                    @Override
                    public ICache roomCache() {
                        return Mockito.mock(RoomCache.class);
                    }
                })
                .build();

        //TODO: mock cache
        component.inject(this);
    }

    @Test
    public void getUser(){
        mockWebServer.enqueue(createUserResponse("someuser", "someavatar", "somerepos"));
        TestObserver<User> observer = new TestObserver<>();
        usersRepo.getUser("someuser").subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getLogin(),"someuser");
        assertEquals(observer.values().get(0).getAvatarUrl(),"someavatar");
        assertEquals(observer.values().get(0).getReposUrl(),"somerepos");
    }


    @Test
    public void getUserRepos(){
        String id = "123456";
        String name = "abcdef";

        mockWebServer.enqueue(createUserReposResponse(id, name));
        TestObserver<List<Repository>> observer = new TestObserver<>();
        usersRepo.getUserRepos(new User("someuser", "avatar_url", "repos_url")).subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).get(0).getId(), id);
        assertEquals(observer.values().get(0).get(0).getName(), name);
    }

    private MockResponse createUserResponse(String login, String avatarUrl, String reposUrl){
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl + "\", \"repos_url\":\"" + reposUrl + "\"}";
        return new MockResponse().setBody(body);
    }

    private MockResponse createUserReposResponse(String id, String name){
        String body = "[{\"id\": " + id + ", \"name\": \"" + name + "\"}]";
        return new MockResponse().setBody(body);
    }

}
