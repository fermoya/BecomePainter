package com.example.fmoyader.becomepainter.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.activities.CanvasActivity;

/**
 * Created by fmoyader on 29/5/17.
 */

public class BecomePainterAppWidget extends AppWidgetProvider {
    public static final String ACTION_UPDATE_LIST = "update_widget_list_action";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            Intent intentToRemoteViewsService = new Intent(context, BecomePainterRemoteViewsService.class);

            //TODO:sobra
            intentToRemoteViewsService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intentToRemoteViewsService.setData(Uri.parse(intentToRemoteViewsService.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.widget_list_of_paintings);

            widget.setRemoteAdapter(R.id.list_view_paintings, intentToRemoteViewsService);
            widget.setEmptyView(R.id.list_view_paintings, R.id.text_view_empty_message);

            Intent intentToMainActivity = new Intent(context, CanvasActivity.class);
            PendingIntent intentClickGeneral = TaskStackBuilder.create(context)
                    .addNextIntent(intentToMainActivity)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setOnClickPendingIntent(R.id.linear_layout_widget, intentClickGeneral);

            Intent intentToDetailActivity = new Intent(context, CanvasActivity.class);
            PendingIntent intentClickTemplate = TaskStackBuilder.create(context)
                    .addNextIntent(intentToDetailActivity)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.list_view_paintings, intentClickTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, widget);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equalsIgnoreCase(ACTION_UPDATE_LIST)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, BecomePainterAppWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_paintings);
        }
    }
}
