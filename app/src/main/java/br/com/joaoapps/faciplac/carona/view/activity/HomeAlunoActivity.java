package br.com.joaoapps.faciplac.carona.view.activity;

import com.example.joaov.caronasolidaria.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.service.firebase.CaronaUsuarioFirebase;
import br.com.joaoapps.faciplac.carona.service.listeners.OnRefreshUsuarisCaronas;
import br.com.joaoapps.faciplac.carona.service.listeners.OnTransacaoListener;
import br.com.joaoapps.faciplac.carona.service.rest.UsuarioRestService;
import br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration.CustomRender;
import br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration.MarkerItem;
import br.com.joaoapps.faciplac.carona.view.componentes.dialogSelectorItemMap.DialogItemMapView;
import br.com.joaoapps.faciplac.carona.view.componentes.dialogSelectorItemMap.DialogSelectorItemMapView;
import br.com.joaoapps.faciplac.carona.view.componentes.search.SearchViewHV;
import br.com.joaoapps.faciplac.carona.view.componentes.sheetCardSelector.SelectorDialogBottom;
import br.com.joaoapps.faciplac.carona.view.componentes.sheetDialogEstablishments.CaronaUsuarioDialogBottom;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;
import br.com.joaoapps.faciplac.carona.view.utils.AppUtil;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

public class HomeAlunoActivity extends LocationActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, DialogItemMapView.OnEventesCaronaUsuarioDetailed {
    private static final long MIN_TIME_BW_UPDATES = 50000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 2000;
    private static final int REQUEST_PHONE_CALL = 115;

