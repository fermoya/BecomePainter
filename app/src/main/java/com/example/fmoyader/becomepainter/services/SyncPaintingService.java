package com.example.fmoyader.becomepainter.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.utils.NotificationsUtils;
import com.example.fmoyader.becomepainter.utils.SQLiteUtils;


public class SyncPaintingService extends IntentService {

    public static final String EXTRA_PAINTING = "extra_painting";

    public SyncPaintingService() {
        super(SyncPaintingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_PAINTING)) {
            Painting painting = intent.getParcelableExtra(EXTRA_PAINTING);
            if (painting != null && !painting.isBlank()) {
                Log.d(getString(R.string.tag_debug_syncing), painting.getTitle() + " by " + painting.getAuthor() + " to be persisted");
                SQLiteUtils.persistInDatabase(painting, getApplicationContext());
                NotificationsUtils.sendNewPaintingSavedNotification(getApplicationContext());
            }
        }
    }
}
