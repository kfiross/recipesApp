package com.foodiz.app.helper.data_binding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.foodiz.app.helper.GlideApp;


public class GlideBindingAdapter {

    @BindingAdapter(value = {"imageUrl", "placeholder", "error"}, requireAll = false)
    public static void setImageUrl(ImageView view, String url, @Nullable Drawable placeholderRes, @Nullable Drawable errorRes) {

        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(true)
                .placeholder(placeholderRes)
                .error(errorRes);

        GlideApp.with(context)
                .setDefaultRequestOptions(options)
                .load(url)
                .centerCrop()
                .into(view);
    }

    @BindingAdapter(value = {"imageSrc", "placeholder", "error"}, requireAll = false)
    public static void setImageUri(ImageView view, Uri uri, @Nullable Drawable placeholderRes, @Nullable Drawable errorRes) {
        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(true)
                .placeholder(placeholderRes)
                .error(errorRes);

        GlideApp.with(context)
                .setDefaultRequestOptions(options)
                .load(uri)
                .centerCrop()
                .into(view);
    }
}