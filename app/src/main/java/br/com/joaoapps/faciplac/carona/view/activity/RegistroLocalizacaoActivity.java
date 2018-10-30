package br.com.joaoapps.faciplac.carona.view.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.joaov.caronasolidaria.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import br.com.joaoapps.faciplac.carona.model.LatLng;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.service.firebase.UsuarioFirebase;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;

public class RegistroLocalizacaoActivity extends LocationActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static final String EDITION = "EDITION";
    private Usuario usuario;
    private LatLng locationActual;
    private LatLng locationSelected;
    private LinearLayout viewMyLocation;
    private Button btnNex;
    public GoogleMap mMap;
    public boolean isEdition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_localizacao_activity);
        SuperActivity.closeDialogLoad();
        initViews();
        hideKeyboard();
        isEdition = getIntent().getBooleanExtra(EDITION, false);
        usuario = (Usuario) Objects.requireNonNull(getIntent().getExtras()).getSerializable("USUARIO");
        hideButtonNext();

        if (usuario != null && usuario.getPositionResidence() != null) {
            if (!isEdition)
                goHomeActivity();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        if (isEdition) {
            setupToolbar("Alterar minha\nlocalização");
        } else {
            setupToolbar("Cadastrar minha\nlocalização");
        }

    }

    private void initViews() {
        viewMyLocation = findViewById(R.id.ll_body_mylocation);
        btnNex = findViewById(R.id.btn_next);
    }

    private void initGps() {
        gpsInit(new OnLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationActual = new LatLng(location.getLatitude(), location.getLongitude());
                if (!isEdition) {
                    moveCamera(locationActual);
                }

            }

            @Override
            public void onErrorConneciton() {
                AlertUtils.showAlert("Erro ao buscar localização", RegistroLocalizacaoActivity.this);

            }

            @Override
            public void permissonDenied() {
                showDialogPermission();
            }

            @Override
            public void gpsOnDenied() {
                showDialogPermission();
            }
        });
    }

    private void setEvents() {
        btnNex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setPositionResidence(locationSelected);
                goHomeActivity();
            }
        });


        viewMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationActual != null) {
                    locationSelected = locationActual;
                    applyMarker(new com.google.android.gms.maps.model.LatLng(locationActual.getLatitude(), locationActual.getLongitude()));
                }
            }
        });
    }

    private void goHomeActivity() {
        UsuarioFirebase.saveOrUpdate(usuario);
        if (!isEdition) {
            Intent intent = new Intent(RegistroLocalizacaoActivity.this, HomeAlunoActivity.class);
            intent.putExtra("USUARIO", usuario);
            startActivityAnim(intent);
        } else {
            btnNex.setText("Salvar");
        }
        finish();
    }

    private void moveCamera(com.google.android.gms.maps.model.LatLng latlng) {
        moveCamera(new LatLng(latlng.latitude, latlng.longitude));
    }

    private void moveCamera(LatLng latlng) {
        hideKeyboard();
        com.google.android.gms.maps.model.LatLng location = new com.google.android.gms.maps.model.LatLng(latlng.getLatitude(), latlng.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14.5f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        initGps();
        setEvents();

        if (isEdition) {
            locationSelected = new LatLng(usuario.getPositionResidence().getLatitude(), usuario.getPositionResidence().getLongitude());
            applyMarker(new com.google.android.gms.maps.model.LatLng(locationActual.getLatitude(), locationActual.getLongitude()));
            moveCamera(locationSelected);
        }
    }

    private void hideButtonNext() {
        btnNex.animate().alpha(0.3f);
    }

    private void showButtonNext() {
        btnNex.animate().alpha(1f).setDuration(500);
    }

    @Override
    public void onMapClick(com.google.android.gms.maps.model.LatLng latLng) {
        locationSelected = new LatLng(latLng.latitude, latLng.longitude);
        applyMarker(latLng);
    }

    private void applyMarker(com.google.android.gms.maps.model.LatLng latLng) {
        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng));
            moveCamera(latLng);
            showButtonNext();
        }
    }
}