package com.example.fmoyader.becomepainter.services;

import android.app.IntentService;
import android.content.Intent;


public class SyncPaintingService extends IntentService {

    public SyncPaintingService() {
        super(SyncPaintingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
