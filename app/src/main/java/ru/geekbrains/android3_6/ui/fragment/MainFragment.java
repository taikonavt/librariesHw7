package ru.geekbrains.android3_6.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.geekbrains.android3_6.App;
import ru.geekbrains.android3_6.R;
import ru.geekbrains.android3_6.mvp.model.image.ImageLoader;
import ru.geekbrains.android3_6.mvp.model.image.android.ImageLoaderGlide;
import ru.geekbrains.android3_6.mvp.presenter.MainPresenter;
import ru.geekbrains.android3_6.mvp.view.MainView;
import ru.geekbrains.android3_6.ui.adapter.RepoRVAdapter;

import javax.inject.Inject;

public class MainFragment extends MvpAppCompatFragment implements MainView {

    public static MainFragment getInstance(String arg) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("arg", arg);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.iv_avatar)
    ImageView avatarImageView;
    @BindView(R.id.tv_error)
    TextView errorTextView;
    @BindView(R.id.tv_username)
    TextView usernameTextView;
    @BindView(R.id.pb_loading)
    ProgressBar loadingProgressBar;
    @BindView(R.id.rv_repos)
    RecyclerView reposRecyclerView;

    @InjectPresenter
    MainPresenter presenter;

    RepoRVAdapter adapter;
    @Inject ImageLoader<ImageView> imageLoader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);

        reposRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reposRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adapter = new RepoRVAdapter(presenter.repoListPresenter);
        reposRecyclerView.setAdapter(adapter);
        return view;
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {
        String arg = getArguments().getString("arg");

        MainPresenter presenter = new MainPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    public void onBackPressed(){
        presenter.onBackPressed();
    }

    @Override
    public void showAvatar(String avatarUrl) {
        imageLoader.loadInto(avatarUrl, avatarImageView);
    }

    @Override
    public void showError(String message) {
        errorTextView.setText(message);
    }

    @Override
    public void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateRepoList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUsername(String username) {
        usernameTextView.setText(username);
    }
}
