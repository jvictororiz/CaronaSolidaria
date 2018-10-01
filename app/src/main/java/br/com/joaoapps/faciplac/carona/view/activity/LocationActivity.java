package br.com.joaoapps.faciplac.carona.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by joaov on 16/12/2017.
 */

public class LocationActivity extends SuperActivity {
    private static final int PERMISSION_REQUEST_RESULT_CODE = 2439;
    private static final int SETTINGS_PERMISSION_REQUEST_CODE = 3542;
    private static String TAG = "GpsActivity";
    private GoogleApiClient googleApiClient;
    private OnLocationListener onLocationListener;
    private LatLng lastLocation;

    public void gpsInit(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
        getLocation();
//        getLastBestLocation();
    }

    public LatLng getLastLocation() {
        return lastLocation;
    }

    public boolean gpsIsEnabled(Activity context) {
        LocationManager service = (LocationManager) getSystemService(context.LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void goConfigGpsOff(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    private void getLastBestLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showDialogPermission();
            return;
        } else {
            if (!gpsIsEnabled(this) || googleApiClient == null) {
                showMessageTurnOnGpsIfDesabled();
                return;
            }
            Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if (0 < GPSLocationTime - NetLocationTime) {
                onLocationListener.onLocationChanged(locationGPS);
            } else {
                onLocationListener.onLocationChanged(locationNet);
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showDialogPermission();
            return;
        } else if (!gpsIsEnabled(this) || googleApiClient == null) {
            showMessageTurnOnGpsIfDesabled();
            return;
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500000);
        mLocationRequest.setFastestInterval(500000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                onLocationListener.onLocationChanged(location);
            }
        });

    }

    protected void showDialogPermission() {
        ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_RESULT_CODE);
    }


    public void showMessageTurnOnGpsIfDesabled() {
        initGoogleApiClient();

    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (!gpsIsEnabled(LocationActivity.this)) {
                            configPopupGps();
                        } else {
                            getLocation();
//                            getLastBestLocation();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        onLocationListener.onErrorConneciton();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        onLocationListener.onErrorConneciton();
                    }
                })
                .build();
        googleApiClient.connect();
    }


    private void configPopupGps() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLocation();
//                        getLastBestLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(LocationActivity.this, PERMISSION_REQUEST_RESULT_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            onLocationListener.onErrorConneciton();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        onLocationListener.onErrorConneciton();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PERMISSION_REQUEST_RESULT_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLocation();
//                        getLastBestLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        onLocationListener.gpsOnDenied();
                        break;
                }
                break;

            case SETTINGS_PERMISSION_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                } else {
                    getLocation();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            int grantResult = grantResults[i];
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                getLastBestLocation();
            } else {
                onLocationListener.permissonDenied();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

    }

    public void goToSettingsPermission() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, SETTINGS_PERMISSION_REQUEST_CODE);
    }


    public interface OnLocationListener {
        void onLocationChanged(Location location);

        void onErrorConneciton();

        void permissonDenied();

        void gpsOnDenied();
    }

}
