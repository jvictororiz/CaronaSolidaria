package br.com.joaoapps.faciplac.carona.view.activity;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.joaov.faciplac.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
import br.com.joaoapps.faciplac.carona.service.rest.OnEventListenerAbstract;
import br.com.joaoapps.faciplac.carona.service.rest.UsuarioRestService;
import br.com.joaoapps.faciplac.carona.view.activity.cadastro.CadastroActivity;
import br.com.joaoapps.faciplac.carona.view.activity.dialogs.ComunicationDialogFragment;
import br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration.CustomRender;
import br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration.MarkerItem;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.ChatDialogView;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.OwlBottomSheetBaseJv;
import br.com.joaoapps.faciplac.carona.view.componentes.dialogSelectorItemMap.DialogItemMapView;
import br.com.joaoapps.faciplac.carona.view.componentes.search.SearchViewHV;
import br.com.joaoapps.faciplac.carona.view.componentes.sheetDialogEstablishments.CaronaUsuarioDialogBottom;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;
import br.com.joaoapps.faciplac.carona.view.utils.AppUtil;
import br.com.joaoapps.faciplac.carona.view.utils.NotificationUtils;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

public class HomeAlunoActivity extends LocationActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, DialogItemMapView.OnEventesCaronaUsuarioDetailed {
    private static final int REQUEST_PHONE_CALL = 115;
    private static final long TIME_TO_CLOSE_BACK = 2000;
    public static final String ENTRY_COMUNICATION = "ENTRY_COMUNICATION";
    private final int ID_NOTIFICATION_MESSAGE = 1;

