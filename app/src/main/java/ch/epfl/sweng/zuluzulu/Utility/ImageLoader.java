package ch.epfl.sweng.zuluzulu.Utility;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;

public interface ImageLoader {

    static void loadUriIntoImageView(ImageView container, String uri, Context context) {
        if (context == null || uri.length() <= 0) {
            Log.e("GLIDE", "Can't load an Uri in an ImageView with a null Context");
            return;
        }

        if (uri.contains("http")) {
            Headers auth = new LazyHeaders.Builder() // This can be cached in a field and reused later.
                    .addHeader("Cookie", "gdpr=accept")
                    .build();
            GlideUrl glideUrl = new GlideUrl(uri, auth);
            Glide.with(context)
                    .load(glideUrl)
                    .fitCenter()
                    .into(container);
        } else {
            Glide.with(context)
                    .load(uri)
                    .fitCenter()
                    .into(container);
        }
    }
}
