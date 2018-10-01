package br.com.joaoapps.faciplac.carona.view.activity.maps;//package br.com.cabal.coopcerto.view.activities.maps;
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.graphics.Point;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.maps.android.clustering.ClusterManager;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Locale;
//
//import br.com.cabal.coopcerto.R;
//import br.com.cabal.coopcerto.model.Establishment;
//import br.com.cabal.coopcerto.view.activities.maps.mapConfiguration.CustomRender;
//import br.com.cabal.coopcerto.view.activities.maps.mapConfiguration.MarkerItem;
//import br.com.cabal.coopcerto.view.activities.util.AlertUtils;
//import br.com.cabal.coopcerto.view.componentes.dialogSelectorItemMap.DialogItemMapCopletedView;
//import br.com.cabal.coopcerto.view.componentes.dialogSelectorItemMap.DialogSelectorItemMapView;
//import br.com.cabal.coopcerto.view.superClasses.SuperActivity;
//import br.com.cabal.coopcerto.view.superClasses.SuperApplication;
//
//public class MapDetailedActivity extends SuperActivity implements OnMapReadyCallback, DialogItemMapCopletedView.OnEventesCaronaUsuarioDetailed {
//
//    public static final String EXTRA_ESTABLISHMENT = "EXTRA_ESTABLISHMENT";
//
//    private GoogleMap mMap;
//    private SupportMapFragment mapFragment;
//    private ClusterManager<MarkerItem> mClusterManager;
//
//    private Establishment establishment;
//    private Location location;
//    private DialogItemMapCopletedView establishmentItem;
//    private MarkerItem markerItem;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map_activity_detailed);
//        setupToolbar("");
//        establishment = (Establishment) getIntent().getSerializableExtra(EXTRA_ESTABLISHMENT);
//
//        establishmentItem = findViewById(R.id.map_item);
//        establishmentItem.refreshCaronaUsuarios(establishment, this);
//        showDetailedEstablishmentItem();
//
//        location = new Location("");
//        location.setLatitude(Double.valueOf(establishment.getLatitude()));
//        location.setLongitude(Double.valueOf(establishment.getLatitude()));
//
//
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    private void showDetailedEstablishmentItem() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                establishmentItem.show();
//            }
//        }, 1000);
//    }
//
//    private void setUpClusterer() {
//        if (mMap != null) {
//            //moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), true);
//            mClusterManager = new ClusterManager<>(this, mMap);
//            mClusterManager.setRenderer(new CustomRender(this, mMap, mClusterManager));
//            mMap.setOnCameraIdleListener(mClusterManager);
//            mMap.getUiSettings().setAllGesturesEnabled(false);
//            mMap.setOnMarkerClickListener(mClusterManager);
//
//
//            createMarkerLocation();
//        }
//    }
//
//    private List<Establishment> getEstablishments(Collection<MarkerItem> items) {
//        ArrayList<Establishment> establishments = new ArrayList<>();
//        for (MarkerItem markerItem : items) {
//            establishments.add(markerItem.getCaronaUsuario());
//        }
//        return establishments;
//    }
//
//    private void createMarkerLocation() {
//        double lat = Double.valueOf(establishment.getLatitude());
//        double lng = Double.valueOf(establishment.getLongitude());
//        CharSequence charSequence = establishment.getName().subSequence(0, 1);
//        MarkerItem markItem = new MarkerItem(this, new LatLng(lat, lng), 1, charSequence, establishment, true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            markItem.setTransitionName("PROFILE");
//        }
//        mClusterManager.addItem(markItem);
//
//        Point mappoint = mMap.getProjection().toScreenLocation(new LatLng(lat, lng));
//        mappoint.set(mappoint.x, mappoint.y + 200);
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(mMap.getProjection().fromScreenLocation(mappoint)));
//    }
//
//    private void moveCamera(LatLng latlng, boolean isMyLocation) {
//        //latlng = new LatLng(latlng.latitude, latlng.longitude + 40);
//        if (isMyLocation) {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.5f));
//        } else {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 25f));
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        if (location != null) {
//            setUpClusterer();
//        }
//    }
//
//    //_________________
//
//    @Override
//    public void onShare(Establishment establishment) {
//        Toast.makeText(this, establishment.getName(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onCall(String number) {
//        Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onClickComunication(Location location) {
//        String uri = "";
//        Intent intent;
//        try {
//
//            uri = String.format(SuperApplication.getLocale(),"geo:" + this.location.getLatitude() + "," +this.location.getLongitude() + "?q=" + location.getLatitude()+","+location.getLongitude()+" ("+location.getProvider()+")");
//            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } catch (ActivityNotFoundException ex) {
//            try {
//                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                startActivity(unrestrictedIntent);
//            } catch (ActivityNotFoundException innerEx) {
//                AlertUtils.showAlert("Não possui aplicação para traçar a rota", this);
//            }
//        }
//    }
//}
