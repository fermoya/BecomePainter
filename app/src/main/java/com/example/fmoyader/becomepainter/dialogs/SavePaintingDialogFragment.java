package com.example.fmoyader.becomepainter.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fmoyader.becomepainter.R;

/**
 * Created by fmoyader on 27/5/17.
 */

public class SavePaintingDialogFragment extends DialogFragment {

    private EditText descriptionEditText;
    private EditText authorEditText;
    private EditText titleEditText;
    private TextView errorTextView;

    public interface OnPositiveButtonListener {
        void onInfoValidated(String title, String author, String description);
    }

    private OnPositiveButtonListener onPositiveButtonListener;

    public void setOnPositiveButtonListener(OnPositiveButtonListener onPositiveButtonListener) {
        this.onPositiveButtonListener = onPositiveButtonListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = View.inflate(getContext(), R.layout.dialog_save_painting, null);
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_painting_description);
        authorEditText = (EditText) view.findViewById(R.id.edit_text_painting_author);
        titleEditText = (EditText) view.findViewById(R.id.edit_text_painting_title);
        errorTextView = (TextView) view.findViewById(R.id.text_view_error);

        AlertDialog dialog = builder.setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_save_text, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.dialog_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }

    public View.OnClickListener positiveClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String paintingTitle = titleEditText.getText().toString();
            String paintingAuthor = authorEditText.getText().toString();
            String paintingDescription = descriptionEditText.getText().toString();
            if (paintingTitle.isEmpty()
                    || paintingAuthor.isEmpty()) {
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                if (onPositiveButtonListener != null) {
                    onPositiveButtonListener.onInfoValidated(paintingTitle, paintingAuthor, paintingDescription);
                }
                getDialog().dismiss();
            }
        }
    };

}
