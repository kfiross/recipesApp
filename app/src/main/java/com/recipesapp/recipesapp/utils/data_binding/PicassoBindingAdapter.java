package com.recipesapp.recipesapp.utils.data_binding;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestCreator;

public class PicassoBindingAdapter {
//
//    @BindingAdapter(value = {"glideImageSrc", "glidePlaceholder"}, requireAll = false)
//    public static void setResource(ImageView view, Object resource, int placeholderRes) {
//
//        Context context = view.getContext();
//
//        RequestOptions options = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .skipMemoryCache(true)
//                .placeholder(placeholderRes)
//                .error(R.drawable.img_dish_placeholder);
//
//        GlideApp.with(context)
//                .setDefaultRequestOptions(options)
//                .load(resource)
//                .thumbnail(0.4f)
//                .into(view);
//    }

    @BindingAdapter(value = {"imageUrl", "placeholder", "error"}, requireAll = false)
    public static void setImageUrl(ImageView view, String url, @Nullable Integer placeholderRes, @Nullable Integer errorRes) {
        if(url==null || url.isEmpty()){
            return;
        }

        RequestCreator requestCreator = Picasso.with(view.getContext()).load(url);

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
            requestCreator.fit().into(view);
        }
        catch (Exception ignored){
            Picasso.with(view.getContext()).load(url).fit().into(view);
        }

    }
}