    private Location locationActual;
    private ViewGroup viewMyLocation, llBodyProfile;
    public GoogleMap mMap;
    private ClusterManager<MarkerItem> mClusterManager;
    private List<CaronaUsuario> listUsers;
    private StatusCarona statusCarona;
    private Usuario usuario;
    private CaronaUsuario mUsuarioCarona;
    private CaronaUsuario caronaUsuarioSelecionado;
    private DialogItemMapView dialogSelectorItemMapView;
    private SearchViewHV searchViewHV;
    private CaronaUsuarioDialogBottom caronaUsuarioDialogBottom;
    private ReceiverPushBroadcastReceiver receiver;
    private CircleImageView imgProfile;
    private ChatDialogView mChatDialog;
    private ComunicationDialogFragment popupAlertPendent;
    private String numberTelephone;
    private boolean hasClose;
    private boolean hasDistanceMin;
    private boolean isFirstSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_aluno);
        isFirstSearch = true;
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
            configTutorial();
            mChatDialog.hide();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }

    private void configTutorial() {
        BubbleShowCaseBuilder bubbleTutorial1 = createBubbleTutorial("Alterar staus", "Aqui você pode alterar o status caso queria pedir ou oferecer carona", searchViewHV.getImgSelector(), "STAUS");
        BubbleShowCaseBuilder bubbleTutorial2 = createBubbleTutorial("Atualizar alunos", "Novos alunos podem ter entrado na rede, aqui você pode buscar todos novamente", searchViewHV.getImgRefresh(), "REFRESH");
        BubbleShowCaseBuilder bubbleTutorial3 = createBubbleTutorial("Dados pessoais", "Você pode clicar aqui para modificar seus dados pessoais e a sua localização", llBodyProfile, "DADOS_PESSOAIS");

        new BubbleShowCaseSequence()
                .addShowCase(bubbleTutorial1)
                .addShowCase(bubbleTutorial2)
                .addShowCase(bubbleTutorial3)
                .show();
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
        receiver = new ReceiverPushBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    ComunicationCaronaBody comunicationCaronaBody = (ComunicationCaronaBody) intent.getSerializableExtra(ENTRY_COMUNICATION);
                    switch (comunicationCaronaBody.getStep()) {
                        case ComunicationCaronaBody.STEP_ONE_COMUNICATION:
                            treatmentInitCommunication(comunicationCaronaBody);
                            break;
                        case ComunicationCaronaBody.STEP_TWO_ACCEPT:
                            Answers.getInstance().logCustom(new CustomEvent("Carona Aceita"));
                            treatmentResponseCommunication(comunicationCaronaBody.getOtherUser());
                            break;

                        case ComunicationCaronaBody.STEP_TWO_DENIED:
                            Answers.getInstance().logCustom(new CustomEvent("Carona Recusada"));
                            treatmentDeniedCommunication(comunicationCaronaBody);
                            break;
                        case ComunicationCaronaBody.RECEIVE_MESSAGE:
                            if (comunicationCaronaBody.getOtherUser().getStatusCarona() != mUsuarioCarona.getStatusCarona()) {
                                treatmentReceiveMessage(comunicationCaronaBody);
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(PushFirebaseReceiver.INTENT_FILTER_USER_COMUNICATION);
        registerReceiver(receiver, filter);

    }

    private void treatmentReceiveMessage(ComunicationCaronaBody comunicationCaronaBody) {
        if (!mChatDialog.isPrepared()) {
            mChatDialog.show();
            mChatDialog.prepareChat(this, getCallbackExpand(), mUsuarioCarona, comunicationCaronaBody.getOtherUser(), message1 ->
                    new UsuarioRestService(HomeAlunoActivity.this).sendMessageChat(mUsuarioCarona, comunicationCaronaBody.getOtherUser(), message1, getCallbackSendMessage()));
        }
        if (!mChatDialog.isExpanded()) {
            mChatDialog.expand();
        }

        if (!AppUtil.isOpeningApp(this) || (AppUtil.isOpeningApp(this) && !mChatDialog.isExpanded())) {
            NotificationUtils.showNotification(this, ID_NOTIFICATION_MESSAGE, comunicationCaronaBody.getOtherUser().getNome(), comunicationCaronaBody.getMessage());
        } else {
            AppUtil.vibrate(HomeAlunoActivity.this, 30);
        }
        mChatDialog.sendMessage(comunicationCaronaBody.getMessage(), comunicationCaronaBody.getOtherUser());
    }

    private OwlBottomSheetBaseJv.OnExpandBottomSheetListener getCallbackExpand() {
        return new OwlBottomSheetBaseJv.OnExpandBottomSheetListener() {
            @Override
            public void onExpand() {
                if (AppUtil.isOpeningApp(HomeAlunoActivity.this)) {
                    NotificationUtils.cancelNotification(HomeAlunoActivity.this, ID_NOTIFICATION_MESSAGE);
                }
            }
        };
    }

    private void treatmentDeniedCommunication(final ComunicationCaronaBody comunicationCaronaBody) {
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

    private void treatmentResponseCommunication(final CaronaUsuario otherUser) {
        AppUtil.vibrate(HomeAlunoActivity.this, 200);
        mChatDialog.show();
        mChatDialog.prepareChat(this, getCallbackExpand(), mUsuarioCarona, otherUser, message1 ->
                new UsuarioRestService(HomeAlunoActivity.this).sendMessageChat(mUsuarioCarona, otherUser, message1, getCallbackSendMessage()));
        mChatDialog.expand();
        new UsuarioRestService(this).sendMessageChat(mUsuarioCarona, otherUser, "", getCallbackSendMessage());
    }

    private OnEventListenerAbstract<Void> getCallbackSendMessage() {
        return new OnEventListenerAbstract<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
            }

            @Override
            public void onError(int erro) {
                super.onError(erro);
            }
        };
    }

    private void treatmentInitCommunication(final ComunicationCaronaBody comunicationCaronaBody) {
        String message = "";
        switch (comunicationCaronaBody.getMyUser().getStatusCarona()) {
            case RECEBER_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", você aceita uma carona?")));
                Answers.getInstance().logCustom(new CustomEvent("Ofereceu carona"));
                break;
            case DAR_CARONA:
                message = ("Olá " + (comunicationCaronaBody.getMyUser().getNome().concat(", você poderia me da uma carona?")));
                Answers.getInstance().logCustom(new CustomEvent("Pediu carona"));
                break;
        }


        final ComunicationDialogFragment popupAlert = ComunicationDialogFragment.newInstance(message, comunicationCaronaBody.getStatusCarona(), comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser());

        if (!AppUtil.isOpeningApp(this)) {
            NotificationUtils.showNotification(this, ID_NOTIFICATION_MESSAGE, comunicationCaronaBody.getOtherUser().getNome(), message);
            this.popupAlertPendent = popupAlert;
        } else {
            initPopupTime(popupAlert);
        }


        popupAlert.configToStepOne(new ComunicationDialogFragment.OnClickButtons() {
            @Override
            public void positive() {
                dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, comunicationCaronaBody.getOtherUser(), HomeAlunoActivity.this);
                dialogSelectorItemMapView.unmaskNumber();
                dialogSelectorItemMapView.hideButton();
                AlertUtils.showInfo("Sua resposta foi enviada para " + comunicationCaronaBody.getOtherUser().getNome(), HomeAlunoActivity.this);
                showExpandedUserSelected();
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

    private void initPopupTime(ComunicationDialogFragment popupAlert) {
        popupAlert.show(getSupportFragmentManager(), usuario.getCpf());
        popupAlert.setCancelable(true);
        popupAlert.initTimeToDeny();
        AppUtil.vibrate(HomeAlunoActivity.this, 200);
    }

    private void sendFeedbackToOtherUser(ComunicationCaronaBody comunicationCaronaBody, boolean accept) {
        UsuarioRestService usuarioRestService = new UsuarioRestService(this);
        if (accept) {
            usuarioRestService.aceitarCarona(comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser(), getCallbackStepTwo());
        } else {
            usuarioRestService.negarCarona(comunicationCaronaBody.getMyUser(), comunicationCaronaBody.getOtherUser(), getCallbackStepTwo());
        }

    }

    private OnEventListenerAbstract<Void> getCallbackStepTwo() {
        return new OnEventListenerAbstract<Void>() {
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (popupAlertPendent != null) {
            initPopupTime(popupAlertPendent);
            popupAlertPendent = null;
        }
        searchViewHV.clearFocousSearch();
    }


    private void initViews() {
        viewMyLocation = findViewById(R.id.ll_body_mylocation);
        searchViewHV = findViewById(R.id.searchHV);
        dialogSelectorItemMapView = findViewById(R.id.dialgo_selector);
        llBodyProfile = findViewById(R.id.ll_body_profile);
        imgProfile = findViewById(R.id.img_profile);
        mChatDialog = findViewById(R.id.owl_bottom_sheet);
    }

    private void initGps() {
        gpsInit(new OnLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationActual = location;
                mUsuarioCarona = new CaronaUsuario(usuario.getPositionResidence(), usuario.getPushId(), usuario.getCpf(), usuario.getUrlFoto(), usuario.getTelefone(), location.getLatitude(), location.getLongitude(), usuario.getNome(), statusCarona);
                CaronaUsuarioFirebase.openOrUpdate(HomeAlunoActivity.this, mUsuarioCarona);
                moveCamera(new LatLng(mUsuarioCarona.getLatitude(), mUsuarioCarona.getLongitude()));
                if (listUsers != null && mClusterManager == null) {
                    setUpClusterer();
                }
                caronaUsuarioDialogBottom.init(mUsuarioCarona, caronaUsuario -> {
                    caronaUsuarioSelecionado = caronaUsuario;
                    dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, caronaUsuarioSelecionado, HomeAlunoActivity.this);
                    showExpandedUserSelected();
                });
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
        CaronaUsuarioFirebase.getAll(this, new OnEventListenerAbstract<List<CaronaUsuario>>() {
            @Override
            public void onSuccess(List<CaronaUsuario> userFirebase) {
                listUsers = userFirebase;
                closeDialogIfOtherUserExit(listUsers);
                setUpClusterer();

            }

            @Override
            public void onError(int code) {
                AlertUtils.showAlert("Falha ao se conectar, tente novamente mais tarde", HomeAlunoActivity.this);
            }
        });
    }

    private void closeDialogIfOtherUserExit(List<CaronaUsuario> listUsers) {
        if (mChatDialog.isPrepared()) {
            for (CaronaUsuario otherUser : listUsers) {
                if (mChatDialog.equalsUser(otherUser.getCpfUsuario())) {
                    AlertUtils.showAlert(otherUser.getNome() + " saiu da conversa", this);
                    mChatDialog.closeDialog();
                    return;
                }
            }
        }
    }

    private View.OnClickListener callbackGoToEditProfile() {
        return view -> CadastroActivity.start(HomeAlunoActivity.this, usuario);
    }

    private void setEvents() {
        llBodyProfile.setOnClickListener(callbackGoToEditProfile());
        imgProfile.setOnClickListener(callbackGoToEditProfile());

        findViewById(R.id.toolbar).findViewById(R.id.toolbar_back).setOnClickListener(view -> {
            finishAnim();
        });
        searchViewHV.configEventChangeType(statusCarona, statusCarona -> {
            if (mUsuarioCarona != null) {
                isFirstSearch = true;
                HomeAlunoActivity.this.statusCarona = statusCarona;
                if (statusCarona == StatusCarona.DAR_CARONA && hasDistanceMin) {
                    searchViewHV.showMinDistanceViews();
                } else {
                    searchViewHV.hideMinDistanceViews();
                }
                mUsuarioCarona.setStatusCarona(statusCarona);
                CaronaUsuarioFirebase.openOrUpdate(HomeAlunoActivity.this, mUsuarioCarona);
                mClusterManager = null;
                setUpClusterer();
            } else {
                AlertUtils.showAlert("Erro interno. Porfavor, tente novamente mais tarde.", HomeAlunoActivity.this);
            }
        }, this::setUpClusterer);

        searchViewHV.setRefreshListener(view -> {
            initGps();
            mClusterManager = null;
        });
        searchViewHV.hideMinDistanceViews();
        searchViewHV.setListenerCheckbox((buttonView, isChecked) -> {
            hasDistanceMin = isChecked;
            if (statusCarona == StatusCarona.DAR_CARONA && hasDistanceMin) {
                searchViewHV.showMinDistanceViews();
            } else {
                searchViewHV.hideMinDistanceViews();
            }
        });

        searchViewHV.setListenerDistance(new SearchViewHV.OnDistanceListener() {
            @Override
            public void changed(long distance) {
                setUpClusterer();
            }
        });

        caronaUsuarioDialogBottom = new CaronaUsuarioDialogBottom(this);

        viewMyLocation.setOnClickListener(view -> {
            if (mUsuarioCarona != null) {
                moveCamera(new LatLng(mUsuarioCarona.getLatitude(), mUsuarioCarona.getLongitude()));
            } else {
                AlertUtils.showAlert("Sessão Expirada", HomeAlunoActivity.this);
                startActivityClearingOthers(LoginActivity.class);
            }
        });

        findViewById(R.id.toolbar_back).setOnClickListener(view -> {
            if (hasClose) {
                HomeAlunoActivity.super.finish();
            } else {
                Toast.makeText(HomeAlunoActivity.this, "Aperte mais uma vez para sair", Toast.LENGTH_SHORT).show();
                hasClose = true;
                initTimeToClose();
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

    private void showExpandedUserSelected() {
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
        showExpandedUserSelected();
    }

    @Override
    public void onCall(String number) {
        this.numberTelephone = number;
        if (dialogSelectorItemMapView.isEnableCall()) {
            AppUtil.callPhone(this, number, REQUEST_PHONE_CALL);
        } else {
            AlertUtils.showAlert("Primeiro é necessário que este usuário aceite.", this);
        }
    }

    @Override
    public void onClickCommunication(final CaronaUsuario caronaUsuarioSelecionado) {
        CaronaUsuarioFirebase.isOnline(caronaUsuarioSelecionado.getCpfUsuario(), new OnEventListenerAbstract<Boolean>() {
            @Override
            public void onSuccess(Boolean isOnline) {
                super.onSuccess(isOnline);

                if (isOnline) {
                    UsuarioRestService usuarioRestService = new UsuarioRestService(HomeAlunoActivity.this);
                    if (statusCarona == StatusCarona.DAR_CARONA) {
                        usuarioRestService.oferecerCarona(mUsuarioCarona, caronaUsuarioSelecionado, getCallback());
                    } else {
                        usuarioRestService.pedirCarona(mUsuarioCarona, caronaUsuarioSelecionado, getCallback());
                    }
                } else {
                    AlertUtils.showAlert("Este usuário parece não está mais disponível, por favor, clique no ícone de atualização para obter os alunos on-line.", HomeAlunoActivity.this);
                }
            }

            @Override
            public void onError(int erro) {
                super.onError(erro);
                Toast.makeText(HomeAlunoActivity.this, "Ação não pode ser completada, verifique sua conexão.", Toast.LENGTH_SHORT).show();
            }
        });
        hideDetailedEstablishementSelected();
    }

    private OnEventListenerAbstract<Void> getCallback() {
        return new OnEventListenerAbstract<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                AlertUtils.showInfo("Foi enviado um alerta para " + caronaUsuarioSelecionado.getNome() + ", aguarde sua resposta... ", HomeAlunoActivity.this);
            }

            @Override
            public void onError(int erro) {
                super.onError(erro);
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
        if (mUsuarioCarona != null) {
            if (caronaUsuarioSelecionado != null) {
                dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, caronaUsuarioSelecionado, this);
                showExpandedUserSelected();
                moveToLocation(getLocationEstablishmenbt(caronaUsuarioSelecionado));
            } else {
                moveCamera(new LatLng(mUsuarioCarona.getLatitude(), mUsuarioCarona.getLongitude()));
            }
        }
        if (listUsers != null && mClusterManager == null) {
            setUpClusterer();
        }

    }

    private void setUpClusterer() {
        setUpClusterer("");
    }

    private void setUpClusterer(String query) {
        if (mMap != null && mUsuarioCarona != null) {
            mMap.clear();
            if (mClusterManager != null) {
                mClusterManager.clearItems();
            }
            mClusterManager = new ClusterManager<>(this, mMap);
            mClusterManager.setRenderer(new CustomRender(this, mMap, mClusterManager));
            setClicksInPoints();
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(mClusterManager);
            convertEstablishmentInPointer(query);
        }
        if (mClusterManager != null) {
            mClusterManager.cluster();
        }

    }

    private void setClicksInPoints() {
        mClusterManager.setOnClusterItemClickListener(markerItem -> {
            caronaUsuarioSelecionado = markerItem.getCaronaUsuario();
            dialogSelectorItemMapView.refreshCaronaUsuarios(locationActual, markerItem.getCaronaUsuario(), HomeAlunoActivity.this);
            showCollectedEstablishment();

            return true;
        });

        mClusterManager.setOnClusterClickListener(cluster -> {
            hideDetailedEstablishementSelected();
            caronaUsuarioDialogBottom.show(getCaronaUsuarios(cluster.getItems()));
            return true;
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

            if (isFirstSearch || !query.isEmpty()) {
                if (empty) {
                    AlertUtils.showInfo("Nenhum usuário compativel online no momento.", this);
                } else {
                    int count = mClusterManager.getAlgorithm().getItems().size();
                    if (count > 1) {
                        AlertUtils.showInfo(count + " alunos online no momento", this);
                    } else {
                        AlertUtils.showInfo("Apenas um usuário online no momento", this);
                    }
                }
                isFirstSearch = false;
            }
        }
    }

    private boolean containsInDistanceMin(CaronaUsuario meuUsuarioCarona, CaronaUsuario caronaUsuario) {
        if (hasDistanceMin) {
            Location locOne = new Location("");
            Location locTwo = new Location("");
            locOne.setLatitude(meuUsuarioCarona.getPositionResidence().getLatitude());
            locOne.setLongitude(meuUsuarioCarona.getPositionResidence().getLongitude());
            locTwo.setLatitude(caronaUsuario.getPositionResidence().getLatitude());
            locTwo.setLongitude(caronaUsuario.getPositionResidence().getLongitude());
            float distanceMin = meuUsuarioCarona.getStatusCarona() == StatusCarona.DAR_CARONA ? searchViewHV.getDistanceMin() : 2000f;
            return locOne.distanceTo(locTwo) <= distanceMin;
        } else {
            return true;
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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    String uri = "tel:" + numberTelephone;
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
        if (mChatDialog.isExpanded()) {
            mChatDialog.collapse();
            return;
        }

        if (dialogSelectorItemMapView != null && dialogSelectorItemMapView.getCurrentState() != DialogItemMapView.TYPE_STATE.HIDE) {
            hideDetailedEstablishementSelected();
            return;
        }
        if (caronaUsuarioDialogBottom != null && caronaUsuarioDialogBottom.isShowing()) {
            caronaUsuarioDialogBottom.dismiss();
            return;
        }
        if (hasClose) {
            CaronaUsuarioFirebase.exit(this, mUsuarioCarona.getCpfUsuario());
            super.onBackPressed();
        } else {
            Toast.makeText(HomeAlunoActivity.this, "Aperte mais uma vez para sair", Toast.LENGTH_SHORT).show();
            hasClose = true;
            initTimeToClose();
        }

    }

    private void initTimeToClose() {
        new Handler().postDelayed(() -> {
            try {
                hasClose = false;
            } catch (Exception ignore) {

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

    private BubbleShowCaseBuilder createBubbleTutorial(String title, String description, View view, String idImage) {
        return createBubbleTutorial(title, description, view, idImage, R.drawable.icon_pedindo_carona_branco, BubbleShowCase.ArrowPosition.TOP);
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
//        CaronaUsuarioFirebase.exit(this, Preferences.getLastCpfLogged(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    private boolean validateUser(CaronaUsuario caronaUsuario, String query) {
        return !usuario.getCpf().equals(caronaUsuario.getCpfUsuario()) &&   // Se não for eu mesmo
                caronaUsuario.getNome().toLowerCase().contains(query.toLowerCase()) &&  // se o nome contém dentro da query de pesquisa (se for vazio sempre contém)
                (containsInDistanceMin(mUsuarioCarona, caronaUsuario));  // se o usuário está dentro da distância mínima (caso tenha distância mínima)
    }
}


