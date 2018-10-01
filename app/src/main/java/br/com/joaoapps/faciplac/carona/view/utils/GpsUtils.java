package br.com.joaoapps.faciplac.carona.view.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.List;

import br.com.joaoapps.faciplac.carona.SuperApplication;
import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.view.utils.listeners.OnLocationListener;

/**
 * Created by joaov on 22/11/2017.
 */

public class GpsUtils {

    public static boolean gpsIsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public static void startActivityConfigGps(AppCompatActivity context) {
        context.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
    }

    public static void setLocationListener(Activity context, final OnLocationListener onLocationListener) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.getProvider(LocationManager.NETWORK_PROVIDER).requiresCell();
        locationManager.getProvider(LocationManager.NETWORK_PROVIDER).requiresNetwork();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                onLocationListener.onLocationChanged(location);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                onLocationListener.onGpsDisabled();


            }
        });
    }

    public static String getNameLocale(Context context, double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, SuperApplication.getLocale());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            return address;
        } catch (Exception e) {
            return "";
        }
    }

    public static void goGpsOnConfig(Context context) {
        if (!gpsIsEnabled(context)) {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    public static String distanceTo(Location locationOne, Location locationTwo) {
        float distance = locationOne.distanceTo(locationTwo);

        String distanceText = "";
        if (distance >= 1000) {
            distanceText = String.valueOf(getDecimal(distance / 1000)) + " KM";
        } else if (distance >= 0 && distance <= 10) {
            distanceText = "Menos de 10 metros";
        } else {
            distanceText = String.valueOf(getDecimal(distance)) + " M";
        }

        distanceText = distanceText.replace(".", ",");
        return distanceText;
    }

    private static float getDecimal(float distance) {
        int casasDecimais = 2;
        BigDecimal aNumber = new BigDecimal(distance);
        aNumber = aNumber.setScale(casasDecimais, BigDecimal.ROUND_HALF_UP);
        return aNumber.floatValue();
    }
}
