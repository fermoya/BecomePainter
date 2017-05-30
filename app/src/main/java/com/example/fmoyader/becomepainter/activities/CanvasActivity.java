package com.example.fmoyader.becomepainter.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.utils.BecomePainterWidgetUtils;
import com.example.fmoyader.becomepainter.view.CanvasView;
import com.example.fmoyader.becomepainter.dialogs.SavePaintingDialogFragment;
import com.example.fmoyader.becomepainter.services.PaintigNotSavedReminderJobService;
import com.example.fmoyader.becomepainter.utils.DrawingPreferencesManager;
import com.example.fmoyader.becomepainter.services.SyncPaintingService;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CanvasActivity extends AppCompatActivity
        implements SavePaintingDialogFragment.OnPositiveButtonListener, CanvasView.OnDrawingListener {

    public static final String EXTRA_PAINTING_ID = "extra_painting_id";
    @BindView(R.id.canvas)
    CanvasView canvasView;

    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS / 10;

    private static final String REMINDER_JOB_TAG = "auto_saving_tag";
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        ButterKnife.bind(this);
        canvasView.setOnDrawingListener(this);

        Intent intent = getIntent();
        Log.d(getString(R.string.tag_canvas_activity), intent.hasExtra(EXTRA_PAINTING_ID) + "");
    }

    private void scheduleJob() {
        Painting painting = canvasView.getPainting();
        Bundle extras = new Bundle();

        if (painting.getTitle() != null && !painting.getTitle().isEmpty()) {
            extras.putString(PaintigNotSavedReminderJobService.EXTRA_PAINTING_TITLE, painting.getTitle());
        }
        if (painting.getTitle() != null && !painting.getTitle().isEmpty()) {
            extras.putString(PaintigNotSavedReminderJobService.EXTRA_PAINTING_AUTHOR, painting.getAuthor());
        }

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(PaintigNotSavedReminderJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setExtras(extras)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);
    }

    public void showSavePaintingDialog(View view) {
        SavePaintingDialogFragment dialogFragment = new SavePaintingDialogFragment();
        dialogFragment.setOnPositiveButtonListener(this);
        dialogFragment.show(getSupportFragmentManager(), null);

        getSupportFragmentManager().executePendingTransactions();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogFragment.positiveClickListener);
    }

    public void showErasePaintingDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_erase_painting_title))
                .setMessage(getString(R.string.dialog_erase_painting_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.dialog_erase_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canvasView.reset();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BecomePainterWidgetUtils.updateWidget(CanvasActivity.this);
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        DrawingPreferencesManager.getInstance(this).setPendingPainting();
        driver.cancelAll();

        Painting painting = canvasView.getPainting();
        if (!painting.isBlank()) {
            if (!painting.isValid()) {
                painting.setTitle(getString(R.string.default_title));
                painting.setAuthor(getString(R.string.default_author));
            }

            Intent intentToSyncPaintingService = new Intent(this, SyncPaintingService.class);
            intentToSyncPaintingService.putExtra(SyncPaintingService.EXTRA_PAINTING, painting);
            startService(intentToSyncPaintingService);
        }

        super.onDestroy();
    }

    @Override
    public void onInfoValidated(String title, String author, String description) {
        Painting painting = canvasView.getPainting();
        if (!painting.isBlank()) {
            painting.setAuthor(author);
            painting.setDescription(description);
            painting.setTitle(title);

            Intent intentToSyncPaintingService = new Intent(this, SyncPaintingService.class);
            intentToSyncPaintingService.putExtra(SyncPaintingService.EXTRA_PAINTING, painting);
            startService(intentToSyncPaintingService);

            canvasView.reset();
        }

    }

    @Override
    public void onNewPaintingStarted() {
        if (driver != null) {
            driver.cancelAll();
        } else {
            driver = new GooglePlayDriver(this);
        }
        scheduleJob();
    }
}
