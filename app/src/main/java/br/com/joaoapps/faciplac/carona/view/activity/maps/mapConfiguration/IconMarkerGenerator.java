/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.squareup.picasso.Picasso;


public class IconMarkerGenerator {
    private final Context mContext;
    private int urlIcon;

    private ViewGroup v;
    private CircleImageView imgIcon;
    private TextView tvLetter;

    public IconMarkerGenerator(Context context) {
        mContext = context;
    }

    public Bitmap makeIconSmall(int urlIcon, boolean isBig) {
        if (imgIcon != null) {
            this.urlIcon = urlIcon;
            setImage(urlIcon);
        }
        createViewSmall(isBig);
        setImage(urlIcon);
        return createBitmapFromView();
    }

    public Bitmap makeIconBig(CharSequence text) {
        createViewBig();
        setText(text);
        return createBitmapFromView();
    }

    public Bitmap createBitmapFromView() {
        v.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return bitmap;
    }

    private void createViewSmall(boolean isBig) {
        if (!isBig) {
            v = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_marker_map_small, null);
        } else {
            v = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_marker_map_small_big, null);
        }
        imgIcon = v.findViewById(R.id.img_icon);

    }

    private void createViewBig() {
        v = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_marker_map, null);
        tvLetter = v.findViewById(R.id.tv_count);
    }

    private void setImage(int urlIcon) {
        Picasso.with(mContext)
                .load(urlIcon)
                .placeholder(R.drawable.icon_user_default)
                .error(R.drawable.icon_user_default)
                .into(imgIcon);
    }

    private void setText(CharSequence text) {
        tvLetter.setText(text);
    }


}
