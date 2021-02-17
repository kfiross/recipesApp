package com.recipesapp.recipesapp.utils.data_binding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.net.URI;

public class PicassoBindingAdapter {
    @BindingAdapter(value = {"imageUrl", "placeholder", "error"}, requireAll = false)
    public static void setImageUrl(ImageView view, String url, @Nullable Drawable placeholderRes, @Nullable Drawable errorRes) {
        if(url==null || url.isEmpty()){
            url = "?";
        }

        RequestCreator requestCreator = Picasso.get().load(url);

        try{
            requestCreator.placeholder(placeholderRes);
        }
        catch (Exception ignored){

        }

        try{
            requestCreator.error(errorRes);
        }
        catch (Exception ignored){

        }

        try {
            requestCreator.centerCrop().fit().into(view);
        }
        catch (Exception ignored){
            Picasso.get().load(url).centerCrop().fit().into(view);
        }

    }

    @BindingAdapter(value = {"imageSrc", "placeholder", "error"}, requireAll = false)
    public static void setImageUri(ImageView view, Uri uri, @Nullable Drawable placeholderRes, @Nullable Drawable errorRes) {
        if(uri==null){
            return;
        }

        RequestCreator requestCreator = Picasso.get().load(uri);

        try{
            requestCreator.placeholder(placeholderRes);
        }
        catch (Exception ignored){

        }

        try{
            requestCreator.error(errorRes);
        }
        catch (Exception ignored){

        }

        try {
            requestCreator.centerCrop().fit().into(view);
        }
        catch (Exception ignored){
            Picasso.get().load(uri).centerCrop().fit().into(view);
        }

    }

}