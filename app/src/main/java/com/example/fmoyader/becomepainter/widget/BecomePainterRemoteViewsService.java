package com.example.fmoyader.becomepainter.widget;


import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by fmoyader on 29/5/17.
 */

public class BecomePainterRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BecomePainterRemoteViewsFactory(getApplicationContext(), intent);
    }
}
