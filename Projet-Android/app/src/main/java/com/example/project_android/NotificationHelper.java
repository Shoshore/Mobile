package com.example.project_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private static final String CHANNEL_PHOTOS    = "channel_photos";
    private static final String CHANNEL_GROUPES   = "channel_groupes";
    private static final String CHANNEL_LIEUX     = "channel_lieux";
    private static int notifId = 1000;

    // Creer les canaux (a appeler au demarrage de l'app)
    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            manager.createNotificationChannel(new NotificationChannel(
                    CHANNEL_PHOTOS,
                    "Nouvelles photos",
                    NotificationManager.IMPORTANCE_DEFAULT));

            manager.createNotificationChannel(new NotificationChannel(
                    CHANNEL_GROUPES,
                    "Activite des groupes",
                    NotificationManager.IMPORTANCE_DEFAULT));

            manager.createNotificationChannel(new NotificationChannel(
                    CHANNEL_LIEUX,
                    "Photos par lieu",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    // Notification nouvelle photo publiee par un utilisateur suivi
    public static void notifyNewPhoto(Context context, String auteur, String lieu) {
        sendNotification(context, CHANNEL_PHOTOS,
                "📸 Nouvelle photo de " + auteur,
                auteur + " a publie une photo a : " + lieu);
    }

    // Notification activite dans un groupe
    public static void notifyGroupeActivite(Context context,
                                            String groupe, String auteur) {
        sendNotification(context, CHANNEL_GROUPES,
                "👥 Activite dans " + groupe,
                auteur + " a publie une nouvelle photo dans le groupe.");
    }

    // Notification photo publiee dans un lieu suivi
    public static void notifyLieuSuivi(Context context, String lieu) {
        sendNotification(context, CHANNEL_LIEUX,
                "📍 Nouveau contenu a : " + lieu,
                "Une nouvelle photo a ete publiee a " + lieu + ".");
    }

    private static void sendNotification(Context context, String channelId,
                                         String title, String content) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        try {
            NotificationManagerCompat.from(context).notify(notifId++, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}