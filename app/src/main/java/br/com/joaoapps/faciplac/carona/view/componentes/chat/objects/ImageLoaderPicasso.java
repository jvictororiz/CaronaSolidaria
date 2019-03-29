package br.com.joaoapps.faciplac.carona.view.componentes.chat.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.joaov.faciplac.caronasolidaria.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;

public class ImageLoaderPicasso implements ImageLoader {
    private Context context;

    public ImageLoaderPicasso(Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.icon_user_default)
                .error(R.drawable.icon_user_default)
                .into(imageView);
    }
}
