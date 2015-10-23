package com.webdroid.core.plugins.notify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.webdroid.R;
import com.webdroid.core.utils.ORes;

public class OAlert {
    public static final String TAG = OAlert.class.getSimpleName();

    private enum Type {
        Alert, Warning, Error
    }

    public static enum ConfirmType {
        POSITIVE, NEGATIVE
    }

    public static void showAlert(Context context, String message) {
        showAlert(context, message, null);
    }

    public static void showWarning(Context context, String message) {
        showWarning(context, message, null);
    }

    public static void showError(Context context, String message) {
        showError(context, message, null);
    }

    public static void showAlert(Context context, String message, OnAlertDismissListener listener) {
        show(context, message, Type.Alert, listener);
    }

    public static void showWarning(Context context, String message, OnAlertDismissListener listener) {
        show(context, message, Type.Warning, listener);
    }

    public static void showError(Context context, String message, OnAlertDismissListener listener) {
        show(context, message, Type.Error, listener);
    }

    private static void show(Context context, String message, Type type, final OnAlertDismissListener listener) {
        AlertDialog.Builder mBuilder;
        mBuilder = new AlertDialog.Builder(context);
        switch (type) {
            case Alert:
                mBuilder.setTitle(R.string.label_alert);
                break;
            case Error:
                mBuilder.setTitle(R.string.label_error);
                break;
            case Warning:
                mBuilder.setTitle(R.string.label_warning);
        }
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onAlertDismiss();
                }
            }
        });
        mBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    listener.onAlertCancel();
                }
            }
        });
        mBuilder.create().show();
    }

    public static void showConfirm(Context context, String message, final OnAlertConfirmListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onConfirmChoiceSelect(ConfirmType.POSITIVE);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onConfirmChoiceSelect(ConfirmType.NEGATIVE);
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    listener.onConfirmChoiceSelect(ConfirmType.NEGATIVE);
                }
            }
        });
        builder.create().show();
    }

    public static void inputDialog(Context context, String title, final OnUserInputListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = ORes.get(context)._dim(R.dimen.default_8dp);
        params.setMargins(margin, margin, margin, margin);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(margin, margin, margin, margin);
        final EditText edtInput = new EditText(context);
        edtInput.setLayoutParams(params);
        edtInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        if (listener != null) {
            listener.onViewCreated(edtInput);
        }
        linearLayout.addView(edtInput);
        builder.setView(linearLayout);
        if (title != null)
            builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(edtInput.getText())) {
                    edtInput.setError("Field required");
                    edtInput.requestFocus();
                } else {
                    if (listener != null) {
                        listener.onUserInputted(edtInput.getText());
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public static interface OnAlertConfirmListener {
        public void onConfirmChoiceSelect(ConfirmType type);
    }

    public static interface OnAlertDismissListener {
        void onAlertDismiss();

        void onAlertCancel();
    }

    public static interface OnUserInputListener {
        public void onViewCreated(EditText inputView);

        public void onUserInputted(Object value);
    }
}
