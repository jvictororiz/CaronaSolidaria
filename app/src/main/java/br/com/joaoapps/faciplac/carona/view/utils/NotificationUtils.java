package br.com.joaoapps.faciplac.carona.view.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.joaov.faciplac.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.view.activity.HomeAlunoActivity;
import br.com.joaoapps.faciplac.carona.view.activity.LoginActivity;

public class NotificationUtils {
    public static void showNotification(Context context, int idNotification, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int icon;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.icon_carona;
        } else {
            icon = R.drawable.positive_joia_icon;
        }

        String channelId = "chanel_message";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, HomeAlunoActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelId, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(idNotification, mBuilder.build());
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}
