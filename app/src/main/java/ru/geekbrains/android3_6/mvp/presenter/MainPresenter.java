package ru.geekbrains.android3_6.mvp.presenter;

import android.annotation.SuppressLint;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;
import ru.geekbrains.android3_6.mvp.model.entity.Repository;
import ru.geekbrains.android3_6.mvp.model.entity.User;
import ru.geekbrains.android3_6.mvp.model.repo.UsersRepo;
import ru.geekbrains.android3_6.mvp.presenter.list.IRepoListPresenter;
import ru.geekbrains.android3_6.mvp.view.MainView;
import ru.geekbrains.android3_6.mvp.view.item.RepoItemView;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

import javax.inject.Inject;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    @Inject Router router;

    public void onBackPressed() {
        router.exit();
    }

    public class RepoListPresenter implements IRepoListPresenter {
        PublishSubject<RepoItemView> clickSubject = PublishSubject.create();

        @Override
        public PublishSubject<RepoItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(RepoItemView view) {
            Repository repository = user.getRepos().get(view.getPos());
            view.setTitle(repository.getName());
        }

        @Override
        public int getRepoCount() {
            return user == null || user.getRepos() == null ? 0 : user.getRepos().size();
        }
    }

    @Inject UsersRepo userRepo;

    private Scheduler mainThreadScheduler;
    public RepoListPresenter repoListPresenter = new RepoListPresenter();
    private User user;

    public MainPresenter(Scheduler scheduler) {
        this.mainThreadScheduler = scheduler;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadInfo("googlesamples");
    }

    @SuppressLint("CheckResult")
    public void loadInfo(String username) {
        getViewState().showLoading();
        userRepo.getUser(username)
                .observeOn(mainThreadScheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    userRepo.getUserRepos(user)
                            .observeOn(mainThreadScheduler)
                            .subscribe(repositories -> {
                                this.user.setRepos(repositories);
                                getViewState().hideLoading();
                                getViewState().updateRepoList();
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user repos");
                                getViewState().showError(throwable.getMessage());
                            });
                }, throwable -> {
                    Timber.e(throwable, "Failed to get user");
                    getViewState().hideLoading();
                    getViewState().showError(throwable.getMessage());
                });
    }
}
