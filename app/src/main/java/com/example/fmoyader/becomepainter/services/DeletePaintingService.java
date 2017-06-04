package com.example.fmoyader.becomepainter.services;

import android.app.IntentService;
import android.content.Intent;

import com.example.fmoyader.becomepainter.sqlite.contract.PaintingContract;
import com.example.fmoyader.becomepainter.utils.BecomePainterWidgetUtils;
import com.example.fmoyader.becomepainter.utils.NotificationsUtils;


public class DeletePaintingService extends IntentService {

    public static final String EXTRA_PAINTING_ID = "extra_painting_id";

    public DeletePaintingService() {
        super(DeletePaintingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_PAINTING_ID)) {
            String paintingId = intent.getStringExtra(EXTRA_PAINTING_ID);
            getApplicationContext().getContentResolver().delete(
                    PaintingContract.PaintingEntry.CONTENT_URI.buildUpon().appendPath(paintingId).build(),
                    null, null
            );
            BecomePainterWidgetUtils.updateWidget(getApplicationContext());
        }
    }

}
