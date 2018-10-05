package br.com.joaoapps.faciplac.carona.service.rest;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.service.ServicePost;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaRequest;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaBody;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.Notification;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.NotificationRequest;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ResponseBody;
import br.com.joaoapps.faciplac.carona.service.listeners.OnTransacaoListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by joaov on 19/11/2017.
 */

public class UsuarioRestService {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private AppCompatActivity context;

    public UsuarioRestService(AppCompatActivity context) {
        this.context = context;
    }


    public void sendNotification(final Notification notification, final OnTransacaoListener onTransacaoListener) {
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    NotificationRequest notificationRequest = new NotificationRequest(notification.getPushIdRemetente(), notification);
                    Gson gson = new Gson();
                    String gsonString = gson.toJson(notificationRequest);

                    RequestBody body = RequestBody.create(JSON, gsonString);
                    String gsonResponse = ServicePost.post(" https://fcm.googleapis.com/fcm/send", body);
                    final ResponseBody responseBody = gson.fromJson(gsonResponse, ResponseBody.class);
                    defaultThreatment(context, responseBody, onTransacaoListener);
                } catch (Exception e) {
                    e.printStackTrace();
                    onTransacaoListener.error(Code.ERRO_AO_ENVIAR_PUSH);
                }
            }
        });
        thread.start();
    }

    public void pedirCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnTransacaoListener onTransacaoListener) {
        serviceCarona(ComunicationCaronaBody.STEP_ONE_COMUNICATION, onTransacaoListener, StatusCarona.RECEBER_CARONA, myUser, otherUser);
    }

    public void oferecerCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnTransacaoListener onTransacaoListener) {
        serviceCarona(ComunicationCaronaBody.STEP_ONE_COMUNICATION, onTransacaoListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }

    public void negarCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnTransacaoListener onTransacaoListener) {
        serviceCarona(ComunicationCaronaBody.STEP_TWO_DENIED,onTransacaoListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }

    public void aceitarCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnTransacaoListener onTransacaoListener) {
        serviceCarona(ComunicationCaronaBody.STEP_TWO_ACCEPT,onTransacaoListener, StatusCarona.DAR_CARONA, myUser, otherUser);

    }


    private void serviceCarona(final int step, final OnTransacaoListener onTransacaoListener, final StatusCarona statusCarona, final CaronaUsuario myUser, final CaronaUsuario otherUser) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComunicationCaronaBody notificationRequest = new ComunicationCaronaBody(step, myUser, otherUser, statusCarona);
                    ComunicationCaronaRequest comunicationCaronaRequest = new ComunicationCaronaRequest(notificationRequest, otherUser.getPushId());
                    String gsonString = new Gson().toJson(comunicationCaronaRequest);
                    RequestBody body = RequestBody.create(JSON, gsonString);
                    String gsonResponse = ServicePost.post(" https://fcm.googleapis.com/fcm/send", body);
                    final ResponseBody responseBody = new Gson().fromJson(gsonResponse, ResponseBody.class);
                    defaultThreatment(context, responseBody, onTransacaoListener);
                } catch (Exception e) {
                    e.printStackTrace();
                    onTransacaoListener.error(Code.ERRO_AO_ENVIAR_PUSH);
                }
            }
        });
        thread.start();
    }

    private void defaultThreatment(final AppCompatActivity context, final ResponseBody responseBody, final OnTransacaoListener onTransacaoListener) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (responseBody.getSuccess() == 1) {
                    context.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            onTransacaoListener.success(null);
                        }
                    });

                } else {
                    onTransacaoListener.error(Code.ERRO_AO_ENVIAR_PUSH);
                }

            }
        });
    }
}

