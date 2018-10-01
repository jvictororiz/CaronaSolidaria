package br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;

public class MarkerItem extends View implements ClusterItem  {
    private final LatLng mPosition;
    private int urlIcon;
    private CharSequence charSequence;
    private CaronaUsuario caronaUsuario;
    private boolean isBig;

    public MarkerItem(Context context, LatLng mPosition, int urlIcon, CharSequence charSequence) {
        super(context);
        this.mPosition = mPosition;
        this.urlIcon = urlIcon;
        this.charSequence = charSequence;
        isBig = false;
    }

    public MarkerItem(Context context, LatLng mPosition, int urlIcon, CharSequence charSequence, CaronaUsuario caronaUsuario, boolean isBig) {
        super(context);
        this.mPosition = mPosition;
        this.urlIcon = urlIcon;
        this.charSequence = charSequence;
        this.caronaUsuario = caronaUsuario;
        this.isBig = isBig;
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public int getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(int urlIcon) {
        this.urlIcon = urlIcon;
    }

    public CaronaUsuario getCaronaUsuario() {
        return caronaUsuario;
    }

    public void setCaronaUsuario(CaronaUsuario caronaUsuario) {
        this.caronaUsuario = caronaUsuario;
    }

    public boolean isBig() {
        return isBig;
    }

    public void setBig(boolean big) {
        isBig = big;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return charSequence.toString();
    }

    @Override
    public String getSnippet() {
        return charSequence.toString();
    }
}