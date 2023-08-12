package com.example.uas_10120090_akb;
//NIM   : 10102090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class NotificationUtils {
    @SuppressLint("MissingPermission")
    public static void showNotification(Context context, String title, String message) {

        createNotificationChannel(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "2043653629476828747")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "2043653629476828747";
            String channelName = "UAS_10120090_AKB";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
