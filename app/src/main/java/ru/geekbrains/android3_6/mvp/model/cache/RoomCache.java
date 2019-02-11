package ru.geekbrains.android3_6.mvp.model.cache;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_6.mvp.model.entity.User;
import ru.geekbrains.android3_6.mvp.model.entity.Repository;
import ru.geekbrains.android3_6.mvp.model.entity.room.RoomRepository;
import ru.geekbrains.android3_6.mvp.model.entity.room.RoomUser;
import ru.geekbrains.android3_6.mvp.model.entity.room.db.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class RoomCache implements ICache
{
    @Override
    public void putUser(User user) {
        RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                .findByLogin(user.getLogin());

        if (roomUser == null) {
            roomUser = new RoomUser();
            roomUser.setLogin(user.getLogin());
        }

        roomUser.setAvatarUrl(user.getAvatarUrl());
        roomUser.setReposUrl(user.getReposUrl());

        UserDatabase.getInstance().getUserDao()
                .insert(roomUser);
    }

    @Override
    public Single<User> getUser(String username) {
        return Single.create(emitter -> {
            RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                    .findByLogin(username);

            if (roomUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onSuccess(new User(roomUser.getLogin(), roomUser.getAvatarUrl(), roomUser.getReposUrl()));
            }
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public void putUserRepos(User user, List<Repository> repos) {
        RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                .findByLogin(user.getLogin());

        if (roomUser == null) {
            roomUser = new RoomUser();
            roomUser.setLogin(user.getLogin());
            roomUser.setAvatarUrl(user.getAvatarUrl());
            roomUser.setReposUrl(user.getReposUrl());
            UserDatabase.getInstance()
                    .getUserDao()
                    .insert(roomUser);
        }
        if (!repos.isEmpty()) {
            List<RoomRepository> roomRepositories = new ArrayList<>();
            for (Repository repository : repos) {
                RoomRepository roomRepository = new RoomRepository(repository.getId(), repository.getName(), user.getLogin());
                roomRepositories.add(roomRepository);
            }

            UserDatabase.getInstance()
                    .getRepositoryDao()
                    .insert(roomRepositories);
        }
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) {
        return Single.create(emitter -> {
            RoomUser roomUser = UserDatabase.getInstance()
                    .getUserDao()
                    .findByLogin(user.getLogin());

            if(roomUser == null){
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                List<RoomRepository> roomRepositories = UserDatabase.getInstance().getRepositoryDao()
                        .getAll();

                List<Repository> repos = new ArrayList<>();
                for (RoomRepository roomRepository: roomRepositories){
                    repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                }

                emitter.onSuccess(repos);
            }
        }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>)(Class)List.class);
    }
}
