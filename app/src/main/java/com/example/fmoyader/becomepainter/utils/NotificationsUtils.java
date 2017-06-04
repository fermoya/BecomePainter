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

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.activities.CanvasDrawerActivity;

/**
 * Created by fmoyader on 28/5/17.
 */

public class NotificationsUtils {

    private static final int ID_PAINTING_SAVED_NOTIFICATION = 1000;
    private static final int ACTION_SEE_DRAWINGS_INTENT_ID = 100;
    private static final int CANVAS_INTENT_ID = 200;

    public static void sendNewPaintingSavedNotification(Context context) {
        boolean isNotificationsActive = DrawingPreferencesManager.getInstance(context).isNotificationsActive();
        if (!isNotificationsActive) {
            return;
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        PendingIntent contentIntent = buildContentIntent(context);
        PendingIntent actionIntent = buildActionIntent(context);

        Bitmap largeIcon = buildLargeIcon(context);
        Notification notification = builder.setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_painting_saved_title))
                .setContentText(context.getString(R.string.notification_painting_saved_description))
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.become_painter_icon)
                .setLargeIcon(largeIcon)
                .setContentIntent(contentIntent)
                .addAction(
                        R.drawable.ic_done_white_24dp,
                        context.getString(R.string.notification_action_see_drawings),
                        actionIntent
                )
                .build();
        notificationManager.notify(ID_PAINTING_SAVED_NOTIFICATION, notification);
    }

    private static Bitmap buildLargeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.become_painter_icon);
    }

    private static PendingIntent buildContentIntent(Context context) {
        Intent intentToCanvasActivity = new Intent(context, CanvasDrawerActivity.class);
        return PendingIntent.getActivity(
                context, CANVAS_INTENT_ID, intentToCanvasActivity, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent buildActionIntent(Context context) {
        Intent intentToDrawingListFragment = new Intent(context, CanvasDrawerActivity.class);
        intentToDrawingListFragment.putExtra(CanvasDrawerActivity.EXTRA_SEE_DRAWINGS, true);
        return PendingIntent.getActivity(
                context, ACTION_SEE_DRAWINGS_INTENT_ID, intentToDrawingListFragment, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void sendReminderNotification(Context context, String title, String author, String numberOfLines) {
        boolean isNotificationsActive = DrawingPreferencesManager.getInstance(context).isNotificationsActive();
        if (!isNotificationsActive) {
            return;
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        PendingIntent pendingIntent = buildContentIntent(context);
        Bitmap largeIcon = buildLargeIcon(context);

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
