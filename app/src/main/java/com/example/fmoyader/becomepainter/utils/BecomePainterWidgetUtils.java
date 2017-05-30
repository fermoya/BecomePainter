package com.example.fmoyader.becomepainter.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.fmoyader.becomepainter.widget.BecomePainterAppWidget;

/**
 * Created by fmoyader on 11/5/17.
 */

public class BecomePainterWidgetUtils {
    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, BecomePainterAppWidget.class);
        intent.setAction(BecomePainterAppWidget.ACTION_UPDATE_LIST);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, BecomePainterAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(intent);
    }
}
