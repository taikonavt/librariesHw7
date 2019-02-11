package ru.geekbrains.android3_6.mvp.model.image;

import android.support.annotation.Nullable;

public interface ImageLoader<T>
{
    void loadInto(@Nullable String url, T container);
}
