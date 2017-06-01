package com.example.fmoyader.becomepainter.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.activities.CanvasDrawerActivity;
import com.example.fmoyader.becomepainter.dialogs.SaveNewPaintingDialogFragment;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.services.PaintigNotSavedReminderJobService;
import com.example.fmoyader.becomepainter.services.SavePaintingService;
import com.example.fmoyader.becomepainter.utils.SQLiteUtils;
import com.example.fmoyader.becomepainter.views.CanvasView;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CanvasFragment extends Fragment implements SaveNewPaintingDialogFragment.OnPositiveButtonListener,
        CanvasView.OnDrawingListener, View.OnClickListener, LoaderManager.LoaderCallbacks<Painting>{

    private static final int ID_PAINTING_LOADER = 2001;
    @BindView(R.id.canvas)
    CanvasView canvasView;
    @BindView(R.id.button_save)
    Button savePaintingButton;
    @BindView(R.id.button_erase)
    Button erasePaintingButton;

    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS / 10;

    private static final String REMINDER_JOB_TAG = "reminder_tag";
    private Driver driver;

    public CanvasFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        ButterKnife.bind(this, view);
        savePaintingButton.setOnClickListener(this);
        erasePaintingButton.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(CanvasDrawerActivity.EXTRA_PAINTING_ID)) {
            String paintingId = bundle.getString(CanvasDrawerActivity.EXTRA_PAINTING_ID);
            Log.d(getString(R.string.tag_canvas_activity), paintingId);
            loadPaiting(paintingId);
        }

        return view;
    }

    private void loadPaiting(String paintingId) {
        Bundle bundle = new Bundle();
        bundle.putString(CanvasDrawerActivity.EXTRA_PAINTING_ID, paintingId);
        getActivity().getSupportLoaderManager().restartLoader(
               ID_PAINTING_LOADER, bundle, this
        );
    }

    public void showSavingDialog() {
        if (canvasView.getPainting().getId() == 0) {
            showSaveNewPaintingDialog();
        } else {
            showSavePaintingDialog();
        }
    }

    private void showSavePaintingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_save_painting_title))
                .setMessage(getString(R.string.dialog_save_painting_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.dialog_save_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePainting(canvasView.getPainting());
                        canvasView.reset();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showSaveNewPaintingDialog() {
        SaveNewPaintingDialogFragment dialogFragment = new SaveNewPaintingDialogFragment();
        dialogFragment.setOnPositiveButtonListener(this);
        dialogFragment.show(getFragmentManager(), null);

        getFragmentManager().executePendingTransactions();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogFragment.positiveClickListener);
    }

    public void showErasePaintingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    }
                })
                .show();
    }

    @Override
    public void onInfoValidated(String title, String author, String description) {
        Painting painting = canvasView.getPainting();
        if (!painting.isBlank()) {
            painting.setAuthor(author);
            painting.setDescription(description);
            painting.setTitle(title);

            savePainting(painting);
            canvasView.reset();
        }

    }

    private void savePainting(Painting painting) {
        Intent intentToSyncPaintingService = new Intent(getActivity(), SavePaintingService.class);
        intentToSyncPaintingService.putExtra(SavePaintingService.EXTRA_PAINTING, painting);
        getActivity().startService(intentToSyncPaintingService);
    }

    @Override
    public void onNewPaintingStarted() {
        if (driver != null) {
            driver.cancelAll();
        } else {
            driver = new GooglePlayDriver(getContext());
        }
        scheduleJob();
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

    @Override
    public void onDetach() {
        super.onDetach();
        erasePaintingButton.setOnClickListener(null);
        savePaintingButton.setOnClickListener(null);
        canvasView.setOnDrawingListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                showSavingDialog();
                break;
            case R.id.button_erase:
                showErasePaintingDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public Loader<Painting> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Painting>(getContext()) {

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                forceLoad();
            }

            @Override
            public Painting loadInBackground() {
                String paintingId = args.getString(CanvasDrawerActivity.EXTRA_PAINTING_ID);
                Painting painting = SQLiteUtils.fetchPaintingById(paintingId, getContext());
                return painting;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Painting> loader, Painting painting) {
        canvasView.drawPainting(painting);
    }

    @Override
    public void onLoaderReset(Loader<Painting> loader) {

    }
}
