package ru.geekbrains.android3_6;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.geekbrains.android3_6.di.DaggerTestComponent;
import ru.geekbrains.android3_6.di.TestComponent;
import ru.geekbrains.android3_6.di.modules.ApiModule;
import ru.geekbrains.android3_6.mvp.model.entity.User;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;

import javax.inject.Inject;
import java.io.IOException;

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
                }).build();

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
        //TODO:
    }

    private MockResponse createUserResponse(String login, String avatarUrl, String reposUrl){
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl + "\", \"repos_url\":\"" + reposUrl + "\"}";
        return new MockResponse().setBody(body);

    }
}
