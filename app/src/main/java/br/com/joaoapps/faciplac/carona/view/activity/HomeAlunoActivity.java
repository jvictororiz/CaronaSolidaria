package br.com.joaoapps.faciplac.carona.view.activity;

import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.joaov.faciplac.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.service.firebase.CaronaUsuarioFirebase;
import br.com.joaoapps.faciplac.carona.service.firebase.push.PushFirebaseReceiver;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaBody;
import br.com.joaoapps.faciplac.carona.service.listeners.OnRefreshUsuarisCaronas;
import br.com.joaoapps.faciplac.carona.service.listeners.OnTransacaoListener;
import br.com.joaoapps.faciplac.carona.service.rest.UsuarioRestService;
import br.com.joaoapps.faciplac.carona.view.activity.cadastro.CadastroActivity;
import br.com.joaoapps.faciplac.carona.view.activity.dialogs.ComunicationDialogFragment;
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
    private static final int REQUEST_PHONE_CALL = 115;
    private static final long TIME_TO_CLOSE_BACK = 2000;
    public static final String ENTRY_COMUNICATION = "ENTRY_COMUNICATION";

    private StatusCarona statusCarona;
    private Usuario usuario;
    private CaronaUsuario meuUsuarioCarona;
    private CaronaUsuario caronaUsuarioSelecionado;
    private Location locationActual;
    private ViewGroup viewMyLocation, llBodyProfile;
    private ClusterManager<MarkerItem> mClusterManager;
    private List<CaronaUsuario> listUsers;
    public GoogleMap mMap;
    private DialogItemMapView dialogSelectorItemMapView;
    private SearchViewHV searchViewHV;
    private CaronaUsuarioDialogBottom caronaUsuarioDialogBottom;
    private String numberTellphone;
    private boolean hasClose;
    private BroadcastReceiver receiver;
    private CircleImageView imgProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_aluno);
        try {
            SuperActivity.closeDialogLoad();
            registerReceiver();
            usuario = (Usuario) Objects.requireNonNull(getIntent().getExtras()).getSerializable("USUARIO");
            assert usuario != null;
            setupToolbar(usuario.getNome());
            initViews();
            statusCarona = StatusCarona.RECEBER_CARONA;
            initImgProfile();
            initGps();
            setEvents();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            findAllUsers();
            hideKeyboard();

            BubbleShowCaseBuilder bubbleTutorial1 = createBubbleTutorial("Alterar staus", "Aqui você pode alterar o status caso queria pedir ou oferecer carona", searchViewHV.getImgSelector(), "STAUS", BubbleShowCase.ArrowPosition.TOP);
            BubbleShowCaseBuilder bubbleTutorial2 = createBubbleTutorial("Atualizar alunos", "Novos alunos podem ter entrado na rede, aqui você pode buscar todos novamente", searchViewHV.getImgRefresh(), "REFRESH", BubbleShowCase.ArrowPosition.TOP);
            BubbleShowCaseBuilder bubbleTutorial3 = createBubbleTutorial("Dados pessoais", "Você pode clicar aqui para modificar seus dados pessoais e a sua localização", llBodyProfile, "DADOS_PESSOAIS", BubbleShowCase.ArrowPosition.TOP);

            new BubbleShowCaseSequence()
                    .addShowCase(bubbleTutorial1)
                    .addShowCase(bubbleTutorial2)
                    .addShowCase(bubbleTutorial3)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }

    private void initImgProfile() {
        if (!usuario.getUrlFoto().isEmpty()) {
            Picasso.with(this)
                    .load(usuario.getUrlFoto())
                    .placeholder(R.drawable.icon_user_default)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.icon_user_default)
                    .into(imgProfile);
        }
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    ComunicationCaronaBody comunicationCaronaBody = (ComunicationCaronaBody) intent.getSerializableExtra(ENTRY_COMUNICATION);
                    switch (comunicationCaronaBody.getStep()) {
                        case ComunicationCaronaBody.STEP_ONE_COMUNICATION:
                            teratmentInitComunication(comunicationCaronaBody);
                            break;
                        case ComunicationCaronaBody.STEP_TWO_ACCEPT:
                            treatmentResopnseComunication(comunicationCaronaBody);
                            break;

                        case ComunicationCaronaBody.STEP_TWO_DENIED:
                            treatmentDeniedComunication(comunicationCaronaBody);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(PushFirebaseReceiver.INTENT_FILTER_USER_COMUNICATION));
    }

    private void treatmentDeniedComunication(final ComunicationCaronaBody comunicationCaronaBody) {
        String message = "";
        switch (comunicationCaronaBody.getMyUser().getStatusCarona()) {
            case RECEBER_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", Desculpe mas estou indisponível no momento")));
                break;
            case DAR_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", Eu agradeço porém, agora estou indisponível")));
                break;
        }
        ComunicationDialogFragment popupAlert = ComunicationDialogFragment.newInstance(message, comunicationCaronaBody.getStatusCarona(), comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser());
        popupAlert.show(getSupportFragmentManager(), comunicationCaronaBody.getOtherUser().getCpfUsuario());
        popupAlert.setCancelable(true);
        AppUtil.vibrate(HomeAlunoActivity.this, 200);
        popupAlert.configToStepThree();
    }

    private void treatmentResopnseComunication(final ComunicationCaronaBody comunicationCaronaBody) {
        String message = "";
        switch (comunicationCaronaBody.getMyUser().getStatusCarona()) {
            case RECEBER_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", posso te da uma carona, visualise meu perfil para ver o meu celular :)")));
                break;
            case DAR_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", obrigado, eu aceito a carona, pode visualizar o meu perfil para ver o meu número :)")));
                break;
        }
        final ComunicationDialogFragment popupAlert = ComunicationDialogFragment.newInstance(message, comunicationCaronaBody.getStatusCarona(), comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser());
        popupAlert.show(getSupportFragmentManager(), comunicationCaronaBody.getOtherUser().getCpfUsuario());
        popupAlert.setCancelable(true);
        AppUtil.vibrate(HomeAlunoActivity.this, 200);
        popupAlert.configToStepTwo(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, comunicationCaronaBody.getOtherUser(), HomeAlunoActivity.this);
                dialogSelectorItemMapView.unmaskNumber();
                dialogSelectorItemMapView.hideButton();
                showExpandedEstalbishmentSelected();
                popupAlert.dismiss();
            }
        });
    }

    private void teratmentInitComunication(final ComunicationCaronaBody comunicationCaronaBody) {
        String message = "";
        switch (comunicationCaronaBody.getMyUser().getStatusCarona()) {
            case RECEBER_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", você aceita uma carona?")));
                break;
            case DAR_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", você poderia me da uma carona?")));
                break;
        }
        final ComunicationDialogFragment popupAlert = ComunicationDialogFragment.newInstance(message, comunicationCaronaBody.getStatusCarona(), comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser());
        popupAlert.show(getSupportFragmentManager(), comunicationCaronaBody.getOtherUser().getCpfUsuario());
        popupAlert.setCancelable(true);
        popupAlert.initTimeToDeny();
        AppUtil.vibrate(HomeAlunoActivity.this, 200);

        popupAlert.configToStepOne(new ComunicationDialogFragment.OnClickButtons() {
            @Override
            public void positive() {
                dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, comunicationCaronaBody.getOtherUser(), HomeAlunoActivity.this);
                dialogSelectorItemMapView.unmaskNumber();
                dialogSelectorItemMapView.hideButton();
                AlertUtils.showInfo("Sua resposta foi enviada para " + comunicationCaronaBody.getOtherUser().getNome(), HomeAlunoActivity.this);
                showExpandedEstalbishmentSelected();
                sendFeedbackToOtherUser(comunicationCaronaBody, true);
                popupAlert.dismiss();
            }

            @Override
            public void negative() {
                AlertUtils.showInfo("Sua resposta foi enviada para " + comunicationCaronaBody.getOtherUser().getNome(), HomeAlunoActivity.this);
                sendFeedbackToOtherUser(comunicationCaronaBody, false);
                popupAlert.dismiss();
            }
        });
    }

    private void sendFeedbackToOtherUser(ComunicationCaronaBody comunicationCaronaBody, boolean accept) {
        UsuarioRestService usuarioRestService = new UsuarioRestService(this);
        if (accept) {
            usuarioRestService.aceitarCarona(comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser(), getCallbackStepTwo());
        } else {
            usuarioRestService.negarCarona(comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser(), getCallbackStepTwo());
        }

    }

    private OnTransacaoListener getCallbackStepTwo() {
        return new OnTransacaoListener() {
            @Override
            public void success(Object object) {

            }

            @Override
            public void error(int code) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (meuUsuarioCarona != null) {
            CaronaUsuarioFirebase.openOrUpdate(this, meuUsuarioCarona);
        }
        searchViewHV.clearFocousSearch();
    }

    private void initViews() {
        viewMyLocation = findViewById(R.id.ll_body_mylocation);
        searchViewHV = findViewById(R.id.searchHV);
        dialogSelectorItemMapView = findViewById(R.id.dialgo_selector);
        llBodyProfile = findViewById(R.id.ll_body_profile);
        imgProfile = findViewById(R.id.img_profile);
    }

    private void initGps() {
        gpsInit(new OnLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationActual = location;
                meuUsuarioCarona = new CaronaUsuario(usuario.getPositionResidence(), usuario.getPushId(), usuario.getCpf(), usuario.getUrlFoto(), usuario.getTelefone(), location.getLatitude(), location.getLongitude(), usuario.getNome(), statusCarona);
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

    private void findAllUsers() {
        CaronaUsuarioFirebase.getAll(new OnRefreshUsuarisCaronas() {
            @Override
            public void onSuccess(List<CaronaUsuario> usuarioFirebase) {
                listUsers = usuarioFirebase;
                setUpClusterer(false);

            }

            @Override
            public void onError(int code) {
                AlertUtils.showAlert("Falha ao se conectar, tente novamente mais tarde", HomeAlunoActivity.this);
            }
        });
    }

    private View.OnClickListener callbackGoToEditProfile() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastroActivity.start(HomeAlunoActivity.this, usuario);
            }
        };
    }

    private void setEvents() {
        llBodyProfile.setOnClickListener(callbackGoToEditProfile());
        imgProfile.setOnClickListener(callbackGoToEditProfile());

        findViewById(R.id.toolbar).findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaronaUsuarioFirebase.exit(HomeAlunoActivity.this, Preferences.getLastCpfLogged(HomeAlunoActivity.this));
                finishAnim();
            }
        });
        searchViewHV.configEventChangeType(statusCarona, new SelectorDialogBottom.OnStatusCarona() {
            @Override
            public void selected(StatusCarona statusCarona) {
                if (meuUsuarioCarona != null) {
                    HomeAlunoActivity.this.statusCarona = statusCarona;
                    meuUsuarioCarona.setStatusCarona(statusCarona);
                    CaronaUsuarioFirebase.openOrUpdate(HomeAlunoActivity.this, meuUsuarioCarona);
                    mClusterManager = null;
                    setUpClusterer(false);
                } else {
                    AlertUtils.showAlert("Erro interno. Porfavor, tente novamente mais tarde.", HomeAlunoActivity.this);
                }
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
                findAllUsers();
            }
        });

        searchViewHV.setListenerDistance(new SearchViewHV.OnDistanceListener() {
            @Override
            public void changed(long distance) {
                findAllUsers();
            }
        });

        caronaUsuarioDialogBottom = new CaronaUsuarioDialogBottom(this);

        viewMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meuUsuarioCarona != null) {
                    moveCamera(new LatLng(meuUsuarioCarona.getLatitude(), meuUsuarioCarona.getLongitude()));
                } else {
                    AlertUtils.showAlert("Sessão Expirada", HomeAlunoActivity.this);
                    startActivityClearingOthers(LoginActivity.class);
                }
            }
        });

        findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasClose) {
                    HomeAlunoActivity.super.finish();
                } else {
                    Toast.makeText(HomeAlunoActivity.this, "Aperte mais uma vez para sair", Toast.LENGTH_SHORT).show();
                    hasClose = true;
                    initTimeToClose();
                }
            }
        });
    }

    private void moveCamera(LatLng latlng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.5f));

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
        showTutorialIfFirstAccess();
    }

    private void showTutorialIfFirstAccess() {
        createBubbleTutorial("Desbloqueio do telefone", "O número deste aluno será desbloqueado assim que ele responder ao seu chamado.", null, "TELEFONE", R.mipmap.ic_cell_block, BubbleShowCase.ArrowPosition.BOTTOM).show();
//        new BubbleShowCaseSequence()
//                .addShowCase(bubbleTutorial1)
//                .show();
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
        if (dialogSelectorItemMapView.isEnableCall()) {
            AppUtil.callPhone(this, number, REQUEST_PHONE_CALL);
        } else {
            AlertUtils.showAlert("Primeiro é necessário que este usuário aceite.", this);
        }
    }

    @Override
    public void onClickComunication(final CaronaUsuario caronaUsuarioSelecionado) {
        UsuarioRestService usuarioRestService = new UsuarioRestService(this);
        if (statusCarona == StatusCarona.DAR_CARONA) {
            usuarioRestService.oferecerCarona(meuUsuarioCarona, caronaUsuarioSelecionado, getCallback());
        } else {
            usuarioRestService.pedirCarona(meuUsuarioCarona, caronaUsuarioSelecionado, getCallback());
        }
        hideDetailedEstablishementSelected();
    }

    private OnTransacaoListener getCallback() {
        return new OnTransacaoListener() {
            @Override
            public void success(Object object) {
                AlertUtils.showInfo("Foi enviado um alerta para " + caronaUsuarioSelecionado.getNome() + ", aguarde sua resposta... ", HomeAlunoActivity.this);
            }

            @Override
            public void error(int code) {
                AlertUtils.showAlert("Falha ao buscar a sua localização", HomeAlunoActivity.this);
            }
        };
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
        if (mMap != null && meuUsuarioCarona != null) {
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
        boolean empty = true;
        if (listUsers != null && !listUsers.isEmpty()) {
            for (CaronaUsuario caronaUsuario : getUsersByStatusCarona()) {
                if (validateUser(caronaUsuario, query)) {
                    empty = false;
                    double lat = (caronaUsuario.getLatitude());
                    double lng = (caronaUsuario.getLongitude());
                    CharSequence charSequence = caronaUsuario.getNome().subSequence(0, 1);
                    MarkerItem markItem = new MarkerItem(this, new LatLng(lat, lng), 1, charSequence);
                    markItem.setCaronaUsuario(caronaUsuario);
                    mClusterManager.addItem(markItem);
                }
            }
            if (empty) {
                AlertUtils.showAlert("Nenhum usuário compativel online no momento.", this);
            } else {
                int count = mClusterManager.getAlgorithm().getItems().size();
                if (count > 1) {
                    AlertUtils.showInfo(count + " alunos online no momento", this);
                } else {
                    AlertUtils.showInfo("Apenas um usuário online no momento", this);
                }

            }
        }
    }

    private boolean isDistanceMin(CaronaUsuario meuUsuarioCarona, CaronaUsuario caronaUsuario) {
        Location locOne = new Location("");
        Location locTwo = new Location("");
        locOne.setLatitude(meuUsuarioCarona.getPositionResidence().getLatitude());
        locOne.setLongitude(meuUsuarioCarona.getPositionResidence().getLongitude());
        locTwo.setLatitude(caronaUsuario.getPositionResidence().getLatitude());
        locTwo.setLongitude(caronaUsuario.getPositionResidence().getLongitude());
        //TODO: REVER ESSA REGRA DE NEGOCIO (Tem que ver se o cara que pede carona tem que diminuir a distancia de acordo com quem oferece)
        float distanceMin = meuUsuarioCarona.getStatusCarona() == StatusCarona.DAR_CARONA ? searchViewHV.getDistanceMin() : 2000f;
        return locOne.distanceTo(locTwo) <= distanceMin;
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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    String uri = "tel:" + numberTellphone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                } else {
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (dialogSelectorItemMapView != null && dialogSelectorItemMapView.getCurrentState() != DialogItemMapView.TYPE_STATE.HIDE) {
            hideDetailedEstablishementSelected();
            return;
        }
        if (caronaUsuarioDialogBottom != null && caronaUsuarioDialogBottom.isShowing()) {
            caronaUsuarioDialogBottom.dismiss();
            return;
        }
        if (hasClose) {
            super.onBackPressed();
        } else {
            Toast.makeText(HomeAlunoActivity.this, "Aperte mais uma vez para sair", Toast.LENGTH_SHORT).show();
            hasClose = true;
            initTimeToClose();
        }

    }

    private void initTimeToClose() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    hasClose = false;
                } catch (Exception ignore) {

                }
            }
        }, TIME_TO_CLOSE_BACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CadastroActivity.USER_CODE) {
            if (data != null) {
                usuario = (Usuario) data.getSerializableExtra("USER");
                if (usuario != null) {
                    setSupportActionBar(null);
                    setupToolbar(usuario.getNome());
                    initImgProfile();
                    hideKeyboard();
                }
            }
        }
    }

    private BubbleShowCaseBuilder createBubbleTutorial(String title, String description, View view, String idImage, BubbleShowCase.ArrowPosition direction) {
        return createBubbleTutorial(title, description, view, idImage, R.drawable.icon_pedindo_carona_branco, direction);
    }

    private BubbleShowCaseBuilder createBubbleTutorial(String title, String description, View view, String idImage, int icon, BubbleShowCase.ArrowPosition direction) {
        BubbleShowCaseBuilder image = new BubbleShowCaseBuilder(this)
                .title(title)
                .description(description)
                .arrowPosition(direction)
                .backgroundColor(getResources().getColor(R.color.colorPrimary))
                .textColor(Color.WHITE)
                .showOnce(idImage)
                .titleTextSize(18)
                .descriptionTextSize(14)
                .image(getResources().getDrawable(icon));

        if (view != null) {
            image.targetView(view);
        }
        return image;

    }


    @Override
    public void hideKeyboard() {
        super.hideKeyboard();
        searchViewHV.clearFocousSearch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CaronaUsuarioFirebase.exit(this, Preferences.getLastCpfLogged(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean validateUser(CaronaUsuario caronaUsuario, String query) {
        return !usuario.getCpf().equals(caronaUsuario.getCpfUsuario()) &&
                caronaUsuario.getNome().toLowerCase().contains(query.toLowerCase()) &&
                isDistanceMin(meuUsuarioCarona, caronaUsuario);
    }
}


