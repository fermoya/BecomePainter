package com.example.fmoyader.becomepainter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.fmoyader.becomepainter.R;

/**
 * Created by fmoyader on 26/5/17.
 */

public class DrawingPreferencesManager {

    private Context context;
    private static DrawingPreferencesManager drawingPreferences;

    private DrawingPreferencesManager(Context context) {
        this.context = context;
    }

    public synchronized static DrawingPreferencesManager getInstance(Context context) {
        if (drawingPreferences == null) {
            drawingPreferences = new DrawingPreferencesManager(context);
        }

        return drawingPreferences;
    }

    public void setPendingPainting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.preference_pending_painting_key), true);
        editor.commit();
    }

    public boolean isPendingPainting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(
                context.getString(R.string.preference_pending_painting_key),
                context.getResources().getBoolean(R.bool.preference_pending_painting_default));
    }

    public boolean isNotificationsActive() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(
                context.getString(R.string.preference_activate_notifications_key),
                context.getResources().getBoolean(R.bool.preference_activate_notification_default));
    }

}