    private StatusCarona statusCarona;
    private Usuario usuario;
    private CaronaUsuario meuUsuarioCarona;
    private CaronaUsuario caronaUsuarioSelecionado;
    private Location locationActual;
    private LinearLayout viewMyLocation;
    private ClusterManager<MarkerItem> mClusterManager;
    private List<CaronaUsuario> listUsers;
    public GoogleMap mMap;
    private DialogItemMapView dialogSelectorItemMapView;
    private SearchViewHV searchViewHV;
    private CaronaUsuarioDialogBottom caronaUsuarioDialogBottom;
    private String numberTellphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_aluno);
        setupToolbar("");
        initViews();
        statusCarona = StatusCarona.DAR_CARONA;
        usuario = (Usuario) Objects.requireNonNull(getIntent().getExtras()).getSerializable("USUARIO");
        initGps();
        setEvents();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findAllUsers(false);
        hideKeyboard();
    }

    private void initViews() {
        viewMyLocation = findViewById(R.id.ll_body_mylocation);
        searchViewHV = findViewById(R.id.searchHV);
        dialogSelectorItemMapView = findViewById(R.id.dialgo_selector);
    }

    private void initGps() {
        gpsInit(new OnLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationActual = location;
                meuUsuarioCarona = new CaronaUsuario(usuario.getPushId(), usuario.getCpf(), usuario.getUrlFoto(), usuario.getTelefone(), location.getLatitude(), location.getLongitude(), usuario.getNome(), "5", statusCarona);
                CaronaUsuarioFirebase.openOrUpdate(HomeAlunoActivity.this, meuUsuarioCarona);
                moveCamera(new LatLng(meuUsuarioCarona.getLatitude(), meuUsuarioCarona.getLongitude()));
                caronaUsuarioDialogBottom.init(meuUsuarioCarona, new DialogSelectorItemMapView.OnSelectCaronaUsuario() {
                    @Override
                    public void select(CaronaUsuario caronaUsuario) {
                        caronaUsuarioSelecionado = caronaUsuario;
                        dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, caronaUsuarioSelecionado, HomeAlunoActivity.this);
                        showExpandedEstalbishmentSelected();
                    }
                });
                ;
            }

            @Override
            public void onErrorConneciton() {
                AlertUtils.showAlert("Erro ao buscar localização", HomeAlunoActivity.this);

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

    private void findAllUsers(final boolean refreshForced) {
        CaronaUsuarioFirebase.getAll(new OnRefreshUsuarisCaronas() {
            @Override
            public void onSuccess(List<CaronaUsuario> usuarioFirebase) {
                listUsers = usuarioFirebase;
                if ((mMap != null && mClusterManager == null) || refreshForced) {
                    setUpClusterer(false);
                }

            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void setEvents() {
        findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaronaUsuarioFirebase.exit(HomeAlunoActivity.this, Preferences.getLastCpfLogged(HomeAlunoActivity.this));
                finishAnim();
            }
        });
        searchViewHV.configEventChangeType(statusCarona, new SelectorDialogBottom.OnStatusCarona() {
            @Override
            public void selected(StatusCarona statusCarona) {
                HomeAlunoActivity.this.statusCarona = statusCarona;
                meuUsuarioCarona.setStatusCarona(statusCarona);
                CaronaUsuarioFirebase.openOrUpdate(HomeAlunoActivity.this, meuUsuarioCarona);
                mClusterManager = null;
                setUpClusterer(false);
            }
        }, new SearchViewHV.OnQurySubmit() {
            @Override
            public void submit(String query) {
                setUpClusterer(false, query);
            }
        });

        searchViewHV.setRefreshListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGps();
                mClusterManager = null;
                findAllUsers(true);
            }
        });

        caronaUsuarioDialogBottom = new CaronaUsuarioDialogBottom(this);

        viewMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCamera(new LatLng(meuUsuarioCarona.getLatitude(), meuUsuarioCarona.getLongitude()));
            }
        });

        findById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void moveCamera(LatLng latlng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14.5f));

    }

    private void showCollectedEstablishment() {
        hideKeyboard();
        viewMyLocation.animate().setDuration(200).alpha(1f);
        viewMyLocation.animate().setDuration(180).translationY(-370);
        searchViewHV.show();
        dialogSelectorItemMapView.showCollected();
    }

    private void showExpandedEstalbishmentSelected() {
        hideKeyboard();
        viewMyLocation.animate().setDuration(200).alpha(0f);
        searchViewHV.hide();
        dialogSelectorItemMapView.showExpanded();
    }

    private void hideDetailedEstablishementSelected() {
        searchViewHV.show();
        viewMyLocation.animate().setDuration(200).alpha(1f);
        dialogSelectorItemMapView.hide();
        viewMyLocation.animate().setDuration(180).translationY(0);
    }

    private Location getLocationEstablishmenbt(CaronaUsuario caronaUsuario) {
        Location location = new Location("");
        location.setLatitude((caronaUsuario.getLatitude()));
        location.setLongitude((caronaUsuario.getLongitude()));
        return location;
    }

    public void moveToLocation(Location location) {
        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));
    }


    @Override
    public void onClickCollected() {
        showExpandedEstalbishmentSelected();
    }

    @Override
    public void onCall(String number) {
        this.numberTellphone = number;
        AppUtil.callPhone(this, number, REQUEST_PHONE_CALL);
    }

    @Override
    public void onClickComunication(CaronaUsuario caronaUsuarioSelecionado) {
//        Uri gmmIntentUri = Uri.parse("geo:" + caronaUsuarioSelecionado.getLatitude() + "," + caronaUsuarioSelecionado.getLongitude() + "?q=" + location.getLatitude() + "," + location.getLongitude());
//        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//        intent.setData(gmmIntentUri);
//        startActivity(intent);

        UsuarioRestService usuarioRestService = new UsuarioRestService(this);
        if (statusCarona == StatusCarona.DAR_CARONA) {
            usuarioRestService.oferecerCarona(meuUsuarioCarona, caronaUsuarioSelecionado, new OnTransacaoListener() {
                @Override
                public void success(Object object) {
                    Log.i("", "");
                }

                @Override
                public void error(int code) {

                }
            });
        } else {
            usuarioRestService.pedirCarona(meuUsuarioCarona, caronaUsuarioSelecionado, new OnTransacaoListener() {
                @Override
                public void success(Object object) {
                    Log.i("", "");
                }

                @Override
                public void error(int code) {
                    Log.i("", "");
                }
            });
        }


    }

    @Override
    public void onMapClick(LatLng latLng) {
        hideDetailedEstablishementSelected();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (meuUsuarioCarona != null) {
            if (caronaUsuarioSelecionado != null) {
                dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, caronaUsuarioSelecionado, this);
                showExpandedEstalbishmentSelected();
                moveToLocation(getLocationEstablishmenbt(caronaUsuarioSelecionado));
            } else {
                moveCamera(new LatLng(meuUsuarioCarona.getLatitude(), meuUsuarioCarona.getLongitude()));
            }
        }
        if (listUsers != null && mClusterManager == null) {
            setUpClusterer(true);
        }
    }

    private void setUpClusterer(boolean isFirst) {
        setUpClusterer(isFirst, "");
    }

    private void setUpClusterer(boolean isFirst, String query) {
        if (mMap != null) {
            mMap.clear();
            if (mClusterManager != null) {
                mClusterManager.clearItems();
            }
            mClusterManager = new ClusterManager<>(this, mMap);
            mClusterManager.setRenderer(new CustomRender(this, mMap, mClusterManager));
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(mClusterManager);
            convertEstablishmentInPointer(query);
            setClicksInPoints();
        }
        if (mClusterManager != null) {
            mClusterManager.cluster();
        }

    }

    private void setClicksInPoints() {
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
            @Override
            public boolean onClusterItemClick(MarkerItem markerItem) {
                caronaUsuarioSelecionado = markerItem.getCaronaUsuario();
                dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, markerItem.getCaronaUsuario(), HomeAlunoActivity.this);
                showCollectedEstablishment();

                return true;
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkerItem>() {
            @Override
            public boolean onClusterClick(Cluster<MarkerItem> cluster) {
                hideDetailedEstablishementSelected();
                caronaUsuarioDialogBottom.show(getCaronaUsuarios(cluster.getItems()));
                return true;
            }
        });
    }

    private List<CaronaUsuario> getCaronaUsuarios(Collection<MarkerItem> items) {
        ArrayList<CaronaUsuario> caronaUsuarios = new ArrayList<>();
        for (MarkerItem markerItem : items) {
            caronaUsuarios.add(markerItem.getCaronaUsuario());
        }
        return caronaUsuarios;
    }


    private void convertEstablishmentInPointer(String query) {
        if (listUsers != null && !listUsers.isEmpty()) {
            for (CaronaUsuario caronaUsuario : getUsersByStatusCarona()) {
                if (!usuario.getCpf().equals(caronaUsuario.getCpfUsuario()) && caronaUsuario.getNome().toLowerCase().contains(query.toLowerCase())) {
                    double lat = (caronaUsuario.getLatitude());
                    double lng = (caronaUsuario.getLongitude());
                    CharSequence charSequence = caronaUsuario.getNome().subSequence(0, 1);
                    MarkerItem markItem = new MarkerItem(this, new LatLng(lat, lng), 1, charSequence);
                    markItem.setCaronaUsuario(caronaUsuario);
                    mClusterManager.addItem(markItem);
                }
            }
        }
    }

    private List<CaronaUsuario> getUsersByStatusCarona() {
        List<CaronaUsuario> statusCaronas = new ArrayList<>();
        for (CaronaUsuario caronaUsuario : listUsers) {
            if (!caronaUsuario.getStatusCarona().equals(statusCarona)) {
                statusCaronas.add(caronaUsuario);
            }
        }
        return statusCaronas;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String uri = "tel:" + numberTellphone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                } else {
                    AlertUtils.showAlert("Sem premissão para acessar o telefone", this);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CaronaUsuarioFirebase.exit(this, Preferences.getLastCpfLogged(this));
    }
}