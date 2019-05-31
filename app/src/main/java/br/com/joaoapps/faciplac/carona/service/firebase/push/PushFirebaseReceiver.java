package br.com.joaoapps.faciplac.carona.service.firebase.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.joaov.faciplac.caronasolidaria.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaBody;
import br.com.joaoapps.faciplac.carona.view.activity.HomeAlunoActivity;
import br.com.joaoapps.faciplac.carona.view.activity.LoginActivity;
import kotlin.jvm.Throws;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by joaov on 18/11/2017.
 */

public class PushFirebaseReceiver extends FirebaseMessagingService {
    public static final String INTENT_FILTER_USER_COMUNICATION = "ENTRY_USER_COMUNICATION";
    private static final String ID_NOTIFICATION = "ID_NOTIFICATION";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.i("apagar", remoteMessage.getData().toString());
            trataPushDados(remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            trataNotification(remoteMessage.getNotification());
        }

    }

    private void trataPushDados(Map<String, String> data) {
        try {
            ComunicationCaronaBody comunicationCaronaBody = new ComunicationCaronaBody(data);
            Intent intent = new Intent(INTENT_FILTER_USER_COMUNICATION);
            intent.putExtra(HomeAlunoActivity.ENTRY_COMUNICATION, comunicationCaronaBody);

            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trataNotification(RemoteMessage.Notification notification) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int icon;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.icon;
        } else {
            icon = R.drawable.icon_carona;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(icon)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, LoginActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        assert notificationManager != null;
        notificationManager.notify(notificationId, mBuilder.build());

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}