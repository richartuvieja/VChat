package com.kuikos.vchat.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.kuikos.vchat.ChatApplication;
import com.firebase.androidchat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kuikos.vchat.activities.MainActivity;

/**
 * Created by j_uhalde on 26/05/2016.
 */
public class MessageReceiver extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(RemoteMessage message) {

        Log.d(TAG, "From: " + message.getFrom());

        String mensaje = " ";
        String titulo = " ";

        String from = message.getFrom();

        if (from.startsWith("/topics/global")){
            mensaje = message.getNotification().getBody();
            sendNotification((int) System.currentTimeMillis(), "Alerta", mensaje);
        }


        if (from.startsWith("/topics/newMessage")){
            titulo = "Nuevo mensaje de " + message.getData().get("usuario");
            mensaje = message.getData().get("usuario");
            String user_actual = ((ChatApplication)this.getApplication()).getUser().reg_username;

            if (!user_actual.equals(message.getData().get("usuario").trim()))
                sendNotification(1, titulo, mensaje);
        }



    }


    private void sendNotification(int messageId, String titulo, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, messageId, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.v)
                .setColor(ContextCompat.getColor(this.getBaseContext(), R.color.color_primary))
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId, notificationBuilder.build());
    }
}
