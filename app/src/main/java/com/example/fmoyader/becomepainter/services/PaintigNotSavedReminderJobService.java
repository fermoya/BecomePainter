package com.example.fmoyader.becomepainter.services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.utils.NotificationsUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by fmoyader on 28/5/17.
 */

public class PaintigNotSavedReminderJobService extends JobService {

    public static final String EXTRA_PAINTING_TITLE = "extra_painting_title";
    public static final String EXTRA_PAINTING_AUTHOR = "extra_painting_author";
    public static final String EXTRA_PAINTING_NUMBER_OF_LINES = "extra_painting_number_of_lines";
    private AsyncTask<Void, Void, Void> backgroundTask;

    @Override
    public boolean onStartJob(JobParameters params) {
        Bundle extras = params.getExtras();
        final String title = findInBundle(EXTRA_PAINTING_TITLE, extras);
        final String author = findInBundle(EXTRA_PAINTING_AUTHOR, extras);
        final String numberOfLines = findInBundle(EXTRA_PAINTING_NUMBER_OF_LINES, extras);

        backgroundTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                NotificationsUtils.sendReminderNotification(
                        getApplicationContext(), title, author, numberOfLines);
                return null;
            }
        };

        Log.d(getString(R.string.tag_debug_reminder), "New reminder scheduled " + title + " by " + author);

        backgroundTask.execute();
        return true;
    }

    public String findInBundle(String key, Bundle extras) {
        if (key.equals(EXTRA_PAINTING_TITLE)) {
            return extras.containsKey(EXTRA_PAINTING_TITLE) ?
                    extras.getString(EXTRA_PAINTING_TITLE) : getString(R.string.default_title);
        }
        if (key.equals(EXTRA_PAINTING_AUTHOR)) {
            return extras.containsKey(EXTRA_PAINTING_AUTHOR) ?
                    extras.getString(EXTRA_PAINTING_AUTHOR) : getString(R.string.default_author);
        }
        if (key.equals(EXTRA_PAINTING_NUMBER_OF_LINES)) {
            return extras.containsKey(EXTRA_PAINTING_NUMBER_OF_LINES) ?
                    extras.getString(EXTRA_PAINTING_NUMBER_OF_LINES) : getString(R.string.default_number_of_lines);
        }

        return null;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (backgroundTask != null) backgroundTask.cancel(true);
        return false;
    }
}
