package com.example.fmoyader.becomepainter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.fmoyader.becomepainter.canvas.dto.Painting;
import com.example.fmoyader.becomepainter.canvas.view.CanvasView;
import com.example.fmoyader.becomepainter.dialogs.SavePaintingDialogFragment;
import com.example.fmoyader.becomepainter.preferences.DrawingPreferencesManager;
import com.example.fmoyader.becomepainter.utils.SQLiteUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CanvasActivity extends AppCompatActivity implements SavePaintingDialogFragment.OnPositiveButtonListener {

    @BindView(R.id.canvas)
    CanvasView canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        ButterKnife.bind(this);
    }

    public void startSavePaintingDialog(View view) {
        SavePaintingDialogFragment dialogFragment = new SavePaintingDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(getSupportFragmentManager(), null);

        getSupportFragmentManager().executePendingTransactions();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogFragment.positiveClickListener);
    }

    public void startErasePaintingDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_erase_painting_title))
                .setMessage(getString(R.string.dialog_erase_painting_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alert_erase_text), null)
                .setNegativeButton(R.string.alert_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        DrawingPreferencesManager.getInstance(this).setPendingPainting();
        super.onDestroy();
    }

    @Override
    public void onInfoValidated(String title, String author, String description) {
        Painting painting = canvasView.getPainting();
        if (!painting.isBlank()) {
            painting.setAuthor(author);
            painting.setDescription(description);
            painting.setTitle(title);

            //TODO: hacerlo en background
            SQLiteUtils.persistInDatabase(painting, this);
            canvasView.reset();
        }

    }
}
