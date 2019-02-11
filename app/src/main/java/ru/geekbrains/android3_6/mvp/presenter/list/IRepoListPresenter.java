package ru.geekbrains.android3_6.mvp.presenter.list;

import io.reactivex.subjects.PublishSubject;
import ru.geekbrains.android3_6.mvp.view.item.RepoItemView;

public interface IRepoListPresenter
{
    PublishSubject<RepoItemView> getClickSubject();
    void bindView(RepoItemView rowView);
    int getRepoCount();
}
