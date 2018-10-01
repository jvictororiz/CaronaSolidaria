package br.com.joaoapps.faciplac.carona.view.utils.listeners;

import android.location.Location;

/**
 * Created by joaov on 22/11/2017.
 */

public interface OnLocationListener {
    void onLocationChanged(Location location);
    void onGpsDisabled();
}
