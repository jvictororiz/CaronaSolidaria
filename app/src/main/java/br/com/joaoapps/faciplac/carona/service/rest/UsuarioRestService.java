package br.com.joaoapps.faciplac.carona.service.rest;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.service.ServicePost;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaRequest;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaBody;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.Notification;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.NotificationRequest;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ResponseBody;
import br.com.joaoapps.faciplac.carona.service.listeners.OnEventListener;
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


    public void sendNotification(final Notification notification, final OnEventListener<Void> onEventListener) {
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
                onEventListener.error(Code.ERRO_AO_ENVIAR_PUSH);
            }
        });
        thread.start();
    }

    public void pedirCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnEventListener<Void> onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_ONE_COMUNICATION, onEventListener, StatusCarona.RECEBER_CARONA, myUser, otherUser);
    }

    public void oferecerCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnEventListener<Void> onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_ONE_COMUNICATION, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }

    public void negarCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnEventListener<Void> onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_TWO_DENIED, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }

    public void aceitarCarona(CaronaUsuario myUser, CaronaUsuario otherUser, final OnEventListener<Void> onEventListener) {
        serviceCaronaDialogsAlert(ComunicationCaronaBody.STEP_TWO_ACCEPT, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);

    }

    public void sendMessageChat(CaronaUsuario myUser, CaronaUsuario otherUser, String message, final OnEventListener<Void> onEventListener) {
        ComunicationCaronaBody sendMessageComunication = new ComunicationCaronaBody(ComunicationCaronaBody.SEND_OR_RECEIVE_MESSAGE, myUser, otherUser, message);
        serviceCarona(sendMessageComunication, onEventListener, StatusCarona.DAR_CARONA, myUser, otherUser);
    }


    private void serviceCaronaDialogsAlert(final int step, final OnEventListener<Void> onEventListener, final StatusCarona statusCarona, final CaronaUsuario myUser, final CaronaUsuario otherUser) {
        ComunicationCaronaBody notificationRequest = new ComunicationCaronaBody(step, myUser, otherUser, statusCarona);
        serviceCarona(notificationRequest, onEventListener, statusCarona, myUser, otherUser);
    }

    private void serviceCarona(ComunicationCaronaBody notificationRequest, final OnEventListener<Void> onEventListener, final StatusCarona statusCarona, final CaronaUsuario myUser, final CaronaUsuario otherUser) {
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
                onEventListener.error(Code.ERRO_AO_ENVIAR_PUSH);
            }
        });
        thread.start();
    }

    private void defaultThreatment(final AppCompatActivity context, final ResponseBody responseBody, final OnEventListener<Void> onEventListener) {
        context.runOnUiThread(() -> {
            if (responseBody.getSuccess() == 1) {
                context.runOnUiThread(() -> onEventListener.success(null));

            } else {
                onEventListener.error(Code.ERRO_AO_ENVIAR_PUSH);
            }

        });
    }
}

