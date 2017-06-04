package com.example.fmoyader.becomepainter.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.activities.CanvasDrawerActivity;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.utils.SQLiteUtils;

import java.util.List;

/**
 * Created by fmoyader on 29/5/17.
 */

class BecomePainterRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final int appWidgetId;
    private Context context;
    private List<Painting> paintings;

    public BecomePainterRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        paintings = SQLiteUtils.fetchAllPaintingSummaries(context);
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        Log.d("WIDGET", "Size: " + paintings.size());
        return paintings.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        Painting painting = paintings.get(position);
        row.setTextViewText(R.id.text_view_author, painting.getAuthor());
        row.setTextViewText(R.id.text_view_title, painting.getTitle());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(CanvasDrawerActivity.EXTRA_PAINTING_ID, painting.getId() + "");
        row.setOnClickFillInIntent(R.id.linear_layout_widget_item, fillInIntent);

        Log.d(context.getString(R.string.tag_widget_factory), "Position " + position + ": Added painting " + painting.getTitle() + " by " + painting.getAuthor());

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
