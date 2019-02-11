package ru.geekbrains.android3_6.mvp.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.geekbrains.android3_6.NetworkStatus;
import ru.geekbrains.android3_6.mvp.model.cache.ImageCache;
import ru.geekbrains.android3_6.mvp.model.image.ImageLoader;
import timber.log.Timber;

public class ImageLoaderGlide implements ImageLoader<ImageView> {

    private ImageCache cache;

    public ImageLoaderGlide(ImageCache cache) {
        this.cache = cache;
    }

    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (NetworkStatus.isOnline()) {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.e(e, "Image load failed");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    cache.saveImage(url, resource);
                    return false;
                }
            }).into(container);
        } else {
            if (cache.contains(url)) {
                GlideApp.with(container.getContext())
                        .load(cache.getFile(url))
                        .into(container);
            }
        }
    }

}
