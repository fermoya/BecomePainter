package com.example.fmoyader.becomepainter.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.fmoyader.becomepainter.activities.CanvasActivity;
import com.example.fmoyader.becomepainter.R;

/**
 * Created by fmoyader on 28/5/17.
 */

public class NotificationsUtils {

    private static final int ID_PAINTING_SAVED_NOTIFICATION = 1000;

    public static void sendNewPaintingSavedNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        //TODO: differente acions to navigate to different scenes
        //TODO: change to navigate to List of drawings
        Intent intentToCanvasActivity = new Intent(context, CanvasActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intentToCanvasActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.become_painter_icon);
        Notification notification = builder.setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_painting_saved_title))
                .setContentText(context.getString(R.string.notification_painting_saved_description))
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.become_painter_icon)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(ID_PAINTING_SAVED_NOTIFICATION, notification);
    }

    public static void sendReminderNotification(Context context, String title, String author, String numberOfLines) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent intentToCanvasActivity = new Intent(context, CanvasActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intentToCanvasActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.become_painter_icon);

        String message = String.format(
                context.getString(R.string.notification_reminder_description),
                title, author, numberOfLines
        );

        Notification notification = builder.setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_notification_reminder_title))
                .setContentText(message)
                .setPriority(Notification.PRIORITY_LOW)
                .setSmallIcon(R.drawable.become_painter_icon)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(ID_PAINTING_SAVED_NOTIFICATION, notification);
    }

}
