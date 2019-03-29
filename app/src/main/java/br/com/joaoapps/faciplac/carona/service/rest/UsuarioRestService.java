package br.com.joaoapps.faciplac.carona.service.rest;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.service.ServicePost;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaBody;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaRequest;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.Notification;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.NotificationRequest;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ResponseBody;
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


    public void sendNotification(final Notification notification, OnEventListenerAbstract<Void> onEventListener) {
        final Thread thread = new Thread(() -> {
            try {
                NotificationRequest notificationRequest = new NotificationRequest(notification.getPushIdRemetente(), notification);
                Gson gson = new GsonBuilder().setLenient().create();
                String gsonString = gson.toJson(notificationRequest);

                RequestBody body = RequestBody.create(JSON, gsonString);
                String gsonResponse = ServicePost.post(" https://fcm.googleapis.com/fcm/send", body);
                final ResponseBody responseBody = gson.fromJson(gsonResponse, ResponseBody.class);
                defaultThreatment(context, responseBody, onEventListener);
            } catch (Exception e) {
                e.printStackTrace();
                onEventListener.onError(Code.ERRO_AO_ENVIAR_PUSH);
            }
        });
        thread.start();
    }

    public void pedirCarona(CaronaUsuario myUser, CaronaUsuario otherUser, OnEventListenerAbstract<Void> onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_ONE_COMUNICATION, onEventListener, StatusCarona.RECEBER_CARONA, myUser, otherUser);
    }

    public void oferecerCarona(CaronaUsuario myUser, CaronaUsuario otherUser, OnEventListenerAbstract<Void>  onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_ONE_COMUNICATION, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }

    public void negarCarona(CaronaUsuario myUser, CaronaUsuario otherUser, OnEventListenerAbstract<Void>  onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_TWO_DENIED, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }

    public void aceitarCarona(CaronaUsuario myUser, CaronaUsuario otherUser, OnEventListenerAbstract<Void> onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_TWO_ACCEPT, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);

    }

    public void sendMessageChat(CaronaUsuario myUser, CaronaUsuario otherUser, String message, OnEventListenerAbstract<Void> onEventListener) {
        ComunicationCaronaBody sendMessageCommunication = new ComunicationCaronaBody(ComunicationCaronaBody.RECEIVE_MESSAGE, myUser, otherUser, message);
        serviceCarona(sendMessageCommunication, onEventListener,  otherUser);
    }


    private void serviceCaronaDialogsAlert(final int step, OnEventListenerAbstract<Void> onEventListener, final StatusCarona statusCarona, final CaronaUsuario myUser, final CaronaUsuario otherUser) {
        ComunicationCaronaBody notificationRequest = new ComunicationCaronaBody(step, myUser, otherUser, statusCarona);
        serviceCarona(notificationRequest, onEventListener,  otherUser);
    }

    private void serviceCarona(ComunicationCaronaBody notificationRequest, final OnEventListenerAbstract<Void> onEventListener, final CaronaUsuario otherUser) {
        final Thread thread = new Thread(() -> {
            try {
                ComunicationCaronaRequest comunicationCaronaRequest = new ComunicationCaronaRequest(notificationRequest, otherUser.getPushId());
                String gsonString = new Gson().toJson(comunicationCaronaRequest);
                RequestBody body = RequestBody.create(JSON, gsonString);
                String gsonResponse = ServicePost.post(" https://fcm.googleapis.com/fcm/send", body);
                final ResponseBody responseBody = new Gson().fromJson(gsonResponse, ResponseBody.class);
                defaultThreatment(context, responseBody, onEventListener);
            } catch (Exception e) {
                e.printStackTrace();
                onEventListener.onError(Code.ERRO_AO_ENVIAR_PUSH);
            }
        });
        thread.start();
    }

    private void defaultThreatment(final AppCompatActivity context, final ResponseBody responseBody, OnEventListenerAbstract<Void> onEventListener) {
        context.runOnUiThread(() -> {
            if (responseBody.getSuccess() == 1) {
                context.runOnUiThread(() -> onEventListener.onSuccess(null));

            } else {
                onEventListener.onError(Code.ERRO_AO_ENVIAR_PUSH);
            }

        });
    }


}

