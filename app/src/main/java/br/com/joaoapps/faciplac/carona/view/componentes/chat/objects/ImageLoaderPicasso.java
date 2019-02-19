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
    private Bitmap bitmap;

    public ImageLoaderPicasso(Context context) {
        this.context = context;
    }

    public ImageLoaderPicasso(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
        if (imageView.getDrawable() == null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
            bitmap = null;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
